<template>
    <div class="sidebar-container">
      <ul>
        <li v-on:click="$emit('change-table-inventory')">Inventory</li>
        <li v-on:click="$emit('change-table-order')">Orders</li>
        <li v-on:click="$emit('change-table-specialty')">Specialty Items</li>
        <li v-on:click="$emit('change-table-history')">History</li>
      </ul>
    </div>
  </template>
  
  <script>
  export default {
    name: 'SideBar',
  };
  </script>
  
  <style scoped>
  .sidebar-container {
    position: fixed;
    width: 180px;
    border-right: 1px solid #d3d3d3;
    height: 100vh;
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
    background-color: #1a1a1a; 
    color: #fff;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    padding-top: 80px;
    overflow-x: hidden; 
    transition: width 0.3s ease; 
  }
  
  .sidebar-container ul {
    list-style: none;
    padding: 0;
    margin: 0;
    width: 100%;
  }
  
  .sidebar-container ul li {
    background-color: #2e2e2e;
    color: #fff;
    padding: 15px 20px;
    box-sizing: border-box; 
    width: 100%; 
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
    border-left: 5px solid transparent;
  }
  
  .sidebar-container ul li:hover {
    background-color: #e61d25; 
    border-left: 5px solid #fff; 
  }
  
  .sidebar-container ul li:not(:last-child) {
    border-bottom: 1px solid #383838; 
  }
  

  @media (max-width: 920px) {
    .sidebar-container {
      width: 100%; 
      position: relative; 
      height: auto; 
      padding-top: 20px; 
    }
  
    .sidebar-container ul {
      display: flex;
      flex-direction: row; 
      flex-wrap: wrap; /* Allow wrapping for smaller screens */
    }
  
    .sidebar-container ul li {
      flex: 1; 
      text-align: center; 
      padding: 10px; /* Adjust padding */
    }
  }

  @media screen and (max-width: 900px) {
    .sidebar-container {
        margin-top: 52px;
    }
  }
  </style>
  