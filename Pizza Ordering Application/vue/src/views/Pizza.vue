<template>
  <div class="pizza-view-container">
    <div class="pizza-view-components">
        <h2>Make your own pizza</h2>
        <PizzaComponent v-bind:pizza="customPizza" :isCustom="true"/>
    </div>
    
    <h2>Specialty Pizzas</h2>
    <div v-for="(pizza, index) in allPizzas" v-bind:key="{ index }" class="pizza-view-components">
      <PizzaComponent v-bind:pizza="pizza" :isCustom="false"/>
    </div>
  </div>
</template>

<script>
import PizzaComponent from '../components/PizzaComponent.vue';

export default {
    data() {
        return {
            customPizza: {
            description: "Custom Pizza- Make your own delicious pizza",
            price: "32.00",
            crust: "regular",
            sauce: "Alfredo",
            quantity: 100
            },
            allPizzas: this.$store.state.inventory.specialtyPizza,
        }
    },
    components: {
        PizzaComponent
    }
};
</script>

<style>
    .pizza-view-container {
        display: flex;
        flex-direction: column;
        column-gap: 30px;
        align-items: start;
        padding: 40px 40px;
    }

    .pizza-view-components {
        width: 75%;
    }

    @media screen and (max-width: 680px) {
        .pizza-view-container {
            margin-top: 100px;
        }
        .pizza-view-components {
            width: 100%;
            align-items: center;
        }
    }
    @media screen and (max-width: 1080px) and (min-width: 681px) {
  .pizza-view-components {
    width: 95%;
  }
}
</style>
