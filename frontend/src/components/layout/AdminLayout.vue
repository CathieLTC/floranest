<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <!-- Admin Navigation -->
      <nav>
        <ul>
          <li><router-link to="/admin">Dashboard</router-link></li>
          <li><router-link to="/admin/products">Product Management</router-link></li>
          <li><router-link to="/admin/categories">Category Management</router-link></li>
          <li><router-link to="/admin/orders">Order Management</router-link></li>
          <li><router-link to="/admin/users">User Management</router-link></li>
        </ul>
      </nav>
    </aside>
    <main class="admin-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
// AdminLayout component logic
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  font-family: 'Inter', sans-serif;
  background-color: #f4f6f8;
}

.admin-sidebar {
  width: 250px;
  background-color: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 20px;
  border-right: 1px solid #e0e0e0;
}

.admin-sidebar nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.admin-sidebar nav ul li {
  margin-bottom: 10px;
}

.admin-sidebar nav ul li a {
  display: block;
  padding: 10px 15px;
  color: #333;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.2s, color 0.2s;
}

.admin-sidebar nav ul li a:hover,
.admin-sidebar nav ul li a.router-link-active {
  background-color: #e6f7ff;
  color: #1890ff;
}

.admin-content {
  flex-grow: 1;
  padding: 20px;
  background-color: #f0f2f5;
}
</style>
