<template>
  <div class="admin-container">
    <SideBar 
      @change-table-inventory="changeTableInventory" 
      @change-table-order="changeTableOrder"
      @change-table-specialty="changeTableSpecialty"
      @change-table-history="changeTableHistory"
    />
    <div class="admin-content-container">
      <h1 class="admin-table-heading">{{ currentMenuOption }}</h1>

      <!-- Inventory Table -->
      <InventoryTable v-if="currentMenuOption === 'Inventory'" />
      <!-- Orders Table -->
      <OrdersTable v-if="currentMenuOption === 'Orders'" />
      <!-- SpecialtyPizza Table -->
      <SpecialtyPizzaTable v-if="currentMenuOption === 'Specialty'" />
      <!-- Historical Data -->
      <HistoricalChart v-if="currentMenuOption === 'History'" />
    </div>
  </div>
</template>

<script>
import HistoricalChart from '../components/HistoricalChart.vue';
import InventoryTable from '../components/InventoryTable.vue';
import OrdersTable from '../components/OrdersTable.vue';
import SideBar from "../components/SideBar.vue";
import SpecialtyPizzaTable from '../components/SpecialtyPizzaTable.vue';

export default {
  data() {
    return {
      currentMenuOption: 'Inventory',
    };
  },
  methods: {
    changeTableInventory() {
      this.currentMenuOption = 'Inventory';
    },
    changeTableOrder() {
      this.currentMenuOption = 'Orders';
    },
    changeTableSpecialty() {
      this.currentMenuOption = 'Specialty';
    },
    changeTableHistory() {
      this.currentMenuOption = 'History';
    },
  },
  components: {
    SideBar,
    InventoryTable,
    SpecialtyPizzaTable,
    OrdersTable,
    HistoricalChart,
  },
  mounted() {
    let user = localStorage.getItem('user');
    user = JSON.parse(user);
    let role = user?.authorities[0]?.name;

    if (role !== 'ROLE_ADMIN') {
      this.$router.replace('/myorders');
    }
  },
};
</script>

<style scoped>
.admin-container {
  display: flex;
  flex-direction: column;
  height: fit-content;
  width: 100vw;
  flex-wrap: wrap;
  justify-content: start;
}

.admin-content-container {
  margin-top: 50px;
  margin-left: 320px;
  width: calc(100% - 320px); 
  flex: 1; 
  display: flex;
  flex-direction: column;
  align-items: center;
}

.admin-table-heading {
  text-align: center;
}



@media (max-width: 900px) {
  .admin-container {
    flex-direction: column; 
    width: 100vw;
  }
  
  .admin-content-container {
    margin-left: 0; 
    width: 100%;
  }

  .admin-table-heading {
    font-size: 1.5em; 
  }
}
</style>
