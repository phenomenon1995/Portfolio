<template>
    <div class="toppings-table-container">
        <div class="availability-btns">
            <button class="add-specialty-pizza" @click="toggleSpecialtyPizzaForm">Add Specialty Pizza</button>
            <div class="update-availability-btns">
                <button @click.prevent="updateProduct(20)" :class="{'disabled': updatingPizzas.length == 0}" :disabled="updatingPizzas.length == 0">Make Available</button>
                <button @click.prevent="updateProduct(0)" :class="{'disabled': updatingPizzas.length == 0}" :disabled="updatingPizzas.length == 0">Make Unavailable</button>
            </div>
        </div>
        
      <div class="table-wrapper">

        <table v-if="pizzas?.length > 0">
          <thead>
            <tr class="table-header">
              <th class="description-header">Description</th>
              <th class="price-header">Price</th>
              <th class="quantity-header">Quantity</th>
              <th class="availability-header">Availability</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(pizza, index) in pizzas" :key="index">
              <td>{{ pizza?.description }}</td>
              <td>${{ pizza?.price && pizza?.price }}</td>
              <td>{{ pizza?.quantity > 0 ? 'Available' : 'Unavailable'}}</td>
              <td><input v-on:change="addPizzaToUpdatingPizzas(pizza)" type="checkbox"></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="add-pizza-form" v-if="isAddingNewPizza == true">
        
        <form>
            
            <div class="form-close-btn">
                <button @click="toggleSpecialtyPizzaForm">Close</button>
            </div>
            <p v-if="errorMessage">Incorrect Price format. Pizza price must be $99.00 or less</p>
            <h3>Add Specialty Pizza</h3>
            <label>Pizza Name</label>
            <input type="text" :value="newPizzaName" @input="event => newPizzaName = event.target.value" />
            <label>Price</label>
            <input type="text" :value="newPizzaPrice" @input="event => newPizzaPrice = event.target.value" />
            <label>Description</label>
            <input type="text" :value="newPizzaDescription" @input="event => newPizzaDescription = event.target.value" />
            <button type="submit" 
            :class="{'disabled': newPizzaName.length == 0 || newPizzaPrice.length == 0 || newPizzaDescription.length == 0}" 
            :disabled="newPizzaName.length == 0 || newPizzaPrice.length == 0 || newPizzaDescription.length == 0"
            @click.prevent="submitNewPizza"
            >
            Add Pizza
            </button>
        </form>
    </div>
  </template>
  
  <script>
  import productService from '../services/ProductService';
  export default {
    data() {
      return {
        pizzas: [],
        updatingPizzas: [],
        newPizzaName: '',
        newPizzaPrice: '',
        newPizzaDescription: '',
        isAddingNewPizza: false,
        errorMessage: false
      };
    },
    methods: {
      allSpecialtyPizzas() {
        productService.getAllSpecialtyPizzas().then((data) => {
          const getPizzas = [
            ...data.data['Specialty Pizza'],
          ];
          this.pizzas = getPizzas;
          console.log(this.pizzas)
        });
      },
      updateProduct(quantity) {
        for (let pizza of this.updatingPizzas) {
          pizza.quantity = quantity;
          pizza.product_id = pizza.productId
          console.log(pizza)
          productService.updateProduct(pizza).then((data) => console.log(data));
        } 

        this.updatingPizzas = [];

        const allInputs = document.getElementsByTagName('input')
        for(let input of allInputs) {
            input.checked = false
        }
      },
      addPizzaToUpdatingPizzas(pizza) {
        const isChecked = this.updatingPizzas.find((item) => item.productId == pizza.productId);
        console.log(isChecked);
        if (isChecked == undefined) {
          this.updatingPizzas.push(pizza);
        } else {
          this.updatingPizzas = this.updatingPizzas.filter((item) => item.productId !== pizza.productId);
        }
        console.log(this.updatingPizzas);
      },
      toggleSpecialtyPizzaForm() {
        this.isAddingNewPizza = !this.isAddingNewPizza
      },
      changeNewPizzaName() {
        console.log(this.newPizzaName)
      },
      submitNewPizza() {
        const pattern = /^-?\d{1,2}(,\d{2})*(\.\d{2})?$/
        if(!pattern.test(this.newPizzaPrice)) {
            this.errorMessage = true
            setTimeout(() => {
                this.errorMessage = false
            }, 2500)
            return
        } else {
            let newPrice = parseFloat(this.newPizzaPrice).toFixed(2);
            let newPizza = {
            product_id : null,
            product_category_description : "Specialty Pizza",
            price : newPrice.toString(),
            description : this.newPizzaName + " - " + this.newPizzaDescription,
            quantity : "10"
            }
            try {
                productService.createProduct(newPizza).then((data) => {
                    this.pizzas.push(newPizza)
                    this.isAddingNewPizza = false
                })
            }catch(err) {
                console.log(err)
            }
            console.log(newPizza)
        }
      }
    },
    computed: {
        pizzaQuantity(pizza) {
            let currentQuantity = pizza.quantity > 0 ? 'Available' : 'Unavailable'
            return currentQuantity
        }
    },
    mounted() {
      this.allSpecialtyPizzas();
      
    }
  };
  </script>
  
  <style>
  .toppings-table-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  
  .toppings-table-container h2 {
    text-transform: uppercase;
    font-size: 2em;
    font-weight: bold;
  }
  
  .table-wrapper { 
    overflow-y: auto;
    width: 100%;
    text-align: center;
  }
  
  .toppings-table-container table {
    width: 100%;
    border-collapse: collapse;
    border: 2px solid #ddd; 
  }
  
  .toppings-table-container thead th {
    position: sticky;
    top: 0;
    background-color: #f8f8f8; 
    z-index: 1; 
    padding: 12px;
    border-bottom: 2px solid #e61d25; 
    text-transform: uppercase;
  }

  .add-pizza-form {
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: #00000063;
    z-index: 9999999999999999999;
  }

  .add-pizza-form form {
    background-color: #fff;
    width: 40%;
    padding: 50px 20px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  .add-pizza-form form input {
    width: 80%;
    margin-bottom: 15px;
  }

  .add-pizza-form h3 {
    margin-top:0px;
    margin-bottom: 15px;
    padding: 0px;
    font-size: 1.7em;
  }

  .add-pizza-form button.disabled {
    background-color: #c2c2c2
  }

  button.disabled {
    background-color: #c2c2c2
  }

  .update-availability-btns button:nth-child(1).disabled {
    background-color: #c2c2c2
  }

  button.disabled:hover, 
  .update-availability-btns button:nth-child(1).disabled:hover, 
  .add-pizza-form button.disabled:hover {
    background-color: #c2c2c2;
    cursor:default;
  }
  
  .toppings-table-container tbody tr {
    cursor: pointer;
    border-bottom: 1px solid #ddd; 
  }
  
  .toppings-table-container tbody tr:hover {
    background-color: #f0f0f0; 
  }
  
  .toppings-table-container td {
    padding: 12px;
  }
  
  .category-header, .description-header, .price-header, .quantity-header {
    width: 25%;
  }

  .availability-btns {
    display: flex;
    justify-content: space-between;
    gap: 15px;
    width: 100%;
  }

  .update-availability-btns {
    display: flex;
    width: 40%;
    gap: 15px;
  }
  .update-availability-btns button:nth-child(1) {
    background-color: rgb(0, 0, 169);
  }

  .update-availability-btns button:nth-child(1):hover {
    background-color: rgb(0, 0, 111);
  }

  button.add-specialty-pizza {
    border-radius: 5px;
    background-color: rgb(0, 150, 0);
  }

  button.add-specialty-pizza:hover {
    background-color: rgb(0, 105, 0);
  }

  .form-close-btn {
    display: flex;
    justify-content: end;
    width: 100%;
  }

  .form-close-btn button {
    background-color: #000;
    color: #fff;
    padding: 10px 25px;
    border: none;
    border-radius: 3px;
  }

  .form-close-btn button:hover {
    background-color: #232323;
    cursor: pointer;
  }

  @media screen and (max-width: 820px) {
    .availability-btns {
      flex-direction: column;
      align-items: center;
    }

    .update-availability-btns {
      width: 100%;
      justify-content: center;
    }
  }
  </style>
  