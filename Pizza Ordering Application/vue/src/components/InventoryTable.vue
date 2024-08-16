<template>
    <div class="toppings-table-container-inventory">
      <div class="table-wrapper">
        <table v-if="toppings.length > 0">
          <thead>
            <tr class="table-header">
              <th class="category-header">Category</th>
              <th class="description-header">Description</th>
              <th class="price-header">Price</th>
              <th class="quantity-header">Quantity</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(topping, index) in toppings" :key="index" @click.prevent="changeCurrItem(topping)">
              <td>{{ topping?.product_category_description }}</td>
              <td>{{ topping?.description }}</td>
              <td>${{ topping?.price.toFixed(2) }}</td>
              <td>{{ topping?.quantity }}</td>
            </tr>
            <tr v-for="(drink, index) in drinks" :key="index" @click.prevent="changeCurrItem(drink)">
              <td>{{ drink?.product_category_description }}</td>
              <td>{{ drink?.description }}</td>
              <td>${{ drink?.price.toFixed(2) }}</td>
              <td>{{ drink?.quantity }}</td>
            </tr>
            <tr v-for="(dessert, index) in desserts" :key="index" @click.prevent="changeCurrItem(dessert)">
              <td>{{ dessert?.product_category_description }}</td>
              <td>{{ dessert?.description }}</td>
              <td>${{ dessert?.price.toFixed(2) }}</td>
              <td>{{ dessert?.quantity }}</td>
            </tr>
            <tr v-for="(appetizer, index) in appetizers" :key="index" @click.prevent="changeCurrItem(appetizer)">
              <td>{{ appetizer?.product_category_description }}</td>
              <td>{{ appetizer?.description }}</td>
              <td>${{ appetizer?.price.toFixed(2) }}</td>
              <td>{{ appetizer?.quantity }}</td>
            </tr>
            <tr v-for="(salad, index) in salads" :key="index" @click.prevent="changeCurrItem(salad)">
              <td>{{ salad?.product_category_description }}</td>
              <td>{{ salad?.description }}</td>
              <td>${{ salad?.price.toFixed(2) }}</td>
              <td>{{ salad?.quantity }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="inventory-change-pop-up" v-if="isUpdatingInventory">
        <div class="inventory-change-container">
            <p>Category: {{ currItem?.product_category_description }}</p>
            <p>Price: ${{ currItem?.price.toFixed(2) }}</p>
            <p>Description: {{ currItem?.description }}</p>
            <form>
                <label>Quantity:</label>
                <input type="number" :value="currItem.quantity" @input="event => currItem.quantity = event.target.value" />
            </form>
            <div class="inventory-change-btns">
              <button @click.prevent="updateInventoryItem">Save Changes</button>
              <button @click.prevent="isUpdatingInventory = false">Cancel</button>
            </div>
            
        </div>
    </div>
  </template>
  
  <script>
  import productService from '../services/ProductService';
  export default {
    data() {
      return {
        toppings: [],
        drinks: [],
        desserts: [],
        appetizers: [],
        salads: [],
        currItem: {},
        isUpdatingInventory: false
      };
    },
    methods: {
      allToppings() {
        productService.getAllToppings().then((data) => {
          const getToppings = [
            ...data.data['Regular Topping'],
            ...data.data['Premium Topping']
          ];
          this.toppings = getToppings;
        });
      },
      allDrinks() {
        productService.getAllDrinks().then((data) => {
          const getDrinks = [
            ...data.data['Drink'],
          ];
          this.drinks = getDrinks;
        });
      },
      allDesserts() {
        productService.getAllDesserts().then((data) => {
          const getDesserts = [
            ...data.data['Dessert'],
          ];
          this.desserts = getDesserts;
        });
      },
      allAppetizers() {
        productService.getAllAppetizers().then((data) => {
          const getAppetizers = [
            ...data.data['Appetizer'],
          ];
          this.appetizers = getAppetizers;
        });
      },
      allSalads() {
        productService.getAllSalads().then((data) => {
          const getSalads = [
            ...data.data['Salad'],
          ];
          this.salads = getSalads;
        });
      },
      changeCurrItem(item) {
        console.log(item)
        this.currItem = {
            product_id: item.productId,
            product_category_description: item.product_category_description,
            price: parseFloat(parseFloat(item.price).toFixed(2)),
            description: item.description,
            quantity: item.quantity
        }
        this.isUpdatingInventory = true
      },
      updateInventoryItem() {
        this.currItem.quantity = parseInt(this.currItem.quantity)
        console.log(this.currItem)
        try {
                productService.updateProduct(this.currItem).then((data) => {
                    this.currItem = {};
                    this.isUpdatingInventory = false;
                    window.location.reload();
                })
            }catch(err) {
                console.log(err)
            }
      }
    },
    mounted() {
      this.allToppings();
      this.allDrinks();
      this.allDesserts();
      this.allAppetizers();
      this.allSalads();
    }
  };
  </script>
  
  <style>
  .toppings-table-container-inventory {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: start;
    height: auto;
  }
  
  .toppings-table-container-inventory h2 {
    text-transform: uppercase;
    font-size: 2em;
    font-weight: bold;
  }
  
  .table-wrapper {
    max-height: 400px; 
    overflow-y: auto;
    width: 100%;
    text-align: center;
  }

  .toppings-table-container-inventory { 
    height: 100%;
  }
  
  .toppings-table-container-inventory table {
    width: 100%;
    border-collapse: collapse;
    border: 2px solid #ddd; 
    height: 100%;
  }
  
  .toppings-table-container-inventory thead th {
    position: sticky;
    top: 0;
    background-color: #f8f8f8; 
    z-index: 1; 
    padding: 12px;
    border-bottom: 2px solid #e61d25; 
    text-transform: uppercase;
  }
  
  .toppings-table-container-inventory tbody tr {
    cursor: pointer;
    border-bottom: 1px solid #ddd; 
  }
  
  .toppings-table-container-inventory tbody tr:hover {
    background-color: #f0f0f0; 
  }
  
  .toppings-table-container-inventory td {
    padding: 12px;
  }
  
  .category-header, .description-header, .price-header, .quantity-header {
    width: 25%;
  }

  .inventory-change-pop-up {
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

  .inventory-change-container {
    background-color: #fff;
    width: 40%;
    padding: 50px 20px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  .inventory-change-container label {
    margin-right: 20px;
  }

  .inventory-change-container button {
    margin-top: 30px
  }

  .inventory-change-btns {
    display: flex;
    justify-content: start;
    gap: 10px;
    min-width: 80%;
  }

  .inventory-change-btns button:nth-child(1) {
    background-color: #000;
  }
  .inventory-change-btns button:nth-child(1):hover {
    background-color: #252525;
  }

  .inventory-change-btns button:nth-child(2):hover {
    background-color: #a52226;
  }

  .inventory-change-btns button {
    color: #fff;
    background-color: #e61d25;
    padding: 5px 10px;
    border: none;
    cursor: pointer;
  }

  @media screen and (max-width: 820px) {
    .inventory-change-container {
      width: auto;
    }
  }
  </style>
  