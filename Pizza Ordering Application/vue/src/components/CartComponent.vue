<template>
  <div class="cart-new-overlay">
    <div class="cart-section-container">
      <div class="cart-btns">
        <button class="cart-close-btn" @click="$emit('close-cart')">
          Close
        </button>
      </div>
      <h1 class="cart-header">Your Cart</h1>
      <!-- User zipcode -->
      <div class="cart-zipcode">
        <div v-if="zipcode == null">
          <form class="request-cart-zipcode">
            <label for="zipcode">Please enter your zip code</label>
            <input
              id="zipcode"
              type="text"
              pattern="[0-9]{5}"
              placeholder="zipcode"
              required
            />
            <button class="submit-zipcode" @click="setZipCode">Save</button>
          </form>
        </div>
        <div class="show-cart-zipcode" v-if="zipcode != null">
          <p>Zip code: {{ zipcode }}</p>
          <button class="submit-zipcode" @click="setZipCodeToNull">
            Change
          </button>
        </div>
      </div>

      <!-- Carryout or delivery -->
      <div class="carryout-delivery" v-if="zipcode !== null">
        <h2>Carryout or Delivery</h2>

        <div class="order-type-container" v-if="orderType.length > 0">
          <div class="order-type-change-header">
            <p class="order-type-text">
              <strong>{{ orderType }}</strong>
            </p>
            <p class="change-order" @click="setOrderToNull">Change</p>
          </div>

          <p class="eta">{{ eta }}</p>
          <p>Store location:</p>
          <p class="address">{{ address }}</p>
        </div>

        <select
          class="cart-select"
          v-model="selectedOption"
          @change="updateOrder"
          v-if="orderType.length === 0"
        >
          <option value=""></option>
          <option value="carryout">Carryout</option>
          <option value="delivery">Delivery</option>
        </select>
      </div>

      <!-- Items -->
      <div v-if="orderType !== ''" class="order-container">
        <h2>Your order:</h2>
        <h4>Total: ${{ finalPrice }}</h4>
        <div v-if="allItems.length == 0">
          <p>Your cart is empty</p>
        </div>

        <!-- list all items -->
        <div
          class="order-header"
          v-for="(item, index) in allItems"
          :key="index"
        >
          <div class="item-heading">
            <h3>{{ item?.description }}</h3>
            <p>Price: ${{ parseInt(item?.price).toFixed(2) }}</p>
          </div>
          <h4 v-if="item?.sauce">
            Sauce: {{ item?.sauce.description.split("-")[0] }}
          </h4>
          <h4 v-if="item?.crust">Crust: {{ item?.crust.description }}</h4>
          <div v-if="item?.topping && item?.topping.length > 0">
            <h4>Toppings:</h4>
            <p v-for="(topping, index) in item.topping" :key="index">
              {{ topping?.description }}
            </p>
          </div>
          <div class="remove-from-cart-btn">
            <button @click="removeFromCart(item)">Remove from cart</button>
          </div>
        </div>
      </div>

      <!-- Payment info -->
      <div
        class="payment-container"
        v-if="orderType !== '' && allItems.length > 0"
      >
        <h2>Payment</h2>

        <form>
          <p>Please enter your credit card information:</p>
          <input
            required
            type="text"
            placeholder="First name"
            name="firstname"
            v-model="firstName"
            @change="updateUserFirstName()"
          />
          <input
            required
            type="text"
            placeholder="Last name"
            name="lastname"
            v-model="lastName"
            @change="updateUserLastName()"
          />
          <input
            required
            type="email"
            placeholder="Email"
            name="email"
            v-model="email"
            @change="updateUserEmail()"
          />
          <input
            required
            type="tel"
            placeholder="Phone number"
            name="phone"
            v-model="phoneNumber"
            @change="updateUserPhoneNumber()"
          />
          <input
            required
            type="text"
            placeholder="Credit card number"
            name="cardnumber"
            v-model="paymentmethod"
            @change="console.log(paymentmethod)"
          />
          <div class="deliveryInformation" v-if="orderType == 'delivery'">
            <input
              required
              type="text"
              placeholder="Street name"
              name="street"
              v-model="street"
              @change="updateUserStreet()"
            />
            <input
              required
              type="text"
              placeholder="City name"
              name="city"
              v-model="city"
              @change="updateUserCity()"
            />
            <input
              required
              type="text"
              placeholder="State Abbreviation"
              name="state"
              v-model="state"
              @change="updateUserState()"
            />
            <input
              required
              type="text"
              placeholder="Zip code"
              name="zipcode"
              v-model="formZip"
              @change="updateUserFormZip()"
            />
          </div>
          <button type="submit" @click.prevent="submitOrder">
            <p v-if="!loading">Checkout</p>
            <div class="loader" v-if="loading"></div>
          </button>
        </form>
      </div>
    </div>
    <Toast :message="toastMessage" v-if="showToast" />
  </div>
