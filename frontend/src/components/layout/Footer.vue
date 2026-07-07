<template>
  <footer class="footer">
    <div class="footer-grid">

      <div class="col">
        <h3>🌿 FloraNest</h3>
        <p>Your go-to destination for beautiful plants and gardening essentials.</p>
      </div>

      <div class="col">
        <h4>Shop</h4>
        <router-link to="/products">All Products</router-link>
        <router-link to="/shop">Shop</router-link>
        <router-link to="/plant-care">Plant Care</router-link>
      </div>

      <div class="col">
        <h4>Account</h4>
        <router-link to="/login">Login</router-link>
        <router-link to="/register">Register</router-link>
        <router-link to="/MyProfile">My Profile</router-link>
        <router-link to="/MyOrders">My Orders</router-link>
      </div>

      <div class="col">
        <h4>Contact</h4>
        <p>📧 support@floranest.com</p>
        <p>📞 +1 800 FLORA</p>
        <p>🕐 Mon–Fri, 9am–6pm</p>
      </div>

    </div>

    <div class="footer-bottom">
      <p>© {{ year }} FloraNest. All rights reserved. 🌱</p>
    </div>
  </footer>
</template>

<script setup>
const year = new Date().getFullYear();
</script>

<style scoped>
.footer {
  background: #1B5E20;
  color: white;
  padding: 50px 60px 20px;
  margin-top: 60px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 40px;
  margin-bottom: 30px;
}

.col h3 { font-size: 22px; margin-bottom: 10px; }
.col h4 { font-size: 16px; margin-bottom: 12px; color: #a5d6a7; }
.col p  { color: #c8e6c9; font-size: 14px; line-height: 1.8; }

.col a {
  display: block;
  color: #c8e6c9;
  text-decoration: none;
  font-size: 14px;
  line-height: 2;
  transition: 0.2s;
}
.col a:hover { color: white; }

.footer-bottom {
  border-top: 1px solid #2E7D32;
  padding-top: 20px;
  text-align: center;
  color: #a5d6a7;
  font-size: 13px;
}

@media (max-width: 768px) {
  .footer-grid { grid-template-columns: 1fr 1fr; }
}
</style>