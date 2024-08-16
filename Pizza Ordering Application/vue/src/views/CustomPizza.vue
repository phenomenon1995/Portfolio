<template>
  <div class="custom-pizza-view-container">
    <h1>Custom Pizza</h1>

    <!-- Choose size list -->
    <div class="pizza-size-cards">
      <PizzaSizeCardComponent
        v-for="(size, index) in size"
        v-bind:key="index"
        :size="size"
        @add-size="addSize(size)"
        >PizzaSizeCard</PizzaSizeCardComponent
      >
    </div>

    <!-- Crust list -->

    <div v-if="customPizza.size !== null">
      <h2>Crust</h2>
      <div class="crust-container">
        <CrustCardComponent
          v-for="(crust, index) in crust"
          v-bind:key="index"
          v-bind:crust="crust"
          @add-crust="addCrust(crust)"
        ></CrustCardComponent>
      </div>
    </div>

    <!-- Sauce list -->

    <div v-if="customPizza.crust !== null && customPizza.size !== null">
      <h2>Sauce</h2>
      <div class="sauce-container">
        <SauceCardComponent
          v-for="(sauce, index) in sauce"
          v-bind:key="index"
          v-bind:sauce="sauce"
          @add-sauce="addSauce(sauce)"
        ></SauceCardComponent>
      </div>
    </div>

    <!-- Toppings list -->

    <div
      v-if="
        customPizza.sauce !== null &&
        customPizza.crust !== null &&
        customPizza.size !== null
      "
    >
      <h2>Toppings</h2>
      <div class="topping-container">
        <ToppingCardComponent
          v-for="(topping, index) in topping"
          v-bind:key="index"
          v-bind:topping="topping"
          @add-topping="addTopping(topping)"
        ></ToppingCardComponent>
      </div>
    </div>

    <!-- Add to order btn -->
    <div class="add-to-cart-section" v-if="totalPrice > 0">
      <button :disabled="totalPrice == 0" @click="addToCart()">
        Add to Order
      </button>
      <p><span>Total Price:</span> ${{ totalPrice > 0 ? totalPrice : "" }}</p>
    </div>
    <toast v-if="showToast" :message="toastMessage" />
  </div>
</template>

<script>
import CrustCardComponent from "../components/CrustCardComponent.vue";
import PizzaSizeCardComponent from "../components/PizzaSizeCardComponent.vue";
import ToppingCardComponent from "../components/ToppingCardComponent.vue";
import SauceCardComponent from "../components/SauceCardComponent.vue";
import Toast from "../components/Toast.vue";

