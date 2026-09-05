<template>
  <div class="profile-page">
    <section class="profile-header">
      <div class="profile-left">
        <div class="profile-photo">
          <div class="profile-image-wrap">
            <img v-if="profileImage" :src="profileImage" alt="Profile" class="profile-image"/>
            <el-icon v-else class="profile-image profile-avatar"><UserFilled /></el-icon>
          </div>
          <input ref="fileInput" type="file" accept="image/*" class="hidden-input" @change="changePhoto"/>
          <el-button type="success" plain @click="fileInput.click()">Change Photo</el-button>
        </div>
        <div>
          <h1>{{ user.fullName }}</h1>
          <p>{{ user.email }}</p>
        </div>
      </div>
    </section>

    <!-- =========================
         DASHBOARD
    ========================= -->
    <section class="dashboard">
      <div class="stat-grid">
        <div class="stat-card" @click="router.push('/my-orders')">
          <div class="stat-icon green"><el-icon><Box /></el-icon></div>
          <div class="stat-info">
            <h2>{{ stats.orders }}</h2>
            <p>Orders</p>
            <span>Track shipments</span>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>

        <div class="stat-card" @click="router.push('/wishlist')">
          <div class="stat-icon pink"><el-icon><Star /></el-icon></div>
          <div class="stat-info">
            <h2>{{ wishlistStore.count }}</h2>
            <p>Wishlist</p>
            <span>Saved plants</span>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>

        <div class="stat-card" @click="router.push('/cart')">
          <div class="stat-icon orange"><el-icon><ShoppingCart /></el-icon></div>
          <div class="stat-info">
            <h2>{{ stats.cart }}</h2>
            <p>Cart Items</p>
            <span>Ready to checkout</span>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>
      </div>

      <p class="manage-label">QUICK LINKS</p>
      <div class="manage-grid">
        <div class="manage-card" @click="goToSection('shipping-addresses')">
          <div class="manage-icon"><el-icon><Location /></el-icon></div>
          <div class="manage-info">
            <h3>Shipping Addresses</h3>
            <p>Manage delivery addresses</p>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>

        <div class="manage-card" @click="goToSection('payment-methods')">
          <div class="manage-icon"><el-icon><CreditCard /></el-icon></div>
          <div class="manage-info">
            <h3>Payment Methods</h3>
            <p>Manage your saved cards</p>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>

        <div class="manage-card" @click="goToSection('notifications')">
          <div class="manage-icon"><el-icon><Bell /></el-icon></div>
          <div class="manage-info">
            <h3>Notifications</h3>
            <p>Choose email preferences</p>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>

        <div class="manage-card" @click="goToSection('password')">
          <div class="manage-icon"><el-icon><Lock /></el-icon></div>
          <div class="manage-info">
            <h3>Security</h3>
            <p>Change your password</p>
          </div>
          <el-icon class="go"><ArrowRight /></el-icon>
        </div>
      </div>
    </section>

    <!-- =========================
         PERSONAL INFORMATION
    ========================= -->
    <section id="personal-info" class="section">
      <h2>👤 Personal Information</h2>
      <div class="form-grid">
        <el-input v-model="user.fullName" placeholder="Full Name"/>
        <el-input v-model="user.email" placeholder="Email"/>
        <el-input v-model="user.phone" placeholder="Phone"/>
        <el-input v-model="user.country" placeholder="Country"/>
        <el-input v-model="user.city" placeholder="City"/>
        <el-input v-model="user.address" placeholder="Address"/>
      </div>
      <el-button type="success" :loading="profileSaving" @click="saveProfile">Save Changes</el-button>
    </section>

    <!-- =========================
         ADDRESS BOOK
    ========================= -->
    <section id="shipping-addresses" class="section">
      <div class="section-header">
        <h2>📍 Shipping Addresses</h2>
        <el-button type="success" plain @click="openAddAddress">Add New Address</el-button>
      </div>

      <div class="address-card" v-for="(address, index) in addresses" :key="index">
        <h3>{{ address.title }}</h3>
        <p>{{ address.address }}</p>
        <p>{{ address.city }}, {{ address.country }}</p>

        <div class="address-buttons">
          <el-button size="small" @click="openEditAddress(index)">Edit</el-button>
          <el-button size="small" type="success" plain @click="setDefaultAddress(index)">Set as Default</el-button>
          <el-button type="danger" size="small" @click="deleteAddress(index)">Delete</el-button>
        </div>
      </div>

      <!-- ADDRESS FORM -->
      <div v-if="showAddressForm" class="edit-box">
        <h3>{{ editingIndex === null ? "Add New Address" : "Edit Address" }}</h3>
        <el-input v-model="addressForm.title" placeholder="Address Name"/>
        <el-input v-model="addressForm.address" placeholder="Street Address"/>
        <el-input v-model="addressForm.city" placeholder="City"/>
        <el-input v-model="addressForm.country" placeholder="Country"/>
        <el-button type="success" @click="saveAddress">Save Address</el-button>
        <el-button @click="showAddressForm = false">Cancel</el-button>
      </div>
    </section>

    <!-- =========================
         PAYMENT METHODS
    ========================= -->
    <section class="section">
      <div class="section-header">
        <h2>💳 Payment Methods</h2>
        <el-button type="success" plain @click="openAddCard">Add Card</el-button>
      </div>

      <div class="payment-card" v-for="(card, index) in cards" :key="index">
        <div class="payment-info">
          <h3>💳 {{ card.type }}</h3>
          <p>**** **** **** {{ card.number }}</p>
          <p>Expiry: {{ card.expiry }}</p>
        </div>
        <el-button type="danger" size="small" @click="removeCard(index)">Remove</el-button>
      </div>

      <!-- ADD CARD FORM -->
      <div v-if="showCardForm" class="edit-box">
        <h3>Add New Card</h3>
        <el-input v-model="cardForm.type" placeholder="Card Type (Visa/Mastercard)"/>
        <el-input v-model="cardForm.number" placeholder="Card Number"/>
        <el-input v-model="cardForm.expiry" placeholder="Expiry Date"/>
        <el-button type="success" @click="saveCard">Save Card</el-button>
        <el-button @click="showCardForm = false">Cancel</el-button>
      </div>
    </section>

    <!-- =========================
         NOTIFICATIONS
    ========================= -->
    <section id="notifications" class="section">
      <h2>🔔 Notification Settings</h2>

      <div class="setting">
        <span>Email Notifications</span>
        <el-switch v-model="notifications.email"/>
      </div>

      <div class="setting">
        <span>Order Updates</span>
        <el-switch v-model="notifications.orders"/>
      </div>

      <div class="setting">
        <span>Promotional Offers</span>
        <el-switch v-model="notifications.promotions"/>
      </div>
    </section>

    <!-- =========================
         RECENT ORDERS
    ========================= -->
    <section class="section">
      <div class="section-header">
        <h2>📦 Recent Orders</h2>
        <router-link to="/my-orders">
          <el-button type="success">View All</el-button>
        </router-link>
      </div>

      <table class="orders-table">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Status</th>
            <th>Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in recentOrders" :key="order.orderId">
            <td>#{{ order.orderNumber }}</td>
            <td>
              <span class="status" :class="statusClass(order.orderStatus)">
                {{ order.orderStatus }}
              </span>
            </td>
            <td>${{ Number(order.totalAmount).toFixed(2) }}</td>
          </tr>
          <tr v-if="recentOrders.length === 0">
            <td colspan="3" style="text-align:center;color:#888;padding:25px;">
              No orders yet.
            </td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- =========================
         PASSWORD
    ========================= -->
    <section id="password" class="section">
      <h2>🔒 Change Password</h2>

      <div class="form-grid">
        <el-input v-model="password.current" type="password" placeholder="Current Password" show-password/>
        <el-input v-model="password.new" type="password" placeholder="New Password" show-password/>
        <el-input v-model="password.confirm" type="password" placeholder="Confirm Password" show-password/>
      </div>

      <el-button type="warning" @click="updatePassword">Update Password</el-button>
    </section>

    <!-- =========================
         LOGOUT
    ========================= -->
    <section class="logout">
      <el-button type="danger" size="large" @click="logout">Logout</el-button>
    </section>
  </div>
