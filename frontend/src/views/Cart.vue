<template>
  <div class="cart-container">

    <!-- Page Header -->
    <div class="cart-header">
      <h1>🛒 Shopping Cart</h1>
      <p>Review your selected plants before proceeding to checkout.</p>
    </div>

    <div class="cart-content">
      <!-- Cart Items -->
      <div class="cart-items">

        <div class="cart-item"v-for="item in cartItems":key="item.id">
          <img :src="item.imageUrl" :alt="item.productName">

          <div class="item-info">
            <h3>{{ item.productName }}</h3>
            <p class="category">{{ item.categoryName }}</p>
            <p class="price">${{ item.price }}</p>
          </div>

          <div class="quantity">
            <el-button circle @click="decrease(item)"> - </el-button>
            <span>{{ item.quantity }}</span>
            <el-button circle @click="increase(item)"> + </el-button>
          </div>

          <div class="total">
            ${{ (item.price * item.quantity).toFixed(2) }}
          </div>

          <el-button type="danger" @click="removeItem(item.cartId)">Remove </el-button>

        </div>

      </div>

      <!-- Order Summary -->
      <div class="summary">

        <h2>Order Summary</h2>

        <div class="summary-row">
          <span>Items</span>
          <span>{{ totalItems }}</span>
        </div>

        <div class="summary-row">
          <span>Total</span>
          <span>${{ totalPrice.toFixed(2) }}</span>
        </div>

        <router-link to="/checkout">
          <el-button type="success" class="checkout-btn">Proceed to Checkout</el-button>
        </router-link>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import api from "@/api/axios";
import { ElMessage } from "element-plus";

const cartItems = ref([]);
const loadCart = async () => {
  const user = JSON.parse(localStorage.getItem("user"));

  if (!user) {
    ElMessage.warning("Please login first.");
    return;
  }

  try {

    const response = await api.get(`/cart/${user.userId}`);

    cartItems.value = response.data;

  } catch (error) {

    console.error(error);

    ElMessage.error("Failed to load cart.");

  }

};

onMounted(() => {
  loadCart();
});

const increase = async (item) => {
    await api.put(`/cart/increase/${item.cartId}`);
    loadCart();
};

const decrease = async (item) => {

    if(item.quantity==1){

        return;

    }

    await api.put(`/cart/decrease/${item.cartId}`);

    loadCart();

};

const removeItem = async (id)=>{
    await api.delete(`/cart/${id}`);
    loadCart();
}

const totalItems = computed(() =>
  cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
);

const totalPrice = computed(() =>
  cartItems.value.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  )
);
</script>

<style scoped>

.cart-container{
  padding:40px 60px;
  background:#f4fff6;
  min-height:100vh;
}

.cart-header{
  text-align:center;
  margin-bottom:40px;
}

.cart-header h1{
  color:#2E7D32;
  font-size:34px;
}

.cart-header p{
  color:#666;
}

.cart-content{
  display:grid;
  grid-template-columns:2fr 1fr;
  gap:30px;
}

.cart-items{
  display:flex;
  flex-direction:column;
  gap:20px;
}

.cart-item{
  display:grid;
  grid-template-columns:120px 1fr auto auto auto;
  align-items:center;
  gap:20px;
  background:white;
  padding:20px;
  border-radius:12px;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.cart-item img{
  width:120px;
  height:120px;
  object-fit:cover;
  border-radius:10px;
}

.item-info h3{
  margin-bottom:8px;
}

.category{
  color:#777;
}

.price{
  color:#2E7D32;
  font-weight:bold;
  margin-top:8px;
}

.quantity{
  display:flex;
  align-items:center;
  gap:10px;
}

.quantity span{
  font-weight:bold;
  min-width:20px;
  text-align:center;
}

.total{
  font-size:18px;
  font-weight:bold;
  color:#2E7D32;
}

.summary{
  background:white;
  padding:25px;
  border-radius:12px;
  height:fit-content;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.summary h2{
  color:#2E7D32;
  margin-bottom:20px;
}

.summary-row{
  display:flex;
  justify-content:space-between;
  margin:15px 0;
  font-size:16px;
}

.checkout-btn{
  width:100%;
  margin-top:20px;
}

@media(max-width:900px){

.cart-content{
  grid-template-columns:1fr;
}

.cart-item{
  grid-template-columns:1fr;
  text-align:center;
}

.cart-item img{
  margin:auto;
}

.quantity{
  justify-content:center;
}

}

</style>