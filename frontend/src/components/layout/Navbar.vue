<template>
  <div class="app-shell">
    <!-- Shared botanical backdrop for every page except the homepage -->
    <div v-if="!isHome" class="site-bg" aria-hidden="true"></div>

    <header class="navbar">
      <div class="navbar-inner">
        <!-- Logo -->
        <router-link to="/" class="logo">
          <span class="logo-icon">
            <svg width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M14 2C14 2 6 6 6 14C6 20 10 26 14 26C18 26 22 20 22 14C22 6 14 2 14 2Z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M14 8V26M14 8C14 8 10 11 10 16M14 8C14 8 18 11 18 16" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
            </svg>
          </span>
          <span class="logo-text">Flora<span class="logo-accent">Nest</span></span>
        </router-link>

        <!-- Navigation -->
        <nav class="nav-links">
          <router-link to="/">Home</router-link>
          <router-link to="/shop">Shops</router-link>
          <router-link to="/products">Products</router-link>
          <router-link to="/tools-and-pots">Tools &amp; Pots</router-link>
          <router-link to="/reviews">Reviews</router-link>
          <router-link to="/about">About</router-link>
        </nav>

        <!-- Right Section -->
        <div class="right-section">
          <el-input
            v-model="searchQuery"
            placeholder="Search plants…"
            class="search"
            clearable
            @keyup.enter="goToSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <!-- Wishlist icon -->
          <el-badge :value="wishlistStore.count" :hidden="wishlistStore.count === 0" class="nav-badge">
            <button class="icon-btn" @click="router.push('/wishlist')" title="Wishlist">
              <el-icon><Star /></el-icon>
            </button>
          </el-badge>

          <!-- Cart icon -->
          <button class="icon-btn" @click="goToCart" title="Cart">
            <el-icon><ShoppingCart /></el-icon>
          </button>

          <div class="auth-area">
            <el-dropdown v-if="user" trigger="hover" placement="bottom" size="default">
              <span class="profile-trigger">
                <span class="avatar">
                  {{ user.fullName?.charAt(0).toUpperCase() || 'U' }}
                </span>
                <span class="profile-name">{{ user.fullName }}</span>
                <el-icon class="arrow"><ArrowDown /></el-icon>
              </span>

              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/my-profile')"><el-icon><UserFilled /></el-icon>My Profile</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/my-orders')"><el-icon><Box /></el-icon>My Orders</el-dropdown-item>
                  <el-dropdown-item divided @click="logout"><el-icon><SwitchButton /></el-icon>Logout</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <div v-else class="auth-links">
              <router-link to="/login" class="auth-login">Login</router-link>
              <router-link to="/register" class="auth-register">Sign Up</router-link>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
    <AppFooter />

    <!-- Floating AI Assistant button -->
    <AiFab />
  </div>
</template>

