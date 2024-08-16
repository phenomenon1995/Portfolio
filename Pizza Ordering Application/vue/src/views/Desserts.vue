<template>
  <div class="dessert-view-container">
    <div class="dessert-view-components">
      <h2 class="dessert-header">Desserts</h2>
      <!-- All Dessert Components -->
      <div
        v-for="(dessert, index) in getAllDesserts"
        v-bind:key="{ index }"
        class="dessert-view-component"
      >
        <DessertComponent
          v-bind:dessert="dessert"
          :dessertName="dessert.description.split('-')[0].trim()"
          @add-to-cart="addToCart(dessert.productId)"
        />
      </div>
    </div>
  </div>

  <Toast :message="toastMessage" v-if="showToast" />
</template>

<script>
import DessertComponent from "../components/DessertComponent.vue";
import Toast from "../components/Toast.vue";

export default {
  data() {
    return {
      toastMessage: "",
      showToast: false,
      zipcode: this.$store.state.isDelivery.zipcode,
      allDesserts: this.$store.state.inventory.desserts,
      currDessert: {},
    };
  },
  methods: {
    addToCart(id) {
      console.log(this.$store.state.isDelivery.zipcode);
      if (this.$store.state.isDelivery.zipcode === null) {
        this.toastMessage =
          "Please add a zipcode, and choose carryout or delivery";

        this.showToast = true;

        this.$store.commit("TOGGLE_CART", true);
        setTimeout(() => {
          this.showToast = false;
        }, 1500);
        return;
      } else {
        // Add to current order in store
        this.currDessert = this.allDesserts.find(
          (dessert) => dessert.productId === id
        );
        this.$store.commit("ADD_TO_CURR_ORDER", this.currDessert);
        let storedDessert = localStorage.getItem("dessert");

        // Add to local storage
        if (storedDessert) {
          storedDessert = JSON.parse(storedDessert);
          storedDessert.push(this.currDessert);
          localStorage.setItem("dessert", JSON.stringify(storedDessert));
        } else {
          let dessert = [];
          dessert.push(this.currDessert);
          localStorage.setItem("dessert", JSON.stringify(dessert));
        }

        // Add to other cart
        this.$store.commit("ADD_TO_OTHER_CART", this.currDessert.productId);
        console.log(this.$store.state.cart);
        this.popUpVisible = false;
        this.toastMessage = "Successfully added to your cart";
        this.showToast = true;
        this.$store.commit("TOGGLE_CART", true);
        setTimeout(() => {
          this.showToast = false;
        }, 1500);
      }
    },
  },
  components: {
    DessertComponent,
    Toast,
  },
  computed: {
    getAllDesserts() {
      return this.$store.state.inventory.desserts;
    },
  },
};
</script>

<style>
.dessert-view-container {
  display: flex;
  flex-direction: column;
  column-gap: 30px;
  align-items: start;
  padding: 40px 40px;
}

.dessert-view-components {
  width: 75%;
}

.dessert-header {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 20px;
}

@media screen and (max-width: 680px) {
  .dessert-view-components {
    width: 100%;
  }

  .dessert-view-container {
    margin-bottom: 0px;
  }

  
}

@media screen and (max-width: 1080px) and (min-width: 681px) {
  .dessert-view-components {
    width: 95%;
  }
}
</style>
