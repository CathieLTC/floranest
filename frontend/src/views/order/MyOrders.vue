<template>
  <div class="orders-page">

    <h1>📦 My Orders</h1>
    <p class="subtitle">Track all your orders in one place</p>

    <div class="table-wrapper">

      <table class="orders-table">

        <thead>
          <tr>
            <th>Order ID</th>
            <th>Date</th>
            <th>Status</th>
            <th>Total</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>

          <!-- MAIN ROW -->
          <template v-for="order in orders" :key="order.id">

            <tr>

              <td>#{{ order.id }}</td>
              <td>{{ order.date }}</td>

              <td>
                <span
                  class="status"
                  :class="statusClass(order.status)"
                >
                  {{ order.status }}
                </span>
              </td>

              <td>${{ order.total }}</td>

              <td>
                <el-button
                  size="small"
                  type="success"
                  @click="toggleDetails(order.id)"
                >
                  {{ expanded === order.id ? "Hide" : "View" }} Details
                </el-button>
              </td>

            </tr>

            <!-- EXPANDED ROW -->
            <tr v-if="expanded === order.id" class="details-row">

              <td colspan="5">

                <div class="details-box">

                  <h4>🛒 Order Items</h4>

                  <div
                    v-for="item in order.items"
                    :key="item.name"
                    class="item"
                  >
                    🌿 {{ item.name }} — Qty: {{ item.qty }}
                  </div>

                </div>

              </td>

            </tr>

          </template>

        </tbody>

      </table>

    </div>

  </div>
</template>

<script setup>
import { ref } from "vue";

/* ORDERS */
const orders = ref([
  {
    id: 1001,
    date: "2026-07-01",
    status: "Delivered",
    total: 65,
    items: [
      { name: "Monstera", qty: 1 },
      { name: "Aloe Vera", qty: 2 }
    ]
  },
  {
    id: 1002,
    date: "2026-07-03",
    status: "Processing",
    total: 40,
    items: [
      { name: "Snake Plant", qty: 1 }
    ]
  },
  {
    id: 1003,
    date: "2026-07-05",
    status: "Shipped",
    total: 80,
    items: [
      { name: "Rose Plant", qty: 2 },
      { name: "Cactus", qty: 1 }
    ]
  }
]);

/* EXPAND CONTROL */
const expanded = ref(null);

const toggleDetails = (id) => {
  expanded.value = expanded.value === id ? null : id;
};

/* STATUS COLORS */
const statusClass = (status) => {
  if (status === "Delivered") return "delivered";
  if (status === "Processing") return "processing";
  if (status === "Shipped") return "shipped";
  return "";
};
</script>

<style scoped>

.orders-page{
  padding:50px;
  background:#f5fff7;
  min-height:100vh;
}

h1{
  color:#2E7D32;
}

.subtitle{
  color:#666;
  margin-bottom:25px;
}

.table-wrapper{
  background:white;
  padding:20px;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,0.08);
}

.orders-table{
  width:100%;
  border-collapse:collapse;
}

.orders-table th,
.orders-table td{
  padding:15px;
  border-bottom:1px solid #eee;
  text-align:left;
}

.orders-table th{
  background:#f8f8f8;
}

/* STATUS */
.status{
  padding:5px 12px;
  border-radius:20px;
  font-size:12px;
  font-weight:bold;
  color:white;
}

.delivered{
  background:#2E7D32;
}

.processing{
  background:#fbc02d;
  color:black;
}

.shipped{
  background:#1976d2;
}

/* DETAILS ROW */
.details-row td{
  background:#f9fff9;
}

.details-box{
  padding:15px;
  border-left:4px solid #2E7D32;
}

.details-box h4{
  margin-bottom:10px;
  color:#2E7D32;
}

.item{
  padding:5px 0;
  color:#555;
}

</style>