export default {
  data() {
    return {
      customPizza: {
        id: this.$store.state.cart.pizza.length + 1,
        description: "",
        price: 0.0,
        crust: null,
        sauce: null,
        size: null,
        topping: [],
      },
      allPizzas: this.$store.state.inventory.specialtyPizza,
      size: this.$store.state.inventory.size,

      crust: this.$store.state.inventory.crust,
      sauce: this.$store.state.inventory.sauce,
      showToast: false,
      toastMessage: "",
    };
  },
  components: {
    PizzaSizeCardComponent,
    ToppingCardComponent,
    CrustCardComponent,
    SauceCardComponent,
    Toast,
  },

  methods: {
    addTopping(topping) {
      const toppingElem = document.getElementById(
        `topping-${topping.productId}`
      );

      if (toppingElem.checked) {
        this.customPizza.topping.push(topping);
      } else if (!toppingElem.checked) {
        this.customPizza.topping = this.customPizza.topping.filter(
          (t) => t.productId !== topping.productId
        );
      }
    },

    addCrust(crust) {
      const crustElem = document.getElementById(`crust-${crust.productId}`);

      if (crustElem.checked) {
        this.customPizza.crust = crust;
        const allOtherCrustOptions =
          document.getElementsByClassName("crust-checkbox");
        for (let currCrust of allOtherCrustOptions) {
          if (currCrust.id == "crust-" + crust.productId) {
            continue;
          }

          currCrust.checked = false;
        }
      } else if (!crustElem.checked) {
        this.customPizza.crust = {};
      }
    },

    addSauce(sauce) {
      const sauceElem = document.getElementById(`sauce-${sauce.productId}`);

      if (sauceElem.checked) {
        this.customPizza.sauce = sauce;
        const allOtherSauceOptions =
          document.getElementsByClassName("sauce-checkbox");
        for (let currSauce of allOtherSauceOptions) {
          if (currSauce.id == "sauce-" + sauce.productId) {
            continue;
          }

          currSauce.checked = false;
        }
      } else if (!sauceElem.checked) {
        this.customPizza.sauce = {};
      }
    },

    addSize(size) {
      const sizeElem = document.getElementById(`size-${size.productId}`);

      if (!sizeElem.classList.contains("card-highlighted")) {
        this.customPizza.size = size;
        const allOtherSizeOptions = document.getElementsByClassName(
          "pizza-size-card-container"
        );
        for (let currSize of allOtherSizeOptions) {
          if (currSize.id == "size-" + size.productId) {
            currSize.classList.add("card-highlighted");
            continue;
          }

          currSize.classList.remove("card-highlighted");
        }

        console.log(this.customPizza);
      } else if (sizeElem.classList.contains("card-highlighted")) {
        sizeElem.classList.remove("card-highlighted");
        this.customPizza.size = {};
      }
    },

    addToCart() {
      console.log(this.$store.state.isDelivery.zipcode);
      // Check if zipcode and delivery is set
      if (this.zipcode === null) {
        this.toastMessage =
          "Please add a zipcode, and choose carryout or delivery";

        this.showToast = true;

        this.$store.commit("TOGGLE_CART", true);
        setTimeout(() => {
          this.showToast = false;
        }, 1500);
        return;
      }
      // Type is string
      this.customPizza.price = this.totalPrice;
      this.customPizza.description =
        this.customPizza.description.length === 0
          ? "Pizza"
          : this.customPizza.description;
      // Add to current order in store
      this.$store.commit("ADD_TO_CURR_ORDER", this.customPizza);

      let storedPizzas = localStorage.getItem("pizza");

      // Add to local storage
      if (storedPizzas) {
        storedPizzas = JSON.parse(storedPizzas);
        storedPizzas.push(this.customPizza);
        localStorage.removeItem("pizza");
        localStorage.setItem("pizza", JSON.stringify(storedPizzas));
      } else {
        let pizzas = [];
        pizzas.push(this.customPizza);
        localStorage.setItem("pizza", JSON.stringify(pizzas));
      }

      // Add to cart in store
      const toppingsIdList = [];
      for (let topping of this.customPizza.topping) {
        toppingsIdList.push(topping.productId);
      }
      const cartPizza = [
        {id: this.$store.state.cart.pizza.length + 1},
        this.customPizza.size?.productId,
        this.customPizza.sauce?.productId,
        this.customPizza.crust?.productId,
        ...toppingsIdList,
      ];
      this.$store.commit("ADD_TO_PIZZA_CART", cartPizza);
      console.log(this.$store.state.cart);

      // show toast
      this.toastMessage = "Successfully added item to cart";
      this.$store.commit("TOGGLE_CART", true);
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
        // Push to pizzas
        this.$router.replace('/pizza')
      }, 1500);
    },
  },
  computed: {
    totalPrice() {
      if (
        this.customPizza.crust?.productId &&
        this.customPizza.size?.productId &&
        this.customPizza.sauce?.productId
      ) {
        let totalComputedPrice = this.customPizza.size?.price;
        if (this.customPizza?.topping.length > 0) {
          for (let topping of this.customPizza.topping) {
            totalComputedPrice += topping?.price;
          }
        }
        return totalComputedPrice.toFixed(2);
      } else {
        return 0;
      }
    },

    zipcode() {
      return this.$store.state.isDelivery.zipcode;
    },

    topping() {
      return [
        ...this.$store.state.inventory.toppings,
        ...this.$store.state.inventory.premiumToppings,
      ];
    },
  },
};
</script>

<style>
.topping-container,
.crust-container,
.sauce-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  width: 80%;
  gap: 5px;
}
.custom-pizza-view-container {
  padding: 20px 10px;
}
.pizza-size-cards {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  padding: 10px;
  width: 70%;
}
.add-to-cart-section {
  display: flex;
  position: fixed;
  bottom: 0;
  width: 80vw;
  justify-content: space-between;
  align-items: center;
  padding: 0px 30px;
  background-color: #fff;
  border: 1px solid #000;
  border-radius: 5px;
  height: 100px;
  z-index: 999999999999999;
}

.add-to-cart-section button {
  background-color: #e61d25;
  color: #fff;
  border: none;
  border-radius: 15px;
  padding: 15px 35px;
  cursor: pointer;
  font-size: 1.3em;
}

.add-to-cart-section button:hover {
  background-color: #000;
}

.add-to-cart-section p {
  font-size: 1.3em;
}

.add-to-cart-section p span {
  font-weight: bold;
}

@media screen and (max-width: 650px) {
  .custom-pizza-view-container {
    margin-bottom: 0px;
  }

  .pizza-size-cards { 
    flex-direction: column;
  }
  .topping-container,
.crust-container,
.sauce-container {
  grid-template-columns: 1fr;
  width: 100%;
}
.add-to-cart-section button {
  padding: 3px;
  margin-right: 10px;
}

.add-to-cart-section {
  width: 90vw;
  margin-left: -20px;
}
}

@media screen and (max-width: 880px) and (min-width: 480px) {
  .pizza-size-cards { 
    width: 100%;
  }

  .crust-container, .sauce-container, .topping-container {
    width: 100%;
  }

}

</style>
