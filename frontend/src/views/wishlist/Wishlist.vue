<template>
  <div class="wishlist-wrapper">

    <!-- HEADER -->
    <div class="wishlist-header">
      <h1>💚 My Wishlist</h1>
      <p v-if="wishlistStore.count > 0">
        You have {{ wishlistStore.count }} plant{{ wishlistStore.count === 1 ? "" : "s" }} in your wishlist.
      </p>
      <p v-else>Your wishlist is empty. Start adding plants you love!</p>
    </div>

    <!-- EMPTY STATE -->
    <div v-if="wishlistStore.count === 0" class="empty-state">
      <div class="empty-icon">🌱</div>
      <h2>No plants yet</h2>
      <p>Browse our collection and click the heart icon to save your favourites here.</p>
      <router-link to="/products">
        <el-button type="success" size="large">Browse Products</el-button>
      </router-link>
    </div>

    <!-- WISHLIST GRID -->
    <div v-else class="grid">
      <div class="card" v-for="item in wishlistStore.items" :key="item.productId">

        <!-- IMAGE (hover to enlarge, click to view details) -->
        <div class="image-container">
          <router-link
            :to="'/product/' + item.productId"
            class="img-link"
            :aria-label="'View details for ' + item.productName"
          >
            <img :src="item.imageUrl" alt="" />
          </router-link>

          <!-- ALWAYS-VISIBLE VIEW DETAILS OVERLAY -->
          <router-link :to="'/product/' + item.productId" class="view-overlay">
            View Details
          </router-link>
        </div>

        <h3>{{ item.productName }}</h3>
        <p class="price">${{ item.price }}</p>

        <div class="actions">
          <el-button type="success" @click="cartStore.addToCart(item)">Add to Cart</el-button>
          <el-button type="danger" @click="wishlistStore.remove(item.productId)">Remove</el-button>
        </div>
      </div>
    </div>

    <!-- CLEAR ALL -->
    <div v-if="wishlistStore.count > 0" class="clear-all">
      <el-button type="danger" plain @click="wishlistStore.clear()">
        Clear Wishlist
      </el-button>
    </div>

  </div>
</template>

<script setup>
import { useWishlistStore } from "@/stores/wishlist";
import { useCartStore } from "@/stores/cart";

const wishlistStore = useWishlistStore();
const cartStore = useCartStore();
</script>

<style scoped>
.wishlist-wrapper {
  padding: 40px 60px;
  background: #f4fff6;
  min-height: 100vh;
}

.wishlist-header {
  text-align: center;
  margin-bottom: 35px;
}

.wishlist-header h1 {
  color: #2E7D32;
  font-size: 32px;
  margin-bottom: 8px;
}

.wishlist-header p {
  color: #666;
}

/* EMPTY STATE */
.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 16px;
}

.empty-state h2 {
  color: #2E7D32;
  font-size: 24px;
  margin-bottom: 8px;
}

.empty-state p {
  color: #888;
  margin-bottom: 20px;
}

/* GRID */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 25px;
  max-width: 1200px;
  margin: 0 auto;
}

/* CARD */
.card {
  background: white;
  padding: 15px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  transition: 0.3s;
}

.card:hover {
  transform: translateY(-5px);
}

/* IMAGE */
.image-container {
  position: relative;
  overflow: hidden;
  border-radius: 10px;
  margin-bottom: 10px;
}

.img-link {
  display: block;
}

.image-container img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
  transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
  cursor: pointer;
}

/* Image grows larger & wider smoothly on hover */
.image-container:hover img {
  transform: scale(1.15);
}

/* VIEW DETAILS OVERLAY */
.view-overlay {
  position: absolute;
  top: 12px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 2;
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  background: rgba(0, 0, 0, 0.4);
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 999px;
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  transition: background 0.25s ease, box-shadow 0.25s ease;
}

.image-container:hover .view-overlay,
.view-overlay:hover {
  background: rgba(21, 94, 25, 0.92);
  box-shadow: 0 4px 14px rgba(21, 94, 25, 0.4);
}

.view-overlay:focus-visible {
  outline: 3px solid #fff;
  outline-offset: 2px;
}

/* PRICE */
.price {
  font-weight: bold;
  color: #2E7D32;
  margin: 8px 0;
}

/* ACTIONS */
.actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.actions .el-button {
  flex: 1;
}

/* CLEAR ALL */
.clear-all {
  text-align: center;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .wishlist-wrapper { padding: 20px; }
}
</style>