</template>

<script setup>
  import { ref, onMounted } from "vue";
  import { ElMessage } from "element-plus";
  import { useRouter } from "vue-router";
  import {
    UserFilled,
    Box,
    Star,
    ShoppingCart,
    ArrowRight,
    Location,
    CreditCard,
    Bell,
    Lock
  } from "@element-plus/icons-vue";
  import api from "@/api/axios";
  import { useUserStore } from "@/stores/user";
  import { useWishlistStore } from "@/stores/wishlist";

  const router = useRouter();
  const userStore = useUserStore();
  const wishlistStore = useWishlistStore();
  const loggedInUser = userStore.user;

  if (!loggedInUser) {
    router.push("/login");
  }

  /* =========================
        PROFILE IMAGE
  ========================= */
  const fileInput = ref(null);
  const profileImage = ref("");

  const changePhoto = (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      profileImage.value = reader.result;
      localStorage.setItem("profileImage", reader.result);
      ElMessage.success("Profile picture updated");
    };
    reader.readAsDataURL(file);
  };

  /* =========================
        USER INFO
  ========================= */
  const user = ref({
    userId: null,
    fullName: "",
    email: "",
    phone: "",
    country: "",
    city: "",
    address: ""
  });

  const profileSaving = ref(false);

  const loadProfile = async () => {
    if (!loggedInUser) return;

    try {
      const response = await api.get(`/users/${loggedInUser.userId}`);
      user.value = {
        userId: response.data.userId,
        fullName: response.data.fullName || "",
        email: response.data.email || "",
        phone: response.data.phone || "",
        country: response.data.country || "",
        city: response.data.city || "",
        address: response.data.address || ""
      };
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load your profile.");
    }
  };

  const saveProfile = async () => {
    if (!loggedInUser) return;

    if (!user.value.fullName || !user.value.email) {
      ElMessage.warning("Full name and email are required.");
      return;
    }

    profileSaving.value = true;

    try {
      await api.put(`/users/${loggedInUser.userId}`, {
        fullName: user.value.fullName,
        email: user.value.email,
        phone: user.value.phone,
        country: user.value.country,
        city: user.value.city,
        address: user.value.address
      });

      ElMessage.success("Profile saved successfully");
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to save your profile.");
    } finally {
      profileSaving.value = false;
    }
  };

  /* =========================
        DASHBOARD
  ========================= */
  const stats = ref({
    orders: 0,
    cart: 0
  });

  const loadDashboard = async () => {
    if (!loggedInUser) return;

    try {
      const [ordersRes, cartRes] = await Promise.all([
        api.get(`/orders/user/${loggedInUser.userId}`),
        api.get(`/cart/${loggedInUser.userId}`)
      ]);

      const orders = ordersRes.data || [];
      const cart = cartRes.data || [];

      stats.value.orders = orders.length;
      stats.value.cart = cart.length;
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load your dashboard.");
    }
  };

  /* =========================
        SECTION NAVIGATION
  ========================= */
  const goToSection = (id) => {
    const target = document.getElementById(id);
    if (target) {
      target.scrollIntoView({ behavior: "smooth", block: "start" });
    }
  };

  /* =========================
        ADDRESS SYSTEM
  ========================= */
  const addresses = ref([]);
  const showAddressForm = ref(false);
  const editingIndex = ref(null);

  const addressForm = ref({
    title: "",
    address: "",
    city: "",
    country: ""
  });

  const addressStorageKey = () =>
    `floranest_addresses_${loggedInUser?.userId || "guest"}`;

  const loadAddresses = () => {
    const saved = localStorage.getItem(addressStorageKey());
    if (saved) {
      try {
        addresses.value = JSON.parse(saved);
        return;
      } catch {
        // corrupted storage — fall through to profile seed
      }
    }
    // Seed with the profile address so Checkout can use it
    addresses.value = user.value.address
      ? [{
          title: "Default",
          address: user.value.address,
          city: user.value.city || "",
          country: user.value.country || ""
        }]
      : [];
  };

  const persistAddresses = () => {
    localStorage.setItem(addressStorageKey(), JSON.stringify(addresses.value));
  };

  // Push an address book entry into the profile so Checkout pre-fills it
  const setDefaultAddress = async (index) => {
    const address = addresses.value[index];
    if (!address) return;

    try {
      await api.put(`/users/${loggedInUser.userId}`, {
        fullName: user.value.fullName,
        email: user.value.email,
        phone: user.value.phone,
        country: address.country,
        city: address.city,
        address: address.address
      });
      ElMessage.success("Default shipping address updated");
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to update default address.");
    }
  };

  const openAddAddress = () => {
    editingIndex.value = null;
    addressForm.value = { title: "", address: "", city: "", country: "" };
    showAddressForm.value = true;
  };

  const openEditAddress = (index) => {
    editingIndex.value = index;
    addressForm.value = { ...addresses.value[index] };
    showAddressForm.value = true;
  };

  const saveAddress = () => {
    if (!addressForm.value.title || !addressForm.value.address) {
      ElMessage.warning("Address name and street address are required.");
      return;
    }

    if (editingIndex.value === null) {
      addresses.value.push({ ...addressForm.value });
    } else {
      addresses.value[editingIndex.value] = { ...addressForm.value };
    }

    showAddressForm.value = false;
    persistAddresses();
    ElMessage.success("Address saved");
  };

  const deleteAddress = (index) => {
    addresses.value.splice(index, 1);
    persistAddresses();
    ElMessage.success("Address deleted");
  };

  /* =========================
        PAYMENT CARDS
  ========================= */
  const cards = ref([]);
  const showCardForm = ref(false);

  const cardForm = ref({
    type: "",
    number: "",
    expiry: ""
  });

  const cardsStorageKey = () =>
    `floranest_cards_${loggedInUser?.userId || "guest"}`;

  const loadCards = () => {
    const saved = localStorage.getItem(cardsStorageKey());
    if (saved) {
      try {
        cards.value = JSON.parse(saved);
      } catch {
        cards.value = [];
      }
      }
      };

      const persistCards = () => {
    localStorage.setItem(cardsStorageKey(), JSON.stringify(cards.value));
  };

  const openAddCard = () => {
    cardForm.value = { type: "", number: "", expiry: "" };
    showCardForm.value = true;
  };

  const saveCard = () => {
    if (!cardForm.value.type || !cardForm.value.number) {
      ElMessage.warning("Card type and number are required.");
      return;
    }

    cards.value.push({
      ...cardForm.value,
      // Only keep the last 4 digits for display
      number: cardForm.value.number.slice(-4)
    });

    showCardForm.value = false;
    persistCards();
    ElMessage.success("Card added");
  };

  const removeCard = (index) => {
    cards.value.splice(index, 1);
    persistCards();
    ElMessage.success("Card removed");
  };

  /* =========================
        NOTIFICATIONS
  ========================= */
  const notifications = ref({
    email: true,
    orders: true,
    promotions: false
  });

  /* =========================
        ORDERS
  ========================= */
  const recentOrders = ref([]);

  const loadRecentOrders = async () => {
    if (!loggedInUser) return;

    try {
      const response = await api.get(`/orders/user/${loggedInUser.userId}`);
      recentOrders.value = (response.data || []).slice(0, 3);
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load your recent orders.");
    }
  };

  const statusClass = (status) => {
    switch (status) {
      case "PENDING":
        return "pending";
      case "PROCESSING":
        return "processing";
      case "SHIPPED":
        return "shipped";
      case "DELIVERED":
        return "delivered";
      case "CANCELLED":
        return "cancelled";
      default:
        return "";
    }
  };

  /* =========================
        PASSWORD
  ========================= */
  const password = ref({
    current: "",
    new: "",
    confirm: ""
  });

  const updatePassword = async () => {
    if (!password.value.current || !password.value.new || !password.value.confirm) {
      ElMessage.warning("Please complete all password fields.");
      return;
    }

    if (password.value.new !== password.value.confirm) {
      ElMessage.error("Passwords do not match.");
      return;
    }

    try {
      const response = await api.put(
        `/users/${loggedInUser.userId}/password`,
        {
          currentPassword: password.value.current,
          newPassword: password.value.new
        }
      );

      ElMessage.success(response.data);

      password.value = { current: "", new: "", confirm: "" };
    } catch (error) {
      console.error(error);
      ElMessage.error(error.response?.data || "Failed to update password.");
    }
  };

  /* =========================
        LOGOUT
  ========================= */
  const logout = () => {
    userStore.logout();
    ElMessage.warning("Logged out");
    router.push("/login");
  };

  onMounted(async () => {
    const savedImage = localStorage.getItem("profileImage");
    if (savedImage) {
      profileImage.value = savedImage;
    }

    await loadProfile();

    loadAddresses();
    loadCards();
    loadDashboard();
    loadRecentOrders();
  });
