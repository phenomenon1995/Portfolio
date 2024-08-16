<template>
  <div class="pizza-card-container">
    <div class="pizza-card" :style="pizzaStyle"></div>
    <div class="featured-pizza-card-details">
      <div class="featured-pizza-card-text-container">
        <h2 class="featured-pizza-card-text">{{ pizzaName }}</h2>
        <p class="featured-pizza-card-description">
          {{ pizzaDescription }}
        </p>
      </div>

      <img
        class="arrow-image"
        src="../assets/arrow.png"
        alt="Pizza"
        style="width: 8%"
        @click="this.$router.replace('/pizza')"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: "FeaturedPizzaCard",
  data() {
    return {
      pizzaName: this.pizza?.description.split("-")[0],
      pizzaDescription: this.pizza?.description.split("-")[1]
    }
  },
  props: {
    pizza: Object,
    index: Number
  },
  methods: {},
  computed: {
    pizzaStyle() {
        return {
            // Images must be placed inside of public folder
            backgroundImage: `url('./featured-pizza-${this.index}.jpg')`
        };
    }
}
  // Your component's JavaScript logic goes here
};
</script>

<style scoped>
.pizza-card {
  position: relative;
  height: 200px;
  background-size: cover;
  background-position: center;
  border-radius: 9px 9px 0px 0px;
  background-image: url('../assets/Hawian.jpg');
  padding: 0px 30px;
  height: 300px;
}

.featured-pizza-card-details {
  display: flex;
  align-items: center;
  justify-content: space-between;
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
</style>
