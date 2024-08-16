<template>
  <div class="dessert-card-container" :id="'dessert-' + dessert.productId">
    <div class="dessert-card" :style="dessertStyle"></div>
    <div class="dessert-card-details">
      <div class="dessert-card-text-container">
        <h2 class="dessert-card-text">{{ dessertName }} dessert</h2>
        <p class="dessert-card-description">${{ dessert.price.toFixed(2) }}</p>
      </div>

      <button
        v-if="dessert?.quantity > 0"
        class="add-to-cart-btn"
        v-on:click="$emit('add-to-cart')"
      >
        Add to Cart
      </button>
      <button v-if="dessert?.quantity == 0" disabled class="out-of-stock-btn">
        OUT OF STOCK
      </button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  props: {
    dessert: Object,
    dessertName: String,
  },

  computed: {
    dessertStyle() {
      return {
        backgroundImage: `url('./${this.dessertName}.jpg')`,
      };
    },
  },
};
</script>
<style scoped>
.dessert-card-container {
  display: grid;
  grid-template-columns: 1fr 4fr;
  border-radius: 9px;
  border: 1px solid #e1e1e1;
  margin-top: 20px;
  box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
  width: 100%;
}

.dessert-card {
  position: relative;
  height: 150px;
  background-size: cover;
  background-position: center;
  border-radius: 9px 0px 0px 9px;
  background-image: url("../assets/Hawian.jpg");
  padding: 0px 30px;
  min-width: 200px;
}

.dessert-card-container .dessert-card-details {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.dessert-card-text-container {
  flex: 1;
  padding: 0 10px;
}

.dessert-card-text {
  font-size: 1.8em;
  color: #333;
}

.dessert-card-description {
  font-size: 1em;
  color: #333;
}

.add-to-cart-btn,
.out-of-stock-btn {
  background-color: #e61d25;
  color: #fff;
  border-radius: 15px;
  border: none;
  padding: 15px 30px;
  cursor: pointer;
  height: 50px;
  width: 150px;
  text-align: center;
}

.add-to-cart-btn:hover {
  background-color: #000;
  color: #fff;
}

.out-of-stock-btn {
  background-color: #ccc;
  cursor: not-allowed;
}

@media screen and (max-width: 680px) {
  .dessert-card-container {
    grid-template-columns: 1fr;
    padding: 0px;
    width: 100%;
  }
  .dessert-card {
    border-radius: 9px 9px 0px 0px;
  }
  .dessert-card-container .dessert-card-details {
    flex-direction: column;
    align-items: flex-start;
    padding: 10px 20px;
  }
  .dessert-card-text-container {
    width: 100%;
    padding-bottom: 10px;
  }
  .add-to-cart-btn,
  .out-of-stock-btn {
    width: 100%;
    padding: 10px;
    height: auto;
  }
}
</style>
