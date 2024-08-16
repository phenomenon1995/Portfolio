<template>
    <div class="topping-card-container">
      
      <div class="crust-card-text-container">
        <p class="topping-card-description">
          {{ crust.description}}
        </p>
        <input type="checkbox" class="crust-checkbox" :id="'crust-' + crust.productId" :value="1" v-on:click="$emit('add-crust')" :disabled="crust.quantity == 0"/>
      </div>
  </div>
</template>

<script>
export default {
  name: 'CrustCardComponent',
  props: {
    crust: {
      type: Object,
      required: true
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

.crust-card-text-container {
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

</style>