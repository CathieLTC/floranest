<template>
  <footer class="footer">
    <div class="footer-grid">

      <div class="col">
        <h3>FloraNest</h3>
        <p>Your go-to destination for beautiful plants and gardening essentials.</p>
      </div>

      <div class="col">
        <h4>Shop</h4>
        <router-link to="/shop">Shop</router-link>
        <router-link to="/products">Products</router-link>
        <router-link to="/reviews">Reviews</router-link>
      </div>

      <div class="col">
        <h4>Account</h4>
        <router-link to="/login">Login</router-link>
        <router-link to="/register">Register</router-link>
        <router-link to="/my-profile">My Profile</router-link>
        <router-link to="/my-orders">My Orders</router-link>
        <router-link to="/wishlist">Wishlist</router-link>
      </div>

      <div class="col">
        <h4>Contact</h4>
        <p>📧 support@floranest.com</p>
        <p>☎ +1 800 FLORA</p>
        <p>🕐 Mon–Fri, 9am–6pm</p>
      </div>

    </div>

    <div class="footer-bottom">
      <p>© {{ year }} FloraNest. All rights reserved.</p>
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
  padding: 28px 60px 14px;
  margin-top: 40px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 24px;
  margin-bottom: 16px;
}

.col h3 { font-size: 18px; margin-bottom: 6px; }
.col h4 { font-size: 14px; margin-bottom: 8px; color: #a5d6a7; }
.col p  { color: #c8e6c9; font-size: 13px; line-height: 1.5; }

.col a {
  display: block;
  color: #c8e6c9;
  text-decoration: none;
  font-size: 13px;
  line-height: 1.7;
  transition: 0.2s;
}
.col a:hover { color: white; }

.footer-bottom {
  border-top: 1px solid #2E7D32;
  padding-top: 12px;
  text-align: center;
  color: #a5d6a7;
  font-size: 12px;
}

@media (max-width: 768px) {
  .footer-grid { grid-template-columns: 1fr 1fr; }
}
</style>
