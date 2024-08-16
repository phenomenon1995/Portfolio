<template>
  <div>
    <h2 class="dates-header">Choose a Start and End date</h2>
    <div class="search-dates-container">
        
      <div>
        <label for="start-date">Start Date:</label>
        <input
          type="date"
          id="start-date"
          v-model="startDate"
          @change="filterInvoicesByDate"
        />
      </div>

      <div>
        <label for="end-date">End Date:</label>
        <input
          type="date"
          id="end-date"
          v-model="endDate"
          @change="filterInvoicesByDate"
        />
      </div>
    </div>

    <Line
      v-if="chartReady && startDate && endDate"
      :data="data"
      :options="options"
    />
    <h2 class="order-header" v-if="chartReady && startDate && endDate">Completed Orders Information</h2>
    <table v-if="filteredInvoices.length > 0">
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
          v-for="(order, index) in filteredInvoices"
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
  <HistoryOrderPopUp
  v-if="showOrder"
    :showPopUp="showOrder"
    :order="currOrder"
    @hide-popup="showOrderPopUp({})"

  />
</template>

<script>
import invoiceService from "../services/InvoiceService";
import HistoryOrderPopUp from "./HistoryOrderPopUp.vue";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
} from "chart.js";
import { Line } from "vue-chartjs";

ChartJS.register(
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
);

export default {
  components: {
    Line,
    HistoryOrderPopUp
  },
  data() {
    return {
        currOrder: {},
        showOrder: false,
      chartReady: false,
      startDate: "",
      endDate: "",
      filteredInvoices: [],
      allInvoices: [
        { date: "2024-01-15", total: 50 },
        { date: "2024-02-20", total: 75 },
        { date: "2024-03-10", total: 100 },
        { date: "2024-04-05", total: 60 },
      ],
      data: {
        labels: [],
        datasets: [
          {
            label: "Invoice Totals",
            data: [],
            fill: false,
            borderColor: "#e61d25",
            tension: 0.1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            beginAtZero: true,
          },
          y: {
            beginAtZero: true,
          },
        },
      },
    };
  },

  mounted() {
    this.getAllInvoices();
    this.filterInvoicesByDate(this.allInvoices);
    this.chartReady = true;
  },
  methods: {
    getAllInvoices() {
      invoiceService.getOrders().then((data) => {
        let getOrders = [...data.data];
        this.allInvoices = getOrders;
      });
    },
    filterInvoicesByDate() {
      if (this.startDate && this.endDate) {
        const filteredInvoices = this.allInvoices.filter((invoice) => {
          console.log(invoice);
          const invoiceDate = new Date(invoice.timestamp.slice(0, 10));
          return (
            invoice.status == "Complete" &&
            invoiceDate >= new Date(this.startDate) &&
            invoiceDate <= new Date(this.endDate)
          );
        });
        this.filteredInvoices = filteredInvoices;
        this.updateChart(filteredInvoices);
      }
    },
    updateChart(filteredInvoices) {
      const labels = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
      ];
      const dataPoints = filteredInvoices.map((invoice) =>
        invoice.timestamp.slice(0, 10)
      );
      let JanuaryNum = 0;
      let FebruaryNum = 0;
      let MarchNum = 0;
      let AprilNum = 0;
      let MayNum = 0;
      let JuneNum = 0;
      let JulyNum = 0;
      let AugustNum = 0;
      let SeptemberNum = 0; // Added SeptemberNum
      let OctoberNum = 0;
      let NovemberNum = 0;
      let DecemberNum = 0; // Added DecemberNum

      const correctData = [];

      // Loop through each date in dataPoints
      for (let date of dataPoints) {
        // Extract the month part from the date (Assuming date format is YYYY-MM-DD)
        const month = date.slice(5, 7);

        // Use a switch case to increment the respective month counter
        switch (month) {
          case "01":
            JanuaryNum++;
            break;
          case "02":
            FebruaryNum++;
            break;
          case "03":
            MarchNum++;
            break;
          case "04":
            AprilNum++;
            break;
          case "05":
            MayNum++;
            break;
          case "06":
            JuneNum++;
            break;
          case "07":
            JulyNum++;
            break;
          case "08":
            AugustNum++;
            break;
          case "09":
            SeptemberNum++;
            break;
          case "10":
            OctoberNum++;
            break;
          case "11":
            NovemberNum++;
            break;
          case "12":
            DecemberNum++;
            break;
          default:
            console.error(`Unknown month: ${month}`);
            break;
        }
      }

      correctData.push(
        JanuaryNum,
        FebruaryNum,
        MarchNum,
        AprilNum,
        MayNum,
        JuneNum,
        JulyNum,
        AugustNum,
        SeptemberNum,
        OctoberNum,
        NovemberNum,
        DecemberNum
      );

      console.log(correctData);

      this.data.labels = labels;
      this.data.datasets[0].data = correctData;

      // Force chart to re-render
      this.chartReady = false;
      this.$nextTick(() => {
        this.chartReady = true;
      });
    },
    showOrderPopUp(order) {
    this.currOrder = order
    console.log(this.currOrder)
    this.showOrder = !this.showOrder
    }
  },
  
};
</script>

<style scoped>
.table-wrapper {
  overflow-y: auto;
  width: 100%;
  text-align: center;
}

table {
  width: 80%;
  border-collapse: collapse;
  border: 2px solid #ddd;
}

thead th {
  position: sticky;
  top: 0;
  background-color: #f8f8f8;
  z-index: 1;
  padding: 12px;
  border-bottom: 2px solid #e61d25;
  text-transform: uppercase;
  width: 25%;
}

tbody tr {
  cursor: pointer;
  border-bottom: 1px solid #ddd;
}

tbody tr:hover {
  background-color: #f0f0f0;
}

td {
  padding: 12px;
}

.search-dates-container {
  display: flex;
  justify-content: space-between;
}

.search-dates-container label {
    font-size: 1.5em;
    margin-right: 10px;
}
input[type='date'] {
    width: 30%;
    padding: 10px 25px;
}

.order-header {
    margin-top: 90px;
    text-align: center;
}

@media screen and (max-width: 820px) {
  .search-dates-container {
    flex-direction: column;
    align-items: center;
    gap: 40px;
  }

  .dates-header {
    text-align: center;
  }

  table {
    overflow-x: scroll;
    width: 95%;
    margin: 0 auto;
  }

  .table-wrapper {
    overflow-x: scroll;
  }
}
</style>
