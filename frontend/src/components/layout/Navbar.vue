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
        <router-link to="/plant-care">Plant Care</router-link>
        <router-link to="/ai">AI Assistant</router-link>
      </nav>

      <!-- Right Section -->
      <div class="right-section">
        <el-input placeholder="Search plants here..." class="search" clearable>
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-button circle class="icon-btn">
          <el-icon><Star /></el-icon>
        </el-button>

        <el-button circle class="icon-btn" @click="goToCart">
          <el-icon><ShoppingCart /></el-icon>
        </el-button>

        <div class="auth-links">
          <el-dropdown v-if="user" trigger="hover" placement="bottom" size="default">
            
            <span class="profile-dropdown"><el-icon><UserFilled /></el-icon> {{ user.fullName }}
              <el-icon class="arrow"><ArrowDown /></el-icon>
            </span>
            
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/my-profile')"><el-icon><UserFilled /></el-icon>My Profile</el-dropdown-item>
                <el-dropdown-item @click="router.push('/my-orders')"><el-icon><Box /></el-icon>My Orders</el-dropdown-item>
                <el-dropdown-item @click="router.push('/order-history')"><el-icon><Document /></el-icon>Order History</el-dropdown-item>
                <el-dropdown-item divided @click="logout">🚪 Logout</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          
          <div v-else>
            <router-link to="/login">Login</router-link>
            <span>|</span>
            <router-link to="/register">Register</router-link>
          </div>
        </div>
      </div>
    </el-header>
       <router-view />
    <AppFooter />
  </div>
</template>

<script setup>
  import { computed } from "vue";
  import { Search, ShoppingCart, Star, ArrowDown, UserFilled, Box, Document } from "@element-plus/icons-vue";
  import { useRouter } from "vue-router";
  import { ElMessageBox } from "element-plus";
  import { useUserStore } from "@/stores/user";
  import AppFooter from '@/components/layout/Footer.vue';

  const router = useRouter();
  const userStore = useUserStore();

  const user = computed(() => userStore.user);

  const goToCart = () => {

    if (!user.value) {

      ElMessageBox.confirm(
        "Please login to access your shopping cart.",
        "Login Required",
        {
          confirmButtonText: "Login",
          cancelButtonText: "Cancel",
          type: "warning"
        }
      ).then(() => {

        router.push("/login");

      });

      return;
    }

    router.push("/cart");

  };

  const logout = () => {

    userStore.logout();

    router.push("/");

  };
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
    background-color:#2E7D32;
    padding: 10px 15px;
    border-radius: 10px;
  }

  .auth-links a{
    text-decoration:none;
    color:#333;
    font-weight:600;
  }

  .auth-links a:hover{
    color:white;
  }

  .auth-links span{
    color:#bbb;
  }

  .grow {
    flex: 1;
  }

  .profile{
    cursor:pointer;
    font-weight:600;
    color:white;
  }

  .profile-dropdown{
      display:flex;
      align-items:center;
      gap:6px;
      cursor:pointer;
      font-weight:600;
      color:white;
      transition:.25s;
  }

  .profile-dropdown:hover{
      color:#1B5E20;
  }

  .arrow{
      font-size:14px;
      transition:.3s;
  }

  .profile-dropdown:hover .arrow{
      transform:rotate(180deg);
  }

</style>