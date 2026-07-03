<template>
  <div>
    <el-header class="navbar">

      <!-- Logo -->
      <div class="logo">
        🌿 FloraNest
      </div>

      <!-- Navigation -->
      <nav class="nav-links">
        <router-link to="/">Home</router-link>
        <router-link to="/products">Products</router-link>
        <router-link to="/shop">Shop</router-link>
        <router-link to="/ai">AI</router-link>
      </nav>

      <!-- Right Section -->
      <div class="right-section">

        <el-input
          placeholder="Search plants..."
          class="search"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-button circle class="icon-btn">
          <el-icon><Star /></el-icon>
        </el-button>

        <el-button circle class="icon-btn">
          <el-icon><ShoppingCart /></el-icon>
        </el-button>

        <div class="auth-links">
          <router-link to="/login">Login</router-link>
          <span>|</span>
          <router-link to="/register">Register</router-link>
        </div>

      </div>

    </el-header>

    <router-view />
  </div>
</template>

<script setup>
import {
  Search,
  ShoppingCart,
  Star
} from "@element-plus/icons-vue";


</script>

<style scoped>

/* ======================
   NAVBAR
====================== */

.navbar{
  height:80px;
  background:#fff;
  display:flex;
  align-items:center;
  justify-content:space-between;
  padding:0 50px;
  box-shadow:0 2px 10px rgba(0,0,0,.08);
}

/* ======================
   LOGO
====================== */

.logo{
  font-size:30px;
  font-weight:bold;
  color:#2E7D32;
  white-space:nowrap;
}

/* ======================
   NAVIGATION
====================== */

.nav-links{
  display:flex;
  align-items:center;
  gap:50px;
  margin-left:60px;
  flex:1;
}

.nav-links a{
  text-decoration:none;
  color:#333;
  font-size:17px;
  font-weight:600;
  transition:.3s;
}

.nav-links a:hover{
  color:#2E7D32;
}

.router-link-active{
  color:#2E7D32;
  border-bottom:3px solid #2E7D32;
  padding-bottom:5px;
}

/* ======================
   RIGHT SIDE
====================== */

.right-section {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

/* 🔥 FIX: ensures search is visible */
.search {
  width: 240px;
  min-width: 240px;
  flex-shrink: 0;
  display: block;
}

.icon-btn{
  width:40px;
  height:40px;
}

.icon-btn .el-icon{
  font-size:18px;
}

.auth-links{
  display:flex;
  align-items:center;
  gap:10px;
  margin-left:10px;
}

.auth-links a{
  text-decoration:none;
  color:#333;
  font-weight:600;
}

.auth-links a:hover{
  color:#2E7D32;
}

.auth-links span{
  color:#bbb;
}

.grow {
  flex: 1;
}

</style>