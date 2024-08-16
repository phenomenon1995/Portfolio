<template>
  <div id="capstone-app">
    <!-- Header -->
    <header id="nav">
      <HeaderComponent />
    </header>
    <!-- Body -->
    <main class="body-container">
      <router-view />
    </main>
    
    <!-- Footer -->
    
      <FooterComponent />
    
  </div>
</template>

<script>
import HeaderComponent from "./components/HeaderComponent.vue";
import FooterComponent from "./components/FooterComponent.vue";
import productService from "./services/ProductService";
import invoiceService from "./services/InvoiceService";

export default {
  components: {
    HeaderComponent,
    FooterComponent,
  },
  methods: {
    addProducts() {
      
      productService.getProducts().then((data) => {
        const categories = {
          'Regular Topping': 'ADD_REGULAR_TOPPING',
          'Premium Topping': 'ADD_PREMIUM_TOPPING',
          'Size': 'ADD_SIZE',
          'Crust': 'ADD_CRUST',
          'Sauce': 'ADD_SAUCE',
          'Drink': 'ADD_DRINK',
          'Dessert': 'ADD_DESSERT',
          'Appetizer': 'ADD_APPETIZER',
          'Salad': 'ADD_SALAD',
          'Specialty Pizza': 'ADD_SPECIALTY_PIZZA'
        };

        for (const [category, mutation] of Object.entries(categories)) {
          const items = data.data[category];
          if (items) {
            items.forEach(item => {
              this.$store.commit(mutation, item);
            });
          }
        }

        
        this.loadStoredItems('pizza', 'ADD_TO_CURR_ORDER');
        this.loadStoredItems('drink', 'ADD_TO_CURR_ORDER');
        this.loadStoredItems('sides', 'ADD_TO_CURR_ORDER');
        this.loadStoredItems('dessert', 'ADD_TO_CURR_ORDER');
        this.loadStoredItems('salads', 'ADD_TO_CURR_ORDER');
        this.loadStoredItems('specialtypizza', 'ADD_TO_CURR_ORDER');
        

        
        this.loadStoredData('zipcode', 'ADD_ZIPCODE', true);
        this.loadStoredData('orderType', 'UPDATE_ORDER_TYPE', false);
      });
    },
    
    loadStoredItems(storageKey, mutation) {
      let storedItems = localStorage.getItem(storageKey);
      
      if (storedItems && storedItems !== 'undefined') {
        storedItems = JSON.parse(storedItems);
        if (Array.isArray(storedItems) && storedItems.length > 0) {
          storedItems.forEach(item => {
            this.$store.commit(mutation, item);
          });
        }
      }
    },
    
    loadStoredData(storageKey, mutation, isNumeric) {
      let storedData = localStorage.getItem(storageKey);
      if (storedData && storedData !== 'undefined') {
        this.$store.commit(mutation, isNumeric ? parseInt(storedData) : storedData);
      }
    }
  },
  
  mounted() {
    if (this.$store.state.inventory.crust.length === 0) {
      this.addProducts();
    }
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Nunito:ital,wght@0,200..1000;1,200..1000&display=swap");
@import url("https://fonts.googleapis.com/css2?family=PT+Serif:ital,wght@0,400;0,700;1,400;1,700&display=swap");
* {
  font-family: "Nunito", sans-serif;
}



body {
  padding: 0px;
  margin: 0px;
  overflow-x: hidden;
}

.body-container {
  margin-top: 9vh;
  min-height: 100vh;
}

a,
.router-link-active,
.router-link-active:visited,
.router-link-active:active {
  text-decoration: none;
  color: black;
}

.header-nav-links {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 5em;
  left: 14em;
  list-style: none;
  background-color: white;
  padding: 0px 10px;
  border: #e6e6e6 solid 1px;
  z-index: 5;
  visibility: hidden;
  transition: opacity 0.8s ease, transform 0.3s ease;
  transform: translateY(-100px);
}
.show {
  visibility: visible;
}

.header-nav-links.show {
  opacity: 1;
  transform: translateY(0);
}

.header-nav-links .router-link-active {
  border-bottom: #a9a9a9 solid 1px;
  padding: 10px 8px 10px 8px;
  width: 100%;
}

.header-nav-links .router-link-active:nth-child(3) {
  border-bottom: none;
}

.header-nav-links li:hover,
.header-nav-links .router-link-active:hover {
  background-color: #e61d25;
  color: #fff;
}





@media screen and (max-width: 680px) and (min-width: 481px) {
  .body-container {
    margin-bottom: 1200px;
  }
}
</style>
