<template>
  <div class="checkout-page">

    <h1>💳 Checkout</h1>
    <p class="subtitle">Complete your order</p>

    <div class="checkout-grid">

      <!-- LEFT: SHIPPING FORM -->
      <div class="form-box">

        <h2>🚚 Shipping Details</h2>

        <el-input v-model="form.name" placeholder="Full Name" />
        <el-input v-model="form.email" placeholder="Email" />
        <el-input v-model="form.address" placeholder="Address" />
        <el-input v-model="form.city" placeholder="City" />
        <el-input v-model="form.phone" placeholder="Phone Number" />

        <el-select v-model="form.payment" placeholder="Payment Method">
          <el-option label="Cash on Delivery" value="cod" />
          <el-option label="Credit Card" value="card" />
          <el-option label="PayPal" value="paypal" />
        </el-select>

      </div>

      <!-- RIGHT: ORDER SUMMARY -->
      <div class="summary-box">

        <h2>🛒 Order Summary</h2>

        <div
          class="item"
          v-for="item in cart"
          :key="item.name"
        >
          <span>{{ item.name }} x{{ item.qty }}</span>
          <span>${{ item.price * item.qty }}</span>
        </div>

        <hr />

        <div class="total">
          <strong>Total:</strong>
          <strong>${{ total }}</strong>
        </div>

        <el-button
          type="success"
          size="large"
          class="place-btn"
          @click="placeOrder"
        >
          Place Order
        </el-button>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

/* SAMPLE CART (later from Pinia) */
const cart = ref([
  { name: "Monstera", price: 25, qty: 1 },
  { name: "Aloe Vera", price: 15, qty: 2 }
]);

/* FORM */
const form = ref({
  name: "",
  email: "",
  address: "",
  city: "",
  phone: "",
  payment: ""
});

/* TOTAL */
const total = computed(() =>
  cart.value.reduce((sum, item) => sum + item.price * item.qty, 0)
);

/* PLACE ORDER */
const placeOrder = () => {

  if (!form.value.name || !form.value.address || !form.value.payment) {
    alert("Please fill all required fields");
    return;
  }

  // Simulate order creation
  const orderId = Math.floor(Math.random() * 100000);

  // Navigate to confirmation page
  router.push({
    path: "/order-confirmation",
    query: {
      id: orderId,
      total: total.value
    }
  });

};
</script>

<style scoped>

.checkout-page{
  padding:50px;
  background:#f5fff7;
  min-height:100vh;
}

h1{
  color:#2E7D32;
}

.subtitle{
  color:#666;
  margin-bottom:30px;
}

.checkout-grid{
  display:grid;
  grid-template-columns:1fr 1fr;
  gap:30px;
}

/* BOXES */
.form-box,
.summary-box{
  background:white;
  padding:25px;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,0.08);
}

/* INPUTS */
.form-box .el-input,
.form-box .el-select{
  margin-bottom:15px;
  width:100%;
}

/* ORDER ITEMS */
.item{
  display:flex;
  justify-content:space-between;
  margin:10px 0;
  color:#555;
}

.total{
  display:flex;
  justify-content:space-between;
  margin:20px 0;
  font-size:18px;
}

/* BUTTON */
.place-btn{
  width:100%;
}

/* RESPONSIVE */
@media (max-width: 768px){
  .checkout-grid{
    grid-template-columns:1fr;
  }
}
</style>