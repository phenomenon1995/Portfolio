<template>
  <div class="drink-view-container">
    <div class="drink-view-components">
      <h2 class="drink-header">Drinks</h2>
      <!-- All Drink Components -->
      <div
        v-for="(drink, index) in allDrinks"
        v-bind:key="{ index }"
        class="drink-view-components"
      >
        <DrinkComponent
          v-bind:drink="drink"
          @show-popup="showSizePopUp(drink.productId)"
        />
      </div>
    </div>
  </div>

  <!-- Drink size pop-up -->
  <DrinkSizePopUp
    :showPopUp="popUpVisible"
    @hide-popup="hideSizePopUp()"
    @add-to-order="addToOrder()"
  ></DrinkSizePopUp>
  <Toast :message="toastMessage" v-if="showToast" />
</template>

<script>
import DrinkComponent from "../components/DrinkComponent.vue";
import DrinkSizePopUp from "../components/DrinkSizePopUp.vue";
import Toast from "../components/Toast.vue";
export default {
  data() {
    return {
      zipcode: this.$store.state.isDelivery.zipcode,
      allDrinks: this.$store.state.inventory.drinks,
      popUpVisible: false,
      currDrink: {},
      showToast: false,
      toastMessage: ''
    };
  },
  components: {
    DrinkComponent,
    DrinkSizePopUp,
    Toast,
  },
  methods: {
    showSizePopUp(id) {
      this.popUpVisible = true;
      this.currDrink = this.allDrinks.find((drink) => drink.productId === id);
      console.log(this.currDrink);
    },
    hideSizePopUp() {
      this.popUpVisible = false;
      console.log(this.popUpVisible);
    },

    addToOrder() {
      const zip = localStorage.getItem('zipcode') || this.$store.state.isDelivery.zipcode
      if (zip === null) {
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
        this.$store.commit("ADD_TO_CURR_ORDER", this.currDrink);
        let storedDrink = localStorage.getItem('drink')

      // Add to local storage
      if(storedDrink) {
        storedDrink = JSON.parse(storedDrink)
        storedDrink.push(this.currDrink)
        localStorage.removeItem('drink')
        localStorage.setItem('drink', JSON.stringify(storedDrink))
        
      } else {
        let drinks = []
        drinks.push(this.currDrink)
        localStorage.setItem('drink', JSON.stringify(drinks))
      }

        // Add to other cart
        this.$store.commit("ADD_TO_OTHER_CART", this.currDrink.productId);
        console.log(this.$store.state.cart);
        this.popUpVisible = false;
        this.toastMessage = 'Successfully added to your cart'
        this.showToast = true;
        this.$store.commit("TOGGLE_CART", true);
        setTimeout(() => {
          this.showToast = false;
        }, 1500);
      }
    },
  },
};
</script>

<style>
.drink-view-container {
  display: flex;
  flex-direction: column;
  column-gap: 30px;
  align-items: start;
  padding: 40px 40px;
}

.drink-view-components {
  width: 75%;
}

.drink-header {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 20px;
}

@media screen and (max-width: 680px) {
  .drink-view-components {
    width: 100%;
  }

  .drink-view-container {
    margin-bottom: 0px;
  }

  
}

@media screen and (max-width: 1080px) and (min-width: 681px) {
  .drink-view-components {
    width: 95%;
  }
}
</style>
