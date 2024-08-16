<template>
  <div
    class="drink-size-pop-up-container"
    v-bind:class="{ 'popup-hidden': showPopUp === false }"
  >
    <div class="drink-size-option-container">
      <div class="modify-order-btns">
        <div v-if="order.invoice?.status == 'Pending'" class="cancel-order">
          <button @click="cancelOrder">Cancel Order</button>
        </div>
        <div v-if="order.invoice?.status == 'Pending' || order.invoice?.status == 'Ready'" class="cancel-order">
          <button @click="completeOrder">Complete Order</button>
        </div>
        <div v-if="order.invoice?.status == 'Pending'" class="cancel-order">
          <button @click="readyOrder">Ready Order</button>
        </div>
      </div>

      <h2>Order Information</h2>

      <div class="order-info-container">
        <div class="order-info">
          <p class="order-info-subheader">Total:</p>
          <p>${{ order.invoice?.total }}</p>
        </div>
        <div class="order-info">
          <p class="order-info-subheader">Status:</p>
          <p>{{ order.invoice?.status }}</p>
        </div>
        <div class="order-info">
          <p class="order-info-subheader">Order Type:</p>
          <p>{{ order.invoice?.delivery ? "Delivery" : "Carryout" }}</p>
        </div>
        <div class="order-info">
          <p class="order-info-subheader">Order Date:</p>
          <p>{{ order.invoice?.timestamp.slice(0, 10) }}</p>
        </div>
      </div>

      <!-- Order Items -->
      <div class="order-items-container">
        <div class="order-items">
          <h3>Items</h3>
          <!-- Pizzas Section -->
          <div v-if="order?.pizzas?.length > 0">
            <div
              class="order-item"
              v-for="(item, index) in order?.pizzas"
              :key="index"
            >
              <p>
                {{
                  item.pizza_name.length > 0 ? item.pizza_name : "Custom Pizza"
                }}
              </p>
              <p>${{ item.total }}</p>
              <p>Size: {{ item.components[0]?.description }}</p>
              <p>Sauce: {{ item.components[1]?.description.split("-")[0] }}</p>
              <p>Crust: {{ item.components[2]?.description.split("-")[0] }}</p>
              <!-- Fix toppings list display -->
              <!-- <p>Toppings: </p>
              <p v-for="(topping,index) in toppings" :key="index">
                {{ topping.description }}
              </p> -->
            </div>
          </div>

          <!-- Additional Items Section -->
          <div v-if="order?.other?.length > 0">
            <div
              class="order-item"
              v-for="(item, index) in order?.other"
              :key="index"
            >
              <p>
                <strong>{{ item?.description }}</strong>
              </p>
              <p>${{ item?.price }}</p>
              <p>{{ item?.product_category_description }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="drink-size-btns">
        <button v-on:click="$emit('hide-popup')" class="cancel-drink-size">
          Close
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import invoiceService from "../services/InvoiceService";

export default {
  data() {
    return {};
  },
  props: {
    order: Object,
    showPopUp: Boolean,
  },
  methods: {
    cancelOrder() {
      const newInvoice = {
        ...this.order.invoice,
      };
      newInvoice.status = "Cancelled";
      this.isComplete = "Cancelled";
      console.log(newInvoice);
      invoiceService
        .updateOrder(newInvoice.invoice_id, newInvoice)
        .then((data) => {
          this.$emit("update-order", {
            pizza: [...this.order.pizzas],
            other: [...this.order.other],
            invoice: newInvoice,
          });
          this.$emit("hide-popup");
          
        });
    },

    completeOrder() {
      const newInvoice = {
        ...this.order.invoice,
      };
      newInvoice.status = "Complete";
      this.isComplete = "Complete";
      console.log(newInvoice);
      invoiceService
        .updateOrder(newInvoice.invoice_id, newInvoice)
        .then((data) => {
          this.$emit("update-order", {
            pizza: [...this.order.pizzas],
            other: [...this.order.other],
            invoice: newInvoice,
          });
          this.$emit("hide-popup");
          
        });
    },
    readyOrder() {
      const newInvoice = {
        ...this.order.invoice,
      };
      newInvoice.status = "Ready";
      this.isComplete = "Ready";
      console.log(newInvoice);
      invoiceService
        .updateOrder(newInvoice.invoice_id, newInvoice)
        .then((data) => {
          this.$emit("update-order", {
            pizza: [...this.order.pizzas],
            other: [...this.order.other],
            invoice: newInvoice,
          });
          this.$emit("hide-popup");
          
        });
    },
  },
  computed: {
    isComplete() {
      console.log("Is Complete:", this.order?.invoice?.complete);
      return this.order?.invoice?.status;
    },

    // Fix toppings list
    toppings() {
      const getAllToppingsFromOrder = [...this.order.pizzas];
      const allToppings = [];
      for (let topping of getAllToppingsFromOrder) {
        console.log(allToppings);
        allToppings.push(topping);
      }
      return allToppings;
    },
  },
};
</script>

<style>
.drink-size-pop-up-container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  background-color: #00000058;
  height: 100vh;
  width: 100vw;
  z-index: 9999999999999999999999999;
}

.drink-size-option-container {
  background-color: #fff;
  width: 60%;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 15px;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px 0px;
}

.order-items-container {
  width: 100%;
  overflow-y: auto;
}

.popup-hidden {
  visibility: hidden;
}

.drink-size-btns {
  width: 60%;
  display: flex;
  justify-content: center;
  gap: 30px;
}

.drink-size-btns button {
  padding: 15px 30px;
  font-size: 1em;
  cursor: pointer;
}

.cancel-order {
  width: 90%;
  display: flex;
  justify-content: end;
}

.cancel-drink-size:hover {
  color: #e61d25;
  border: 1px solid #000;
  background-color: #fff;
}

.cancel-drink-size {
  background-color: #000;
  color: #fff;
  border-color: #000;
}

.cancel-order button {
  background-color: #e61d25;
  border: 1px solid #000;
  color: #fff;
  padding: 10px 15px;
  cursor: pointer;
}

.cancel-order button:hover {
  background-color: #000;
  color: #fff;
  border-color: #000;
}

.order-info {
  display: flex;
  justify-content: center;
  width: 20%;
  text-align: left;
}

.order-info-subheader {
  font-weight: bold;
  padding: 0px 10px;
}

.order-info-container {
  display: flex;
  width: 100%;
  padding: 10px 15px;
  justify-content: space-around;
  border-bottom: 1px solid #dcdcdc;
}

.order-item {
  padding: 5px 15px;
  margin-left: 45px;
  border: 1px solid #dcdcdc;
  width: 90%;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
  margin: 5px 20px;
}

.order-items h3 {
  text-align: center;
  font-size: 2em;
  margin: 5px;
}

.modify-order-btns {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
}
@media screen and (max-width: 820px) {
  .modify-order-btns {
    grid-template-columns: 1fr;
    gap: 5px;
    width: 100%;
  }

  .order-info-container {
    flex-direction: column;
    align-items: center;
  }

  .order-items-container {
    overflow-y: visible;
  }

  .drink-size-option-container {
    width: 95%
  }

  .order-item {
    width: 80%;
    margin: 0px 0px 0px 15px;
    padding: 0px 0px 0px 15px;
  }
}
</style>