</script>

<style scoped>
  /* =========================
        PAGE
  ========================= */
  .profile-page {
    background: transparent;
    min-height: 100vh;
    padding: 40px;
  }

  /* =========================
        PROFILE HEADER
  ========================= */
  .profile-header {
    background: white;
    padding: 30px;
    border-radius: 20px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, .08);
    margin-bottom: 30px;
  }

  .profile-left {
    display: flex;
    align-items: center;
    gap: 30px;
  }

  .profile-photo {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }

  .profile-image-wrap {
    width: 140px;
    height: 140px;
    border-radius: 50%;
    border: 5px solid #2E7D32;
    overflow: hidden;
    background: #f0faf1;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .profile-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .profile-avatar {
    font-size: 72px;
    color: #2E7D32;
  }

  .hidden-input {
    display: none;
  }

  .profile-left h1 {
    color: #2E7D32;
    margin-bottom: 10px;
  }

  .profile-left p {
    color: #666;
  }

  /* =========================
        DASHBOARD
  ========================= */
  .dashboard {
    margin-bottom: 30px;
  }

  .stat-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: 20px;
  }

  .stat-card {
    display: flex;
    align-items: center;
    gap: 16px;
    background: white;
    padding: 20px 22px;
    border-radius: 18px;
    cursor: pointer;
    border: 2px solid transparent;
    box-shadow: 0 5px 15px rgba(0, 0, 0, .07);
    transition: transform .25s, box-shadow .25s, border-color .25s;
  }

  .stat-card:hover {
    transform: translateY(-5px);
    border-color: #2E7D32;
    box-shadow: 0 12px 26px rgba(46, 125, 50, .16);
  }

  .stat-icon {
    width: 56px;
    height: 56px;
    flex: none;
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 26px;
  }

  .stat-icon.green   { background: #e8f5e9; color: #2E7D32; }
  .stat-icon.blue    { background: #e3f2fd; color: #1976d2; }
  .stat-icon.pink    { background: #fce4ec; color: #d81b60; }
  .stat-icon.orange  { background: #fff3e0; color: #ef6c00; }

  .stat-info h2 {
    font-size: 30px;
    line-height: 1;
    color: #2E7D32;
    margin: 0;
  }

  .stat-info p {
    margin: 8px 0 0;
    font-weight: 600;
    color: #333;
    font-size: 14px;
  }

  .stat-info span {
    display: block;
    margin-top: 3px;
    font-size: 12.5px;
    color: #aaa;
  }

  .go {
    margin-left: auto;
    flex: none;
    color: #c8e6c9;
    font-size: 18px;
    transition: .25s;
  }

  .stat-card:hover .go { color: #2E7D32; }

  .manage-label {
    margin: 26px 0 12px;
    font-size: 12px;
    font-weight: 700;
    letter-spacing: 1.5px;
    color: #9ccc9c;
  }

  .manage-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
    gap: 14px;
  }

  .manage-card {
    display: flex;
    align-items: center;
    gap: 14px;
    background: white;
    border: 1px dashed #c8e6c9;
    border-radius: 14px;
    padding: 14px 16px;
    cursor: pointer;
    transition: .25s;
  }

  .manage-card:hover {
    background: #f5fff7;
    border-style: solid;
    border-color: #2E7D32;
    transform: translateY(-2px);
  }

  .manage-icon {
    width: 42px;
    height: 42px;
    flex: none;
    border-radius: 12px;
    background: #f5fff7;
    color: #2E7D32;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    transition: .25s;
  }

  .manage-card:hover .manage-icon {
    background: #2E7D32;
    color: white;
  }

  .manage-info h3 {
    margin: 0;
    font-size: 15px;
    color: #2E7D32;
  }

  .manage-info p {
    margin: 3px 0 0;
    font-size: 12.5px;
    color: #999;
  }

  .manage-card .go { font-size: 16px; color: #d5e8d5; }
  .manage-card:hover .go { color: #2E7D32; }

  /* =========================
        SECTIONS
  ========================= */
  .section {
    background: white;
    padding: 30px;
    border-radius: 20px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, .08);
    margin-bottom: 30px;
    scroll-margin-top: 24px;
  }

  .section h2 {
    color: #2E7D32;
    margin-bottom: 20px;
  }

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  /* =========================
        FORM
  ========================= */
  .form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 15px;
    margin-bottom: 20px;
  }

  /* =========================
        ADDRESS
  ========================= */
  .address-card {
    background: #f9fff9;
    padding: 20px;
    border-radius: 15px;
    border-left: 5px solid #2E7D32;
    margin-bottom: 15px;
  }

  .address-card h3 {
    color: #2E7D32;
  }

  .address-buttons {
    display: flex;
    gap: 10px;
    margin-top: 15px;
  }

  .edit-box {
    margin-top: 25px;
    padding: 25px;
    background: #f5fff7;
    border-radius: 15px;
  }

  .edit-box .el-input {
    margin-bottom: 15px;
  }

  /* =========================
        PAYMENT
  ========================= */
  .payment-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #f9f9f9;
    padding: 20px;
    border-radius: 15px;
    margin-bottom: 15px;
  }

  .payment-info h3 {
    color: #2E7D32;
  }

  /* =========================
        SETTINGS
  ========================= */
  .setting {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
  }

  /* =========================
          ORDERS
  ========================= */
  .orders-table {
    width: 100%;
    border-collapse: collapse;
  }

  .orders-table th,
  .orders-table td {
    padding: 15px;
    border-bottom: 1px solid #eee;
    text-align: left;
  }

  .orders-table th {
    background: #f5fff7;
  }

  .status {
    padding: 6px 15px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: bold;
  }

  .delivered {
    background: #2E7D32;
    color: white;
  }

  .processing {
    background: #fbc02d;
    color: #222;
  }

  .shipped {
    background: #1976d2;
    color: white;
  }

  .pending {
    background: #e0e0e0;
    color: #333;
  }

  .cancelled {
    background: #d32f2f;
    color: white;
  }

  /* =========================
          LOGOUT
  ========================= */
  .logout {
    text-align: center;
    margin-bottom: 30px;
  }

  /* =========================
        MOBILE
  ========================= */
  @media (max-width: 768px) {
    .profile-left {
      flex-direction: column;
      text-align: center;
    }

    .section-header {
      flex-direction: column;
      gap: 15px;
      align-items: flex-start;
    }

    .payment-card {
      flex-direction: column;
      gap: 15px;
      align-items: flex-start;
    }

    .orders-table {
      display: block;
      overflow-x: auto;
    }
  }
</style>
