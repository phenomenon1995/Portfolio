<template>
  <div class="all-pizza-card-container" v-if="pizza?.quantity > 0">
    <div class="pizza-card" :style="pizzaStyle"></div>
    <div class="featured-pizza-card-details">
      <div class="featured-pizza-card-text-container">
        <h2 class="featured-pizza-card-text">{{ pizzaName }}</h2>
        <p class="featured-pizza-card-description">
          {{ pizzaDescription }}
          {{ name ? name : "" }}
        </p>
      </div>

      <button  class="add-to-cart-btn" v-on:click="addToPizzaCart">
        Add to Cart
      </button>

    </div>
    <Toast :message="toastMessage" v-if="showToast" />
  </div>
</template>

<script>
import Toast from './Toast.vue';
export default {
  data() {
    return {
      pizzaName: this.pizza?.description?.split("-")[0],
      pizzaDescription: this.pizza?.description?.split("-")[1],
      toastMessage: '',
      showToast: false
    };
  },
  name: "FeaturedPizzaCard",
  props: {
    pizza: Object,
    name: { type: String, required: false },
    isCustom: Boolean
  },
  components: {
    Toast
  },
  methods: {
    addToPizzaCart() {
      if (this.isCustom) {
        this.$router.replace("pizza/custom");
      } else {
        if (this.$store.state.isDelivery.zipcode === null) {
          this.popUpVisible = false;
          this.toastMessage =
            "Please add a zipcode, and choose carryout or delivery";

          this.showToast = true;

          this.$store.commit("TOGGLE_CART", true);
          setTimeout(() => {
            this.showToast = false;
          }, 2500);
        } else {
          // Add to current order in store
          this.$store.commit("ADD_TO_CURR_ORDER", this.pizza);
          let storedPizza = localStorage.getItem("specialtypizza");

          // Add to local storage
          if (storedPizza?.length > 0) {
            
            storedPizza = JSON.parse(storedPizza);
            storedPizza.push(this.pizza);
            localStorage.setItem("specialtypizza", JSON.stringify(storedPizza));
          } else {
            
            let pizzas = [];
            pizzas.push(this.pizza);
            localStorage.setItem("specialtypizza", JSON.stringify(pizzas));
          }

          // Add to other cart
          this.$store.commit("ADD_TO_OTHER_CART", this.pizza.productId);
          

          // show toast
          this.toastMessage = "Successfully added to your cart";
          this.showToast = true;
          this.$store.commit("TOGGLE_CART", true);
          setTimeout(() => {
            this.showToast = false;
          }, 1500);
        }
      }
    },
  },
  computed: {
    pizzaStyle() {
      return {  
        // Images must be placed inside of public folder
        backgroundImage: `url('./${this.pizzaName}.jpg')`,
      };
    },
  },
  // Your component's JavaScript logic goes here
};
</script>

<style scoped>
.all-pizza-card-container {
  display: grid;
  grid-template-columns: 1fr 4fr;
  border-radius: 9px;
  border: 1px solid #e1e1e1;
  margin-top: 20px;
  box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
  width: 100%;
  padding-right: 20px;
}

.all-pizza-card-container .add-to-cart-btn {
  justify-self: start;
  background-color: #e61d25;
  color: #fff;
  border-radius: 15px;
  border: none;
  padding: 15px 30px;
  cursor: pointer;
}
.all-pizza-card-container .add-to-cart-btn:hover {
  background-color: #000;
  color: #fff;
}
.pizza-card {
  position: relative;
  height: 200px;
  background-size: cover;
  background-position: center;
  border-radius: 9px 0px 0px 9px;
  background-image: url("../assets/Hawian.jpg");
  padding: 0px 30px;
}

.all-pizza-card-container .featured-pizza-card-details {
  display: flex;
  align-items: center;
}

.featured-pizza-card-details img {
  cursor: pointer;
}

.featured-pizza-card-details .featured-pizza-card-text {
  font-size: 1.8em;
  color: #333;
  font-size: large;
}

.featured-pizza-card-details .featured-pizza-card-description {
  font-size: 1em;
  color: #333;
  font-size: medium;
}

.pizza-card-container {
  border: #d8d8d8 solid 1px;
  border-radius: 10px;
}

.featured-pizza-card-text-container {
  width: 70%;
  padding: 0px 10px;
}

@media screen and (max-width: 680px) {
        .all-pizza-card-container {
            grid-template-columns: 1fr;
            
        }
        .all-pizza-card-container .featured-pizza-card-details {
          flex-direction: column;
          padding-bottom: 20px;
        }
    }
</style>
