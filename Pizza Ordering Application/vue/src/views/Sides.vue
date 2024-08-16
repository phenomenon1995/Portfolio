<template>
  <div class="side-view-container">
    <div class="side-view-components">
      <h2 class="side-header">Appetizers</h2>
      <!-- All side Components -->
      <div
        v-for="(side, index) in getAllSides"
        v-bind:key="{ index }"
        class="side-view-components"
      >
        <SideComponent
          v-bind:side="side"
          :sideName="side.description.split('-')[0].trim()"
          @add-to-cart="addToCart(side.productId)"
        />
      </div>
    </div>
  </div>

  <Toast :message="toastMessage" v-if="showToast" />
</template>

<script>
import SideComponent from "../components/SideComponent.vue";
import Toast from "../components/Toast.vue";

export default {
  data() {
    return {
      toastMessage: "",
      showToast: false,
      zipCode: this.$store.state.zipcode,
      allSides: this.$store.state.inventory.sides,
      currSide: {},
    };
  },
  methods: {
    addToCart(id) {
      console.log(this.$store.state.isDelivery.zipcode);
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
        this.currSide = this.allSides.find((side) => side.productId === id);
        this.$store.commit("ADD_TO_CURR_ORDER", this.currSide);

        let storedSide = localStorage.getItem("sides");

        // Add to local storage
        if (storedSide) {
          console.log(storedSide)
          storedSide = JSON.parse(storedSide);
          console.log(storedSide)
          storedSide.push(this.currSide);
          console.log(storedSide)
          localStorage.setItem("sides", JSON.stringify(storedSide));
          const testing = localStorage.getItem('sides')
          console.log(testing)
        } else {
          let side = [];
          side.push(this.currSide);
          localStorage.setItem("sides", JSON.stringify(side));
        }

        // Add to other cart
        this.$store.commit("ADD_TO_OTHER_CART", this.currSide.productId);
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
    SideComponent,
    Toast,
  },
  computed: {
    getAllSides() {
      return this.$store.state.inventory.sides;
    },
  },
};
</script>

<style>
.side-view-container {
  display: flex;
  flex-direction: column;
  column-gap: 30px;
  align-items: start;
  padding: 40px 40px;
}

.side-view-components {
  width: 75%;
}

.side-header {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 20px;
}

@media screen and (max-width: 680px) {
  .side-view-components {
    width: 100%;
  }

  .side-view-container {
    margin-bottom: 0px;
  }

  
}

@media screen and (max-width: 1080px) and (min-width: 681px) {
  .side-view-components {
    width: 95%;
  }
}
</style>
