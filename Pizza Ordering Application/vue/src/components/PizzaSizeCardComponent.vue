<template>
  <div class="pizza-size-card-container" :id="'size-' + size.productId" v-on:click="$emit('add-size')">
    <div class="pizza-size-card"></div>
    <div class="pizza-size-card-details">
      <div class="pizza-size-card-text-container">
        <h2 class="pizza-size-card-text">{{ size.description }}</h2>
        <p class="pizza-size-card-description">
          ${{ size.price }}
        </p>
      </div>

    </div>
  </div>
</template>

<script>
export default {
    name: 'PizzaSizeCardComponent',

    props: {
        size: {
            type: Object,
            required: true
        },

        
    },

}
</script>

<style scoped>


.pizza-size-card-container{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 20px;
    border-radius: 10px;
    padding: 10px;
    text-align: center;
    cursor: pointer;
    border: 1px solid rgb(225, 225, 225);
    box-shadow: rgba(0, 0, 0, 0.14) 0px 3px 6px;
}

.card-highlighted {
  border: 2px solid #e61d25;
}

input[type="checkbox"] {
  width: 25px;
  height: 25px;
}

input[type="checkbox"]:checked {
  accent-color: #e61d25;
}
</style>