<script setup>
  import { computed, ref } from "vue";
  import { Search, ShoppingCart, ArrowDown, UserFilled, Box, Star, SwitchButton } from "@element-plus/icons-vue";
  import { useRoute, useRouter } from "vue-router";
  import { ElMessageBox } from "element-plus";
  import { useUserStore } from "@/stores/user";
  import { useWishlistStore } from "@/stores/wishlist";
  import AppFooter from '@/components/layout/Footer.vue';
  import AiFab from '@/components/layout/AiFab.vue';

  const router = useRouter();
  const route = useRoute();

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
  /* ======================  NAVBAR  ====================== */
  .navbar {
    position: sticky;
    top: 0;
    z-index: 1000;
    background: rgba(255, 255, 255, 0.92);
    backdrop-filter: blur(12px);
    -webkit-backdrop-filter: blur(12px);
    border-bottom: 1px solid var(--fn-border);
  }

  .navbar-inner {
    max-width: 1340px;
    margin: 0 auto;
    height: var(--fn-nav-h);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 24px;
    gap: 20px;
  }

  /* ======================  LOGO  ====================== */
  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
    text-decoration: none;
    flex-shrink: 0;
  }

  .logo-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    background: var(--fn-green-50);
    border-radius: var(--fn-radius);
    color: var(--fn-green-600);
    transition: all var(--fn-t);
  }

  .logo:hover .logo-icon {
    background: var(--fn-green-600);
    color: #fff;
  }

  .logo-text {
    font-size: 20px;
    font-weight: 800;
    color: var(--fn-ink);
    letter-spacing: -0.03em;
  }

  .logo-accent {
    color: var(--fn-green-600);
  }

  /* ======================  NAV LINKS  ====================== */
  .nav-links {
    display: flex;
    align-items: center;
    gap: 26px;
    margin: 0 auto;
    flex: 1;
    justify-content: center;
  }

  .nav-links a {
    text-decoration: none;
    color: var(--fn-text-2);
    font-size: 14px;
    font-weight: 500;
    letter-spacing: 0.01em;
    padding: 6px 2px;
    white-space: nowrap;
    position: relative;
    transition: color var(--fn-t-fast);
  }

  .nav-links a::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: var(--fn-green-500);
    border-radius: 1px;
    transform: scaleX(0);
    transition: transform var(--fn-t);
    transform-origin: center;
  }

  .nav-links a:hover {
    color: var(--fn-green-700);
  }

  .nav-links a:hover::after,
  .router-link-active::after {
    transform: scaleX(1);
  }

  .router-link-active {
    color: var(--fn-green-600) !important;
    font-weight: 600;
  }

  /* ======================  RIGHT SECTION  ====================== */
  .right-section {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
  }

  .search {
    width: 200px;
    min-width: 200px;
    flex-shrink: 0;
    display: block;
  }

  .icon-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 38px;
    height: 38px;
    border-radius: var(--fn-radius);
    border: 1px solid var(--fn-border);
    background: var(--fn-surface);
    color: var(--fn-text-2);
    cursor: pointer;
    transition: all var(--fn-t-fast);
  }

  .icon-btn:hover {
    border-color: var(--fn-green-400);
    color: var(--fn-green-600);
    background: var(--fn-green-50);
  }

  .icon-btn .el-icon {
    font-size: 17px;
  }

  .nav-badge {
    margin-right: 2px;
  }

  /* ======================  AUTH AREA  ====================== */
  .auth-area {
    margin-left: 4px;
  }

  .auth-links {
    display: flex;
    align-items: center;
    gap: 4px;
  }

  .auth-login {
    color: var(--fn-text-2);
    font-weight: 600;
    font-size: 14px;
    padding: 8px 14px;
    border-radius: var(--fn-radius);
    transition: color var(--fn-t-fast);
  }

  .auth-login:hover {
    color: var(--fn-green-600);
  }

  .auth-register {
    background: var(--fn-green-600);
    color: #fff;
    font-weight: 600;
    font-size: 14px;
    padding: 8px 16px;
    border-radius: var(--fn-radius);
    transition: all var(--fn-t-fast);
  }

  .auth-register:hover {
    background: var(--fn-green-700);
    color: #fff;
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(31, 98, 55, 0.25);
  }

  .profile-trigger {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 4px 10px 4px 4px;
    border-radius: var(--fn-radius);
    transition: background var(--fn-t-fast);
  }

  .profile-trigger:hover {
    background: var(--fn-green-50);
  }

  .avatar {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: linear-gradient(135deg, var(--fn-green-600), var(--fn-green-700));
    color: #fff;
    font-weight: 700;
    font-size: 14px;
    flex-shrink: 0;
  }

  .profile-name {
    font-size: 14px;
    font-weight: 600;
    color: var(--fn-text);
    white-space: nowrap;
  }

  .arrow {
    font-size: 12px;
    color: var(--fn-text-3);
    transition: transform var(--fn-t);
  }

  .profile-trigger:hover .arrow {
    transform: rotate(180deg);
  }

  /* ======================  SHARED PAGE BACKDROP  ====================== */
  .site-bg {
    position: fixed;
    inset: 0;
    z-index: -1;
    pointer-events: none;
    background-color: var(--fn-surface-2);
    background-image:
      radial-gradient(900px 500px at 5% -5%, rgba(107, 186, 138, 0.10), transparent 60%),
      radial-gradient(800px 440px at 98% 110%, rgba(31, 98, 55, 0.08), transparent 60%),
      linear-gradient(175deg, #fbfcfb 0%, #f5f8f5 50%, #f0f4f0 100%);
  }

  /* ======================  RESPONSIVE  ====================== */
  @media (max-width: 1024px) {
    .search {
      width: 160px;
      min-width: 160px;
    }

    .nav-links {
      gap: 14px;
    }
  }

  @media (max-width: 768px) {
    .navbar-inner {
      padding: 0 16px;
      gap: 8px;
    }

    .nav-links {
      display: none;
    }

    .search {
      width: 140px;
      min-width: 0;
      flex: 1;
    }

    .profile-name {
      display: none;
    }

    .auth-links {
      gap: 0;
    }

    .auth-login {
      padding: 8px;
    }

    .auth-register {
      padding: 8px 12px;
      font-size: 13px;
    }
  }
</style>
