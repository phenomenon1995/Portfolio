<template>
  <div class="drink-card-container" :id="'drink-' + drink.productId">
    <div class="drink-card" :style="drinkStyle"></div>
    <div class="drink-card-details">
      <div class="drink-card-text-container">
        <h2 class="drink-card-text">{{ drinkName }} Drink</h2>
        <p class="drink-card-description">
          ${{ drink.price.toFixed(2) }} 
        </p>
      </div>

      <button v-if="drink?.quantity > 0" class="add-to-cart-btn" v-on:click="$emit('show-popup')" >
          Add to Cart
      </button>
      <button v-if="drink?.quantity == 0" disabled class="out-of-stock-btn">
        OUT OF STOCK
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "DrinkCard",
  props: {
    drink: Object,
  },
  methods: {},
  computed: {
    drinkStyle() {
        return {
            // Images must be placed inside of public folder
            backgroundImage: `url('./${this.drinkName}.jpg')`
        };
    },

    drinkName() {
      const arr = this.drink.description.split("-")

      return arr[0].trim()
    }
}
  // Your component's JavaScript logic goes here
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Calistoga&family=PT+Serif:ital,wght@0,400;0,700;1,400;1,700&display=swap');

.drink-card-container {
  display: grid;
  grid-template-columns: 1fr 4fr;
  border-radius: 9px;
  border: 1px solid #e1e1e1;
  margin-top: 20px;
  box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
  width: 100%;
}

.drink-card {
  position: relative;
  height: 150px;
  background-size: cover;
  background-position: center;
  border-radius: 9px 0px 0px 9px;
  background-image: url('../assets/Hawian.jpg');
  padding: 0px 30px;
  min-width: 200px;
}

.drink-card-container .drink-card-details {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.drink-card-text-container {
  flex: 1;
  padding: 0 10px;
}

.drink-card-text {
  font-size: 1.8em;
  color: #333;
}

.drink-card-description {
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
  .drink-card-container {
    grid-template-columns: 1fr;
    padding: 0px;
    width: 100%;
  }

  .drink-card {
    border-radius: 9px 9px 0px 0px;
  }

  .drink-card-container .drink-card-details {
    flex-direction: column;
    align-items: flex-start;
    padding: 10px 20px;
  }

  .drink-card-text-container {
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
