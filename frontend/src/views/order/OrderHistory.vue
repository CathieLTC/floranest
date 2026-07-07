<template>
  <div class="history-page">

    <h1>📜 Order History</h1>

    <p class="subtitle">Completed orders</p>

    <div v-if="history.length === 0" class="empty">
      No past orders 🌱
    </div>

    <div v-else class="orders-list">

      <div
        class="order-card"
        v-for="order in history"
        :key="order.id"
      >

        <div class="top">
          <h3>Order #{{ order.id }}</h3>
          <span class="status">{{ order.status }}</span>
        </div>

        <p><strong>Total:</strong> ${{ order.total }}</p>
        <p><strong>Date:</strong> {{ order.date }}</p>

        <div class="items">
          <p v-for="item in order.items" :key="item.name">
            🌿 {{ item.name }} x{{ item.qty }}
          </p>
        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref } from "vue";

const history = ref([
  {
    id: 1000,
    total: 40,
    status: "Delivered",
    date: "2026-06-25",
    items: [
      { name: "Rose Plant", qty: 2 }
    ]
  },
  {
    id: 999,
    total: 22,
    status: "Delivered",
    date: "2026-06-10",
    items: [
      { name: "Aloe Vera", qty: 1 }
    ]
  }
]);
</script>

<style scoped>
.history-page{
  padding:50px;
  background:#f5fff7;
  min-height:100vh;
}

h1{
  color:#2E7D32;
}

.subtitle{
  color:#666;
  margin-bottom:20px;
}

.empty{
  text-align:center;
  color:#777;
}

.orders-list{
  display:flex;
  flex-direction:column;
  gap:20px;
}

.order-card{
  background:white;
  padding:20px;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.top{
  display:flex;
  justify-content:space-between;
}

.status{
  background:#2E7D32;
  color:white;
  padding:5px 10px;
  border-radius:20px;
  font-size:12px;
}
</style>