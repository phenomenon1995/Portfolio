<template>
  <div class="my-orders-container">
    <h2>My Orders</h2>
    <!-- Add order table -->
    <div v-if="this.$store.state.allInvoices.length == 0">
      <p>You do not have any orders</p>
    </div>
    <table v-if="this.$store.state.allInvoices.length > 0">
      <thead>
        <tr class="table-header">
          <th class="total-header">Total</th>
          <th class="status-header">Status</th>
          <th class="order-type-header">Order type</th>
        </tr>
      </thead>

      <tr
        v-for="(invoice, index) in this.$store.state.allInvoices"
        :key="index"
        @click="showOrderPopUp(invoice)"
      >
        <td>${{ invoice.total.toFixed(2) }}</td>
        <td>{{ invoice.status }}</td>
        <td>{{ invoice.delivery ? "Delivery" : "Carryout" }}</td>
      </tr>
    </table>
    <!-- Order PopUp -->
    <MyOrderPopUp
      :showPopUp="showOrder"
      :order="currOrder"
      @hide-popup="hideOrderPopUp()"
      @update-order="updateOrder"
    />
  </div>
</template>

<script>
import MyOrderPopUp from "../components/MyOrderPopUp.vue";
import invoiceService from "../services/InvoiceService";

export default {
  data() {
    return {
      showOrder: false,
      currOrder: {}
    };
  },
  components: {
    MyOrderPopUp,
  },

  methods: {
    getAllOrders() {
      const userToken = localStorage.getItem("token");

      if (this.$store.state.allInvoices.length == 0) {
        invoiceService.getOrders(userToken).then((data) => {
          for (let invoice of data.data) {
            this.$store.commit("ADD_INVOICE", invoice);
            console.log("Added to store");
          }
          
        });
        
      }

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
      window.location.reload();
    },
  },

  computed: {

  },
  
  mounted() {
    
    let user = this.$store.state.user;
    if (user?.authorities[0]?.name == "ROLE_ADMIN") {
      this.$router.replace("/admin");
    }

    this.getAllOrders();
    
  },
};
</script>

<style>
.my-orders-container {
  margin-top: 90px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 80vh;
}

.my-orders-container h2 {
  text-transform: uppercase;
  font-size: 2em;
  font-weight: bold;
}

.my-orders-container table {
  width: 60%;
  border-collapse: collapse;
}
.my-orders-container thead {
  width: 100%;
  text-align: left;
  border-bottom: 6px solid #e61d25;
}

.my-orders-container th {
  text-transform: uppercase;
}

.my-orders-container tr.table-header:hover {
  background-color: #fff;
}

.my-orders-container .total-header {
  width: 40%;
}

.my-orders-container tr {
  cursor: pointer;
  border-bottom: 1px solid #cfcfcf;
}

.my-orders-container tr:hover {
  background-color: #e4e4e4;
}

.my-orders-container td {
  padding: 20px 0px;
}

.status-header {
  width: 25%;
}
</style>
