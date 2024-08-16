<template>
  <div class="toppings-table-container">
    <div class="pending-toggle">
        <button @click="toggleTable" class="show-all-pending-btn">{{ isPending ? 'Show All Orders' : 'Show Pending Orders' }}</button>
    </div>
    <div class="table-wrapper">
      <table v-if="orders?.length > 0">
        <thead>
          <tr class="table-header">
            <th class="description-header">Customer Id</th>
            <th class="price-header">Total</th>
            <th class="quantity-header">Order Type</th>
            <th class="quantity-header">Status</th>
            <th class="quantity-header">Date</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(order, index) in orders"
            :key="index"
            @click="showOrderPopUp(order)"
          >
            <td>{{ order?.user_id }}</td>
            <td>${{ order?.total.toFixed(2) }}</td>
            <td>{{ order?.delivery ? "Delivery" : "Carryout" }}</td>
            <td>{{ order?.status }}</td>
            <td>{{ order?.timestamp.slice(0, 10) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <!-- Order PopUp -->
  <OrderInvoicePopUp
    :showPopUp="showOrder"
    :order="currOrder"
    @hide-popup="hideOrderPopUp()"
    @update-order="updateOrder"
  />
</template>

<script>
import invoiceService from "../services/InvoiceService";
import OrderInvoicePopUp from "./OrderInvoicePopUp.vue";

export default {
  data() {
    return {
      orders: [],
      currOrder: {},
      showOrder: false,
      isPending: false
    };
  },
  components: {

    OrderInvoicePopUp
  },
  methods: {
    allOrders() {
      const userToken = localStorage.getItem('token')
      invoiceService.getOrders(userToken).then((data) => {
        let getOrders = [...data.data];
        if(this.isPending) {
            getOrders = getOrders.filter((item) => item.status == 'Pending')
        }
        this.orders = getOrders;
        console.log(this.orders)
        console.log(this.orders);
      });
    },
    hideOrderPopUp() {
      this.showOrder = false;
    },
    showOrderPopUp(invoice) {
      const fullInvoice = invoiceService
        .getOrderByInvoiceId(invoice.invoice_id)
        .then((data) => {
          console.log(data.data);
          this.currOrder = data.data;
          this.showOrder = true;
        });
    },

    updateOrder(newInvoice) {
      this.$store.commit("UPDATE_ORDER", newInvoice, true);
      this.orders = this.orders.filter((item) => 
          item.invoice_id !== newInvoice.invoice.invoice_id
      )
      this.orders.push(newInvoice.invoice)
      this.showOrder = false;
      console.log(this.orders)
    },
    toggleTable() {
        this.isPending = !this.isPending
        this.allOrders();
    }
  },
  mounted() {
    this.allOrders();
  },
};
</script>

<style>
.toppings-table-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: start;
  width: 80%;
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

.category-header,
.description-header,
.price-header,
.quantity-header {
  width: 25%;
}

.toppings-table-container button {
  margin-bottom: 20px;
  margin-top: 0px;
  padding: 10px 20px;
  font-size: 1em;
  cursor: pointer;
  background-color: #e61d25; 
  color: white; 
  border: none; 
  border-radius: 25px; 
  transition: background-color 0.3s ease; 
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); 
}


.toppings-table-container button:hover {
  background-color: #c51b20; 
}


.toppings-table-container button:active {
  background-color: #a31418; 
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.2); 
}


.toppings-table-container button:focus {
  outline: none; 
  box-shadow: 0 0 0 3px rgba(230, 29, 37, 0.4); 
}

@media screen and (max-width: 890px) {
  .toppings-table-container table {
    overflow-x: scroll;
    width: 95%;
  }
  .toppings-table-container {
    width: 100%;
  }
}
</style>