</template>

<script>
import Toast from "../components/Toast.vue";
import customerService from "../services/CustomerService";
import invoiceService from "../services/InvoiceService";
import locationService from "../services/LocationService";
import productService from "../services/ProductService";

export default {
  data() {
    return {
      selectedOption: this.$store.state.isDelivery.orderType
        ? this.$store.state.isDelivery.orderType
        : (localStorage.getItem("orderType")
        ? localStorage.getItem("orderType")
        : ""),
      street: localStorage.getItem('street') ? localStorage.getItem('street') : "",
      city: localStorage.getItem('city') ? localStorage.getItem('city') : "",
      state: localStorage.getItem('state') ? localStorage.getItem('state') : "",
      firstName: localStorage.getItem('firstName') ? localStorage.getItem('firstName') : "",
      lastName: localStorage.getItem('lastName') ? localStorage.getItem('lastName') : "",
      email: localStorage.getItem('email') ? localStorage.getItem('email') : "",
      phoneNumber: localStorage.getItem('phoneNumber') ? localStorage.getItem('phoneNumber') : "",
      username: localStorage.getItem("user"),
      zipcode: localStorage.getItem("zipcode")
        ? localStorage.getItem("zipcode")
        : this.$store.state.isDelivery.zipcode,
      paymentmethod: "",
      formZip: localStorage.getItem('formZip') ? localStorage.getItem('formZip') : "",
      eta: "",
      showToast: false,
      address: "5867 Mayfield Road, Mayfield Heights, OH 44124",
      loading: false,
    };
  },
  components: {
    Toast,
  },
  methods: {
    async submitOrder() {
      if(!this.username) {
        this.toastMessage = "Please log in to place an order";
        this.showToast = true;
        setTimeout(() => {
          this.$emit("close-cart");
          this.showToast = false;
          this.loading = false;
          this.$router.replace('/login');
        }, 2500);
        
        return;
      }
      this.loading = true;
      let creditCardPattern = /^(\d{4}[ -]?){3}\d{4}$/
      if(!creditCardPattern.test(this.paymentmethod)) {
        this.toastMessage = "Credit card must be valid";
        this.showToast = true;
        setTimeout(() => {
          this.showToast = false;
        }, 3500);
        this.loading = false;
        return;
      }
      if (
        this.$store.state.isDelivery.orderType == "delivery" &&
        this.state.length !== 2
      ) {
        this.loading = false;
        this.toastMessage = "Incorrect state abbreviation. Please try again";
        this.showToast = true;
        setTimeout(() => {
          this.showToast = false;
        }, 3500);
        return;
      }
      let isValid = true;
      if (this.$store.state.isDelivery.orderType == "delivery") {
        try {
          let customerAddress = {
            street_address: this.street.replaceAll(" ", "%20"),
            city: this.city.replaceAll(" ", "%20"),
            state_abbreviation: this.state.trim(),
            zip_code: this.formZip.trim(),
          };

          let storeAddress = {
            street_address:
              this.$store.state.storeAddress.street_address.replaceAll(
                " ",
                "%20"
              ),
            city: this.$store.state.storeAddress.city.replaceAll(" ", "%20"),
            state_abbreviation:
              this.$store.state.storeAddress.state_abbreviation.trim(),
            zip_code: this.$store.state.storeAddress.zip_code.trim(),
          };
          let sendAddress = [customerAddress, storeAddress];

          await locationService
            .validateAddress(sendAddress)
            .then((data) => {
              if (data.status !== 200) {
                isValid = false;
                console.log(isValid);
                return;
              }
            })
            .finally(() => {
              if (!isValid) {
                return;
              }
            });
        } catch (err) {
          this.loading = false;
          console.log(err.message);
          this.toastMessage = "This address is not in our service area";
          this.showToast = true;
          setTimeout(() => {
            this.showToast = false;
          }, 3500);
          return;
        }
      }
      console.log(this.$store.state.cart.pizza);
      let pizzaItems = this.$store.state.cart.pizza;
      if (pizzaItems.length > 0) {
        for (let pizza of pizzaItems) {
          pizza.shift();
        }
      }

      const delivery =
        this.$store.state.isDelivery.orderType == "delivery" ? true : false;
      const newInvoice = {
        items: this.$store.state.cart,
        credit_card: this.paymentmethod,
        is_delivery: delivery,
        address:
          this.street + " " + this.city + " " + this.state + " " + this.zipcode,
      };
      console.log(newInvoice);
      let responseInvoice = {};
      console.log("sending order");

      await invoiceService.sendOrder(newInvoice).then((data) => {
        responseInvoice = data.data;
        console.log(data);
        try {
          let localInvoice = JSON.stringify(data.data)
          localStorage.setItem('invoice', localInvoice)
          console.log("clearing cart");
          this.$store.commit("EMPTY_CART");
          if (this.$store.state.token.length > 0) {
            this.toastMessage =
              "We have received your order. Check you email for order information";

            this.showToast = true;
            setTimeout(() => {
              this.showToast = false;
              // this.$router.replace("/myOrders");
              this.$emit("close-cart");
            }, 2500);
          } else {
            this.toastMessage =
              "We have received your order. Check your email for order information";
            this.showToast = true;
            setTimeout(() => {
              this.showToast = false;
              this.$router.replace("/");
              this.$emit("close-cart");
            }, 2500);
          }
        } catch (err) {
          console.log(err);
          this.toastMessage = "Unable to process, your order. Please try again";
          this.showToast = true;
          setTimeout(() => {
            this.showToast = false;
          }, 3500);
        }
      });
      if (responseInvoice) {
        let userInfo = JSON.parse(this.username);
        console.log("sending user info");
        if (userInfo) {
          try {
            console.log(userInfo);
            let newCustomer = {
              firstName: this.firstName,
              lastName: this.lastName,
              streetAddress: this.street,
              city: this.city,
              zipcode: parseInt(this.zipcode),
              stateAbbreviation: this.state,
              phoneNumber: this.phoneNumber,
              email: this.email,
              user_id: userInfo.id,
            };

            await customerService.addCustomer(newCustomer).then((data) => {
              console.log("Added customer");
            
            });
          } catch (error) {
            console.log("This customer already exists");
          } finally {
            this.$router.replace("/myorders");
              this.$emit("refresh-page");
          }
          this.loading = false;
          
        }

        

        // console.log(newCustomer)
      }
    },
    updateUserFirstName() {
      localStorage.setItem('firstName', this.firstName)
    },
    updateUserLastName() {
      localStorage.setItem('lastName', this.lastName)
    },
    updateUserEmail() {
      localStorage.setItem('email', this.email)
    },
    updateUserPhoneNumber() {
      localStorage.setItem('phoneNumber', this.phoneNumber)
    },
    updateUserStreet() {
      localStorage.setItem('street', this.street)
    },
    updateUserCity() {
      localStorage.setItem('city', this.city)
    },
    updateUserState() {
      localStorage.setItem('state', this.state)
    },
    updateUserFormZip() {
      localStorage.setItem('formZip', this.formZip)
    },
    updateOrder() {
      
      localStorage.setItem("orderType", this.selectedOption);
      this.$store.commit("UPDATE_ORDER_TYPE", this.selectedOption);
      console.log(this.$store.state.isDelivery.orderType);
    },
    
    setOrderToNull() {
      this.selectedOption = '';
      localStorage.removeItem("orderType");
      this.$store.commit("UPDATE_ORDER_TYPE", "");
    },

    updateAddress() {
      this.$store.commit("UPDATE_ADDRESS", this.address);
      console.log(this.$store.state.invoice.address);
    },

    setZipCode() {
      const inputElem = document.getElementById("zipcode");

      const zip = inputElem.value;
      const pattern = /^\d{5}(?:-?\d{4})?$/;
      if (pattern.test(zip)) {
        this.$store.commit("ADD_ZIPCODE", zip);
        localStorage.setItem("zipcode", zip);
        this.zipcode = zip;
      } else {
        this.toastMessage = "Invalid zipcode format";
        this.showToast = true;
        setTimeout(() => {
          this.showToast = false;
        }, 3500);
      }
    },
    setZipCodeToNull() {
      this.$store.commit("ADD_ZIPCODE", "");
      this.zipcode = null;
    },

    // remove item from cart
    removeFromCart(item) {
      // remove from currentOrder
      this.$store.commit("REMOVE_FROM_CURRENT_ORDER", item.productId);

      // remove from cart
      const isPizza = this.$store.state.cart.pizza.find(
        (pizza) => pizza.id == item.id
      );
      console.log(isPizza);
      const isOther = this.$store.state.cart.other.find(
        (otherItem) => otherItem === item.productId
      );

      this.$store.commit("REMOVE_FROM_CART", item);
      console.log(this.$store.state.cart);

      // remove from localStorage
      // Check if appetizer
      if (item.product_category_id == 7) {
        let storedSide = localStorage.getItem("sides");
        let objectSide = JSON.parse(storedSide);
        let newObjectSide = objectSide.filter(
          (side) => side.productId !== item.productId
        );
        if (newObjectSide.length > 0) {
          localStorage.setItem("sides", JSON.stringify(newObjectSide));
        } else {
          localStorage.removeItem("sides");
        }
      }
      // Check if specialty pizza
      else if (item.product_category_id == 10) {
        let storedSpecialtyPizza = localStorage.getItem("specialtypizza");
        let objectSpecialtyPizza = JSON.parse(storedSpecialtyPizza);
        let newObjectSpecialtyPizza = objectSpecialtyPizza.filter(
          (pizza) => pizza.productId !== item.productId
        );
        if (newObjectSpecialtyPizza.length > 0) {
          localStorage.setItem("specialtypizza", JSON.stringify(newObjectSpecialtyPizza));
        } else {
          localStorage.removeItem("specialtypizza");
        }
      }
      // Check if drink
      else if (item.product_category_id == 8) {
        let storedDrink = localStorage.getItem("drink");
        let objectDrink = JSON.parse(storedDrink);
        let newObjectDrink = objectDrink.filter(
          (drink) => drink.productId !== item.productId
        );
        if (newObjectDrink.length > 0) {
          localStorage.setItem("drink", JSON.stringify(newObjectDrink));
        } else {
          localStorage.removeItem("drink");
        }
      }

      //  Check if dessert
      else if (item.product_category_id == 9) {
        let storedDessert = localStorage.getItem("dessert");
        let objectDessert = JSON.parse(storedDessert);
        let newObjectDessert = objectDessert.filter(
          (dessert) => dessert.productId !== item.productId
        );
        if (newObjectDessert.length > 0) {
          localStorage.setItem("dessert", JSON.stringify(newObjectDessert));
        } else {
          localStorage.removeItem("dessert");
        }
      }
      // Check if salad
      else if (item.product_category_id == 6) {
        let storedSalad = localStorage.getItem("salads");
        let objectSalad = JSON.parse(storedSalad);
        let newObjectSalad = objectSalad.filter(
          (salad) => salad.productId !== item.productId
        );
        if (newObjectSalad.length > 0) {
          localStorage.setItem("salads", JSON.stringify(newObjectSalad));
        } else {
          localStorage.removeItem("salads");
        }
      } else {
        let storedPizza = localStorage.getItem("pizza");
        storedPizza = JSON.parse(storedPizza);
        if (storedPizza) {
          let newStoredPizza = storedPizza.filter(
            (pizza) => pizza.id !== item.id
          );
          console.log(newStoredPizza)
          if (newStoredPizza.length > 0) {
            localStorage.setItem("pizza", JSON.stringify(newStoredPizza));
          } else {
            localStorage.removeItem("pizza");
          }
        }
      }
    },
    loadStoredItems(storageKey, mutation) {
      let storedItems = localStorage.getItem(storageKey);
      
      if (storedItems && storedItems !== 'undefined') {
        storedItems = JSON.parse(storedItems);
        if (Array.isArray(storedItems) && storedItems.length > 0) {
          storedItems.forEach(item => {
            this.$store.commit(mutation, item.productId);
          });
        }
      }
    },
    loadStoredCustomPizza(storageKey, mutation) {
      let storedPizza = localStorage.getItem(storageKey);
      console.log(storedPizza)
      if (storedPizza && storedPizza !== 'undefined') {
        storedPizza = JSON.parse(storedPizza);
        console.log(storedPizza)
        if (Array.isArray(storedPizza) && storedPizza.length > 0) {
          for(let pizza of storedPizza) {
            let currPizzaArray = []
            currPizzaArray.push({id: pizza.id})
            currPizzaArray.push(pizza.crust.productId)
            currPizzaArray.push(pizza.size.productId)
            currPizzaArray.push(pizza.sauce.productId)
            console.log(currPizzaArray)
            for(let topp of pizza.topping) {
              currPizzaArray.push(topp.productId)
            }
            console.log(currPizzaArray)
            this.$store.commit(mutation, currPizzaArray);
            console.log(this.$store.state.cart)
          }
          
        }
      }
    },
  },

  computed: {
    allItems() {
      return this.$store.state.currentOrder;
    },
    orderType() {
      return this.$store.state.isDelivery.orderType;
    },
    finalPrice() {
      let allItems = this.$store.state.currentOrder;
      let total = 0.0;
      console.log(allItems)
      if (allItems.length > 0) {
        for (let item of allItems) {
          total += parseFloat(item?.price);
        }
      }

      return total.toFixed(2);
    },
  },
  beforeCreate() {
    this.$store.commit('EMPTY_OTHER_CART')
  },
  mounted() {
    // load stored items
    this.loadStoredItems("drink", "ADD_TO_OTHER_CART");
    this.loadStoredItems("sides", "ADD_TO_OTHER_CART");
    this.loadStoredItems("dessert", "ADD_TO_OTHER_CART");
    this.loadStoredItems("salads", "ADD_TO_OTHER_CART");
    this.loadStoredItems("specialtypizza", "ADD_TO_OTHER_CART");

    this.loadStoredCustomPizza('pizza', 'ADD_TO_PIZZA_CART');
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Arimo:ital,wght@0,400..700;1,400..700&family=Calistoga&family=PT+Serif:ital,wght@0,400;0,700;1,400;1,700&display=swap");

.cart-header {
  font-family: "Calistoga", cursive;
  font-size: 2rem;
  margin: 10px;
  width: 100%;
  border-bottom: 1px solid #000;
  text-align: center;
}
.cart-section-container {
  display: flex;
  position: fixed;
  background-color: #fff;
  height: 100vh;
  width: 30vw;
  top: 0;
  right: 0;
  z-index: 99999999999999;
  align-items: center;
  flex-direction: column;
  overflow-y: scroll;
  overflow-x: hidden;
}

.cart-zipcode {
  padding-bottom: 20px;
  width: 100%;
  border-bottom: 1px solid #1e1e1e;
}

.request-cart-zipcode {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.request-cart-zipcode label {
  text-align: center;
  margin-bottom: 10px;
}

.request-cart-zipcode input,
.request-cart-zipcode button {
  width: 70%;
  margin-bottom: 10px;
}

.submit-zipcode {
  background-color: #000;
  color: #fff;
  border: 1px solid #000;
  cursor: pointer;
  border-radius: 5px;
}

.submit-zipcode:hover {
  background-color: #fff;
  color: #000;
}

.show-cart-zipcode {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

.show-cart-zipcode .submit-zipcode {
  width: 40%;
  height: 70%;
  padding: 5px 10px;
}

.item-heading,
.order-type-change-header {
  display: flex;
  justify-content: start;
}

.order-type-change-header {
  justify-content: space-between;
}

.order-type-container {
  text-align: left;
  padding: 0px 15px;
}

button[type="submit"] {
  margin: 10px;
  background-color: #e61d25;
  color: #fff;
  border: none;
  border-radius: 15px;
  padding: 9px 25px;
  cursor: pointer;
  font-size: 1.3em;
}

button[type="submit"]:hover {
  background-color: #000;
}

.cart-close-btn {
  margin: 10px;
  background-color: #000;
  color: #fff;
  border-radius: 15px;
  padding: 9px 15px;
  cursor: pointer;
  font-size: 0.9em;
  border: 1px solid #000;
}

.cart-close-btn:hover {
  background-color: #fff;
  color: #000;
}

.cart-btns {
  display: flex;
  justify-content: end;
  width: 100%;
}

.cart-new-overlay {
  position: fixed;
  background-color: rgba(0, 0, 0, 0.5);
  height: 100vh;
  width: 100vw;
  top: 0;
  left: 0;
  z-index: 9999999999999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.carryout-delivery .cart-select {
  width: 80%;
  text-transform: uppercase;
  margin: 10px 10px;
  cursor: pointer;
}

.carryout-delivery .order-type-text {
  text-transform: uppercase;
}

.carryout-delivery .change-order {
  color: blue;
  cursor: pointer;
  text-decoration: underline;
}

.carryout-delivery {
  align-items: center;
  width: 100%;
  border-bottom: 1px solid #000;
  text-align: center;
}

.order-header {
  width: 70%;
  text-align: center;
  align-items: center;
  padding-left: 30px;
  border-bottom: 1px solid #e6e6e6;
  margin: 10px 0px;
}

.order-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.order-header p,
.order-header h4,
.order-header h3,
.order-header h2,
.order-type-change-header p {
  padding: 5px;
  margin: 0px;
  text-align: left;
}

.payment-container {
  display: flex;
  margin-bottom: 20px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex-direction: column;
}

.payment-container input,
.deliveryInformation input {
  display: flex;
  width: 80%;
  justify-content: center;
  margin-top: 10px;
}

.payment-container .payment-radio-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.remove-from-cart-btn button {
  background-color: #e61d25;
  border: none;
  border-radius: 5px;
  color: #fff;
  padding: 5px 5px;
  cursor: pointer;
}

.remove-from-cart-btn button:hover {
  background-color: #740004;
}

.loader {
  width: 50px;
  aspect-ratio: 1;
  border-radius: 50%;
  background: radial-gradient(farthest-side, #e5e5e5 94%, #0000) top/8px 8px
      no-repeat,
    conic-gradient(#0000 30%, #fff);
  -webkit-mask: radial-gradient(farthest-side, #0000 calc(100% - 8px), #000 0);
  animation: l13 1s infinite linear;
}
@keyframes l13 {
  100% {
    transform: rotate(1turn);
  }
}

@media screen and (max-width: 720px) {
  .cart-section-container {
    width: 80%;
  }
}
</style>
