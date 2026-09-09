<template>
  <div class="app-shell">
    <!-- Shared botanical backdrop for every page except the homepage
         (the homepage keeps its own ambient video background). -->
    <div v-if="!isHome" class="site-bg" aria-hidden="true"></div>

    <el-header class="navbar">
      <!-- Logo -->
      <div class="logo">
        FloraNest
      </div>

      <!-- Navigation -->
      <nav class="nav-links">
        <router-link to="/">Home</router-link>
        <router-link to="/shop">Shops</router-link>
        <router-link to="/products">Products</router-link>
        <router-link to="/reviews">Reviews</router-link>
        <router-link to="/about">About</router-link>
      </nav>

      <!-- Right Section -->
      <div class="right-section">
        <el-input
          v-model="searchQuery"
          placeholder="Search plants here..."
          class="search"
          clearable
          @keyup.enter="goToSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <!-- Wishlist icon -->
        <el-badge :value="wishlistStore.count" :hidden="wishlistStore.count === 0" class="wishlist-badge">
          <el-button circle class="icon-btn" @click="router.push('/wishlist')">
            <el-icon><Star /></el-icon>
          </el-button>
        </el-badge>

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

    <!-- Floating AI Assistant button -->
    <AiFab />
  </div>
</template>

<script setup>
  import { computed, ref } from "vue";
  import { Search, ShoppingCart, ArrowDown, UserFilled, Box, Star } from "@element-plus/icons-vue";
  import { useRoute, useRouter } from "vue-router";
  import { ElMessageBox } from "element-plus";
  import { useUserStore } from "@/stores/user";
  import { useWishlistStore } from "@/stores/wishlist";
  import AppFooter from '@/components/layout/Footer.vue';
  import AiFab from '@/components/layout/AiFab.vue';

  const router = useRouter();
  const route = useRoute();

  // True on the homepage, which has its own full-screen ambient video.
  const isHome = computed(() => route.path === "/");

  const userStore = useUserStore();
  const wishlistStore = useWishlistStore();

  const user = computed(() => userStore.user);
  const searchQuery = ref("");

  const goToSearch = () => {
    if (searchQuery.value.trim()) {
      router.push({ path: "/search", query: { q: searchQuery.value.trim() } });
    }
  };

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
    justify-content:center;
    gap:48px;
    margin:0 40px;
    flex:1;
  }

  .nav-links a{
    text-decoration:none;
    color:#333;
    font-size:18px;
    font-weight:600;
    letter-spacing:.2px;
    line-height:1;
    padding:8px 2px;
    white-space:nowrap;
    transition:.3s;
  }

  .nav-links a:hover{
    color:#2E7D32;
    transform:translateY(-1px);
  }

  .router-link-active{
    color:#2E7D32 !important;
    border-bottom:3px solid #2E7D32;
    padding-bottom:5px !important;
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

  .wishlist-badge {
    margin-right: 2px;
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
    color:#fff;
    font-weight:600;
    transition:color .2s;
  }

  .auth-links a:hover{
    color:#e8f5e9;
  }

  .auth-links span{
    color:rgba(255,255,255,.55);
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

  /* ======================
    SHARED PAGE BACKDROP
    Soft botanical texture + subtle green gradient shown on every
    non-home page, giving all inner pages one cohesive visual language.
  ====================== */

  .site-bg{
    position: fixed;
    inset: 0;
    z-index: -1;
    pointer-events: none;
    background-color: #f4faf1;
    background-image:
      url("data:image/svg+xml,%3Csvg%20xmlns='http://www.w3.org/2000/svg'%20width='340'%20height='340'%20viewBox='0%200%20340%20340'%3E%3Cg%20fill='none'%20stroke='%236f9674'%20stroke-width='1.1'%20stroke-linecap='round'%20opacity='0.5'%3E%3Cpath%20d='M14%20330%20C70%20300%20120%20260%20150%20185'/%3E%3Cpath%20d='M38%20300%20C50%20268%2070%20248%2092%20240'/%3E%3Cpath%20d='M84%20264%20C100%20248%20126%20240%20150%20240'/%3E%3Cpath%20d='M150%20185%20C142%20152%20148%20122%20168%20100'/%3E%3Cpath%20d='M318%2096%20C292%20102%20268%20116%20250%20138'/%3E%3Cpath%20d='M282%20118%20C282%20140%20290%20162%20306%20176'/%3E%3Cpath%20d='M250%20138%20C252%20160%20246%20180%20234%20196'/%3E%3Cpath%20d='M330%2072%20C322%2084%20320%2098%20324%20112'/%3E%3C/g%3E%3C/svg%3E"),
      radial-gradient(1100px 560px at 8% -6%, rgba(120, 190, 130, 0.20), transparent 62%),
      radial-gradient(950px 520px at 102% 112%, rgba(46, 125, 50, 0.16), transparent 62%),
      radial-gradient(720px 420px at 90% 4%, rgba(200, 230, 201, 0.55), transparent 60%),
      linear-gradient(175deg, #fbfef7 0%, #f1f8ec 48%, #e7f3e2 100%);
    background-size: 340px 340px, auto, auto, auto, auto;
  }

</style>
