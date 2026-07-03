<template>
  <div>

    <el-header class="navbar">
      <div class="logo">
        🌿 FloraNest
      </div>

      <el-menu mode="horizontal" :ellipsis="false" class="menu" router>

        <el-menu-item index="/">
          <router-link to="/" class="menu-link">Home</router-link>
        </el-menu-item>

        <el-menu-item index="/products">
          <router-link to="/products" class="menu-link">Products</router-link>
        </el-menu-item>

        <el-menu-item index="/ai">
          <router-link to="/ai" class="menu-link">AI</router-link>
        </el-menu-item>

      </el-menu>

      <div class="grow"></div>

      <!-- RIGHT SIDE -->
      <div class="right-section">

        <el-input placeholder="Search plants..." class="search">
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

        <!-- AUTH LINKS -->
        <div class="auth-links">
          <router-link to="/login" class="nav-link">Login</router-link>
          <span>|</span>
          <router-link to="/register" class="nav-link">Register</router-link>
        </div>

      </div>

    </el-header>

    <!-- THIS IS WHY YOUR PAGES NOW SHOW -->
    <router-view />

  </div>
</template>

<script setup>
import { Search, ShoppingCart, Star } from "@element-plus/icons-vue";
</script>

<style scoped>

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  background: white;
  height: 80px;
  box-shadow: 0 2px 10px rgba(0,0,0,.08);
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #2E7D32;
}

/* MENU */
.menu {
  flex: 1;
  margin-left: 30px;
  border: none;
  display: flex;
  align-items: center;
}

/* spacing between menu items */
.menu :deep(.el-menu-item) {
  margin-right: 25px;
}

/* pushes right side */
.grow {
  flex-grow: 1;
}

/* RIGHT SIDE */
.right-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* SEARCH */
.search {
  width: 240px;
}

/* ICON BUTTONS */
.icon-btn {
  width: 36px;
  height: 36px;
  padding: 0;
}

.icon-btn .el-icon {
  font-size: 18px;
}

/* AUTH LINKS */
.auth-links {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-left: 10px;
  font-size: 14px;
}

.auth-links span {
  color: #aaa;
}

.nav-link {
  text-decoration: none;
  color: #333;
  font-weight: 500;
}

.nav-link:hover {
  color: #2E7D32;
}

/* MENU LINKS */
.menu-link {
  text-decoration: none;
  color: inherit;
  font-weight: 500;
}

</style>