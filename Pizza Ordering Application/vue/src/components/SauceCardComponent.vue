<template>
    <div class="topping-card-container">
      <div class="topping-card-text-container">
        <div class="sauce-text-content">
          <p class="sauce-card-header">
          {{ sauceHeader }} &nbsp;
          </p>
          <p class="sauce-card-description">
            {{ sauceDescription }}
          </p>
        </div>
        
        <input type="checkbox" class="sauce-checkbox" :id="'sauce-' + sauce.productId" :value="1" v-on:click="$emit('add-sauce')" :disabled="sauce.quantity == 0"/>
      </div>
  </div>
</template>

<script>
export default {
  name: 'SauceCardComponent',
  props: { 
    sauce: {
      type: Object,
      required: true
 
  }
},
computed: {
  sauceHeader() {
   const arr = this.sauce.description.split('-')
    return arr[0];
  },
  sauceDescription() {
   const arr = this.sauce.description.split('-')
    return arr[arr.length - 1];
  }
}
}
</script>

<style scoped>

.topping-card-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
  border: 1px solid rgb(225, 225, 225);
  box-shadow: rgba(0, 0, 0, 0.14) 0px 3px 6px;
  border-radius: 10px;
  padding: 10px;
  text-align: center;
  margin-top: 20px;
}

.topping-card-text-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

input[type="checkbox"] {
  width: 25px;
  height: 25px;
  cursor: pointer;
}

input[type="checkbox"]:checked {
  accent-color: #e61d25;
}

.sauce-card-header {
  font-weight: bold;
}

</style>