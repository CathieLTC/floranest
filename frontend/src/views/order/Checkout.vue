<template>
  <div class="checkout-page">

    <h1>💳 Checkout</h1>
    <p class="subtitle">Complete your order</p>

    <div class="checkout-grid">

      <!-- LEFT: SHIPPING FORM -->
      <div class="form-box">

        <h2>🚚 Shipping Details</h2>

        <!-- SAVED DETAILS SUMMARY (shown when we have a saved address and aren't editing) -->
        <div v-if="hasSavedAddress && !editingShipping" class="saved-shipping">
          <p><strong>{{ form.name }}</strong></p>
          <p>{{ form.address }}, {{ form.city }}<span v-if="form.country">, {{ form.country }}</span></p>
          <p>{{ form.phone }}</p>
          <el-button size="small" @click="editingShipping = true">Change shipping details</el-button>
        </div>

        <!-- EDITABLE FORM -->
        <template v-else>
          <el-input v-model="form.name" placeholder="Full Name" />
          <el-input v-model="form.email" placeholder="Email" />
          <el-input v-model="form.address" placeholder="Address" />
          <el-input v-model="form.city" placeholder="City" />
          <el-input v-model="form.country" placeholder="Country" />
          <el-input v-model="form.phone" placeholder="Phone Number" />

          <el-checkbox v-model="saveToProfile" class="save-checkbox">
            Save these details to my profile for next time
          </el-checkbox>

          <el-button
            v-if="hasSavedAddress"
            size="small"
            class="cancel-edit-btn"
            @click="cancelEditShipping"
          >
            Cancel
          </el-button>
        </template>

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
          <strong>${{ total.toFixed(2) }}</strong>
        </div>

        <el-button
          type="success"
          size="large"
          class="place-btn"
          :loading="placing"
          @click="placeOrder"
        >
          Place Order
        </el-button>

      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import { useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import api from "@/api/axios";
  import { useUserStore } from "@/stores/user";

  const router = useRouter();
  const userStore = useUserStore();
  const user = userStore.user;

  if (!user) {
    router.push("/login");
  }

  const cart = ref([]);
  const placing = ref(false);

  const form = ref({
    name: user?.fullName || "",
    email: user?.email || "",
    address: "",
    city: "",
    country: "",
    phone: "",
    payment: ""
  });


  const hasSavedAddress = ref(false);
  const editingShipping = ref(false);
  const saveToProfile = ref(true);
  let savedSnapshot = null;

  const loadCart = async () => {

    try{

        const response = await api.get(
            `/cart/user/${user.userId}`
        );

        cart.value = response.data;

    }
    catch(error){
        console.error(error);
        ElMessage.error("Failed to load your cart.");
    }

  };

  const loadShippingDetails = async () => {

    try {

      const response = await api.get(`/users/${user.userId}`);
      const profile = response.data;

      if (profile.address && profile.city && profile.phone) {

        form.value.address = profile.address;
        form.value.city = profile.city;
        form.value.country = profile.country || "";
        form.value.phone = profile.phone;

        hasSavedAddress.value = true;
        editingShipping.value = false;

        savedSnapshot = { ...form.value };

      } else {
        // No complete saved address yet — go straight to the editable form
        editingShipping.value = true;
      }

    } catch (error) {
      console.error(error);
      ElMessage.warning("Could not load your saved shipping details. Please enter them below.");
      editingShipping.value = true;
    }

  };

  const cancelEditShipping = () => {
    if (savedSnapshot) {
      form.value = { ...savedSnapshot };
    }
    editingShipping.value = false;
  };

  onMounted(() => {
    loadCart();
    loadShippingDetails();
  });

  const total = computed(() =>
      cart.value.reduce(
          (sum,item)=>sum + item.price * item.quantity,
          0
      )
  );

 const placeOrder = async () => {

  if (placing.value) return;
  placing.value = true;

  try {

    if (cart.value.length === 0) {
      ElMessage.warning("Your cart is empty — add some plants first.");
      return;
    }

    if (
      !form.value.name ||
      !form.value.email ||
      !form.value.address ||
      !form.value.city ||
      !form.value.phone ||
      !form.value.payment
    ) {
      ElMessage.warning("Please fill all required fields");
      return;
    }

    if (editingShipping.value && saveToProfile.value) {

      try {

        await api.put(`/users/${user.userId}`, {
          fullName: form.value.name,
          email: form.value.email,
          phone: form.value.phone,
          address: form.value.address,
          city: form.value.city,
          country: form.value.country
        });

      } catch (error) {
        console.error(error);
        ElMessage.warning("Could not save shipping details to your profile, but you can still continue.");
      }

    }

    sessionStorage.setItem(
      "checkoutData",
      JSON.stringify({
        shipping: form.value,
        cart: cart.value,
        total: Number(total.value.toFixed(2))
      })
    );

    router.push("/payment");

  } finally {
    placing.value = false;
  }

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

/* SAVED SHIPPING SUMMARY */
.saved-shipping{
  background:#f5fff7;
  border:1px solid #d5ecd8;
  border-radius:10px;
  padding:15px 18px;
  margin-bottom:15px;
}

.saved-shipping p{
  margin:0 0 6px;
  color:#333;
}

.saved-shipping .el-button{
  margin-top:8px;
}

.save-checkbox{
  display:block;
  margin-bottom:15px;
}

.cancel-edit-btn{
  margin-bottom:15px;
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
