<template>
  <div
    class="drink-size-pop-up-container"
    v-bind:class="{ 'popup-hidden': !showPopUp }"
  >
    <div class="drink-size-option-container">
      <h2>Pick your drink size</h2>
      <select>
        <option>{{ size.small }}</option>
        <option>{{ size.medium }}</option>
        <option>{{ size.large }}</option>
      </select>

      <div class="drink-size-btns">
        <button class="add-drink-size" v-on:click="$emit('add-to-order')">Add to Cart</button>
        <button v-on:click="$emit('hide-popup')" class="cancel-drink-size">Cancel</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      size: {
        small: "Small",
        medium: "Medium",
        large: "Large",
      },
    };
  },
  props: {
    drink: Object,
    showPopUp: Boolean,
  },
};
</script>

<style scoped>
.drink-size-pop-up-container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.58);
  height: 100vh;
  width: 100vw;
  z-index: 9999;
}

.popup-hidden {
  visibility: hidden;
}

.drink-size-option-container {
  background-color: #fff;
  width: 80%; /* Adjust width for responsiveness */
  max-width: 400px; /* Set a max width for larger screens */
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2); /* Add some shadow for better visibility */
}

h2 {
  font-size: 1.5em; /* Responsive font size */
  margin-bottom: 15px;
}

select {
  width: 100%; /* Make the select element responsive */
  padding: 5px;
  font-size: 1em;
  margin-bottom: 20px; /* Add margin for spacing */
}

.drink-size-btns {
  width: 100%; /* Make button container responsive */
  display: flex;
  justify-content: space-around; /* Use space-around for spacing */
  gap: 10px; /* Add some gap for spacing */
}

.drink-size-btns button {
  padding: 10px 20px; /* Adjust padding for smaller screens */
  font-size: 1em; /* Responsive font size */
  cursor: pointer;
}

.add-drink-size {
  color: white;
  background-color: #e61d25;
  border: none; /* Remove border for better aesthetics */
}

.add-drink-size:hover {
  background-color: #000;
}

.cancel-drink-size {
  color: #e61d25;
  border: 1px solid #000; /* Add border for consistency */
  background-color: #fff;
}

.cancel-drink-size:hover {
  background-color: #000;
  color: #fff;
}

/* Media Queries for further responsiveness */
@media (max-width: 600px) {
  h2 {
    font-size: 1.2em; /* Smaller font size on mobile */
  }
  
  .drink-size-btns {
    flex-direction: column; /* Stack buttons vertically on mobile */
  }

  .drink-size-btns button {
    width: 100%; /* Full width buttons on mobile */
    margin-bottom: 10px; /* Spacing between buttons */
  }
}
</style>
