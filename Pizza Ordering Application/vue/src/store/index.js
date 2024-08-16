import { createStore as _createStore } from 'vuex';
import axios from 'axios';

export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      inventory: {
        specialtyPizza: [
          
        ],
        size: [],

        toppings: [],

        premiumToppings: [],

        crust: [],

        sauce : [],

        drinks: [],

        desserts: [],

        sides: [],

        salads: []

        
      },
      showCart: false,
      // What is currently in the cart component - front end
      currentOrder: [
        
      ],

      
      isDelivery: {
        orderType: '',
        address: "123 Main Street",
        eta: "15-20 minutes",
        zipcode: null
      },
      
      // What is sent to backend
      cart: {
          pizza: [ 

          ],
          other: [

          ],

        },
        

        allInvoices: [

        ],

        storeAddress: {
                      street_address: "5867 Mayfield Road",
                      city: "Mayfield Heights",
                      state_abbreviation:"OH",
                      zip_code:"44124"
                  }
    },
    
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      },
      SET_USER(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      LOGOUT(state) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('sides');
        localStorage.removeItem('salads');
        localStorage.removeItem('dessert');
        localStorage.removeItem('drink');
        localStorage.removeItem('pizza');
        localStorage.removeItem('specialtypizza');
        localStorage.removeItem('firstName')
        localStorage.removeItem('lastName')
        localStorage.removeItem('phoneNumber')
        localStorage.removeItem('email')
        localStorage.removeItem('street')
        localStorage.removeItem('city')
        localStorage.removeItem('state')
        localStorage.removeItem('formZip')
        state.currentOrder = [];
        state.cart.other = [];
        state.cart.pizza = [];
        state.token = '';
        state.user = {};
        state.allInvoices = [];
        console.log(state.allInvoices)
        console.log(localStorage.removeItem('token'))
        console.log(state.token)
        axios.defaults.headers.common = {};
      },
      ADD_TO_CURR_ORDER(state, item) {
        state.currentOrder.push(item)
      },
      ADD_TO_PIZZA_CART(state, pizza) {
        state.cart.pizza.push(pizza)
        console.log(state.cart.pizza)
      },
      ADD_TO_OTHER_CART(state, item) {
        state.cart.other.push(item)
      },
      ADD_REGULAR_TOPPING(state, topping) {
        state.inventory.toppings.push(topping)
      },
      ADD_SPECIALTY_PIZZA(state, pizza) {
        state.inventory.specialtyPizza.push(pizza)
      },
      ADD_PREMIUM_TOPPING(state, topping) {
        state.inventory.premiumToppings.push(topping)
      },
      ADD_CRUST(state, crust) {
        state.inventory.crust.push(crust)
      },
      ADD_SAUCE(state, sauce) {
        state.inventory.sauce.push(sauce)
      },
      ADD_SIZE(state, size) {
        state.inventory.size.push(size)
      },
      ADD_SALAD(state, salad) {
        state.inventory.salads.push(salad)
      },
      ADD_DESSERT(state, dessert) {
        state.inventory.desserts.push(dessert)
      },
      ADD_APPETIZER(state, side) {
        state.inventory.sides.push(side)
      },
      ADD_DRINK(state, drink) {
        state.inventory.drinks.push(drink)
      },
      UPDATE_CREDIT_CARD(state, number) {
        state.invoice.credit_card = number
      },
      UPDATE_INVOICE_ADDRESS(state, address) {
        state.invoice.address = address
      },
      UPDATE_ORDER_TYPE(state, orderType) {
        state.isDelivery.orderType = orderType
        
       
      },

      UPDATE_ORDER(state, newInvoice, isComplete) {
        for(let invoice of state.allInvoices) {
          if(invoice?.invoice?.invoice_id == newInvoice?.invoice?.invoice_id) {
            invoice.invoice.status = isComplete;
          }
        }
      },

      ADD_ZIPCODE(state, zip) {
        state.isDelivery.zipcode = zip
        localStorage.setItem('zipcode', zip)
      },

      ADD_INVOICE(state, invoice) {
        state.allInvoices.push(invoice)
      },
      EMPTY_OTHER_CART(state) {
        state.cart = {
          pizza: [],
          other: []
        }
      },
      EMPTY_CART(state) {
        state.cart = {
          pizza: [],
          other: []
        }
        state.currentOrder = [];
        localStorage.removeItem('pizza')
        localStorage.removeItem('drink')
        localStorage.removeItem('sides')
        localStorage.removeItem('salads')
        localStorage.removeItem('dessert')
        localStorage.removeItem('specialtypizza')

      },
      TOGGLE_CART(state, value) {
        state.showCart = value
      },
      REMOVE_FROM_CURRENT_ORDER(state, productId) {
       const updatedCart = state.currentOrder.filter((item) => item.productId !== productId)
       state.currentOrder = updatedCart
      },
      REMOVE_FROM_CART(state, item) {
        const isPizza = state.cart.pizza.find((pizza) => pizza[0].id == item.id);
        const isOther = state.cart.other.find((otherItem) => otherItem == item.productId);
        console.log(isOther)
        if(isPizza !== undefined) {
          state.cart.pizza = state.cart.pizza.filter((pizza) => pizza[0].id !== item.id)
        } else if (isOther !== undefined) {
          const updatedCart = state.cart.other.filter((otherItem) => item.productId !== otherItem)
          state.cart.other = updatedCart
          console.log(state.cart)
        }
        
        

       }
    },
  });
  return store;
}
