<template>
  <div class="confirm-page">

    <div class="box">

      <h1>🎉 Order Confirmed!</h1>

      <p class="subtitle">
        Thank you for your purchase. Your plants are being prepared.
      </p>

      <div class="order-info">

        <p><strong>Order ID:</strong> #FN{{ orderId }}</p>

        <p><strong>Total:</strong> ${{ total }}</p>

        <p><strong>Status:</strong> Processing 🌱</p>

      </div>

      <router-link to="/orders">
        <el-button type="success" size="large">
          View My Orders
        </el-button>
      </router-link>

    </div>

  </div>
</template>

<script setup>
import { ref } from "vue";

const orderId = ref(Math.floor(Math.random() * 100000));
const total = ref(45.99); // later from cart
</script>

<style scoped>
.confirm-page{
  min-height:100vh;
  display:flex;
  justify-content:center;
  align-items:center;
  background:#f5fff7;
}

.box{
  background:white;
  padding:40px;
  border-radius:15px;
  text-align:center;
  box-shadow:0 10px 25px rgba(0,0,0,0.1);
  width:400px;
}

h1{
  color:#2E7D32;
  margin-bottom:10px;
}

.subtitle{
  color:#666;
  margin-bottom:20px;
}

.order-info{
  text-align:left;
  margin:20px 0;
}

.order-info p{
  margin:8px 0;
}
</style>