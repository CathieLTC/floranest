<template>
  <el-header class="navbar">
    <div class="logo">
      🌿 FloraNest
    </div>

    <el-menu mode="horizontal" :ellipsis="false" class="menu">

      <el-menu-item index="1">Home</el-menu-item>
      <el-menu-item index="2">Products</el-menu-item>

      <!-- replaced static text with router links (safe improvement) -->
      <el-menu-item index="3">
        <router-link to="/login" class="nav-link">Login</router-link>
      </el-menu-item>

      <el-menu-item index="4">
        <router-link to="/register" class="nav-link">Register</router-link>
      </el-menu-item>

      <div class="grow"></div>

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

    </el-menu>

  </el-header>
</template>

<script setup>
  import { Search,ShoppingCart,Star} from "@element-plus/icons-vue";
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

  .menu {
    flex: 1;
    margin-left: 30px;
    border: none;

    display: flex;
    align-items: center;
  }

  .el-menu-item {
    font-size: 15px;
    font-weight: 500;
  }

  .grow {
    flex-grow: 1;
  }

  /* SEARCH BAR */
  .search {
    width: 240px;
    margin-right: 15px;
  }

  /* ICON BUTTON SIZE FIX */
  .icon-btn {
    width: 36px;
    height: 36px;
    padding: 0;
    margin-left: 8px;
  }

  /* reduce icon size */
  .icon-btn .el-icon {
    font-size: 18px;
  }

  /* LOGIN / REGISTER LINKS */
  .nav-link {
    text-decoration: none;
    color: inherit;
    font-weight: 500;
  }

  /* hover effect */
  .nav-link:hover {
    color: #2E7D32;
  }

</style>