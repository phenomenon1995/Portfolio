<template>
    <div class="salad-view-container">
      <div class="salad-view-components">
        <h2 class="salad-header">Salads</h2>
        <!-- All salad Components -->
        <div
          v-for="(salad, index) in getAllSalads"
          v-bind:key="{ index }"
          class="salad-view-components"
        >
          <SaladComponent
            v-bind:salad="salad"
            :saladName="salad.description.split('-')[0].trim()"
            @add-to-cart="addToCart(salad.productId)"
          />
        </div>
      </div>
    </div>
  
    <Toast :message="toastMessage" v-if="showToast" />
  </template>
  
  <script>
  import SaladComponent from "../components/SaladComponent.vue";
  import Toast from "../components/Toast.vue";
  
  export default {
    data() {
      return {
        toastMessage: "",
        showToast: false,
        zipcode: this.$store.state.zipcode,
        allSalads: this.$store.state.inventory.salads,
        currSalad: {},
      };
    },
    methods: {
      addToCart(id) {
        console.log(this.zipcode);
        if (this.zipcode === null) {
          this.toastMessage =
            "Please add a zipcode, and choose carryout or delivery";
  
          this.showToast = true;
  
          this.$store.commit("TOGGLE_CART", true);
          setTimeout(() => {
            this.showToast = false;
          }, 2500);
        } else {
          // Add to current order in store
          this.currSalad = this.allSalads.find((salad) => salad.productId === id);
          this.$store.commit("ADD_TO_CURR_ORDER", this.currSalad);
  
          let storedSalad = localStorage.getItem("salads");
  
          // Add to local storage
          if (storedSalad) {
            storedSalad = JSON.parse(storedSalad);
            storedSalad.push(this.currSalad);
            localStorage.setItem("salads", JSON.stringify(storedSalad));
          } else {
            let salad = [];
            salad.push(this.currSalad);
            localStorage.setItem("salads", JSON.stringify(salad));
          }
  
          // Add to other cart
          this.$store.commit("ADD_TO_OTHER_CART", this.currSalad.productId);
          console.log(this.$store.state.cart);
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
      SaladComponent,
      Toast,
    },
    computed: {
      getAllSalads() {
        return this.$store.state.inventory.salads;
      },
    },
  };
  </script>
  
  <style>
  .salad-view-container {
    display: flex;
    flex-direction: column;
    column-gap: 30px;
    align-items: start;
    padding: 40px 40px;
  }
  
  .salad-view-components {
    width: 75%;
  }
  
  .salad-header {
    font-size: 2rem;
    font-weight: 700;
    margin-bottom: 20px;
  }

  @media screen and (max-width: 680px) {
  .salad-view-components {
    width: 100%;
  }

  .salad-view-container {
    margin-bottom: 0px;
  }

  
}

@media screen and (max-width: 1080px) and (min-width: 681px) {
  .salad-view-components {
    width: 95%;
  }
}
  </style>
  