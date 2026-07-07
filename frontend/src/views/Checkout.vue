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
          <el-option label="🏧Cash on Delivery" value="cod" />
          <el-option label="💳Credit Card" value="card" />
          <el-option label="PayPal" value="paypal" />
        </el-select>

      </div>

      <!-- RIGHT: ORDER SUMMARY -->
      <div class="summary-box">

        <h2>🛒 Order Summary</h2>

        <div class="item" v-for="item in cart" :key="item.cartId">
          <span>{{ item.productName }} × {{ item.quantity }}</span>
          <span>${{ (item.price * item.quantity).toFixed(2) }}</span>
        </div>

        <hr />

        <div class="total">
          <strong>Total:</strong>
          <strong>${{ total }}</strong>
        </div>

        <el-button type="success" size="large" class="place-btn" @click="placeOrder">Place Order</el-button>

      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import { useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import api from "@/api/axios";

  const router = useRouter();

  const user = JSON.parse(localStorage.getItem("user"));

  const cart = ref([]);

  const form = ref({
    name: user.fullName,
    email: user.email,
    address: user.address || "",
    city: "",
    phone: user.phone || "",
    payment: ""
  });

  const loadCart = async () => {

    try{

        const response = await api.get(
            `/cart/user/${user.userId}`
        );

        cart.value = response.data;

    }
    catch(error){
        console.error(error);
    }

  };

  onMounted(loadCart);

  const total = computed(() =>
      cart.value.reduce(
          (sum,item)=>sum + item.price * item.quantity,
          0
      )
  );

 const placeOrder = () => {

  if (
    !form.value.name ||
    !form.value.email ||
    !form.value.address ||
    !form.value.city ||
    !form.value.phone ||
    !form.value.payment
  ) {
    alert("Please fill all required fields");
    return;
  }

  sessionStorage.setItem(
    "checkoutData",
    JSON.stringify({
      shipping: form.value,
      cart: cart.value,
      total: total.value
    })
  );

  router.push("/payment");

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