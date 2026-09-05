<template>
  <div class="product-card">

    <!-- IMAGE (hover to enlarge, click to view details) -->
    <div class="media">
      <router-link
        :to="'/product/' + product.productId"
        class="image-link"
        :aria-label="'View details for ' + product.productName"
      >
        <img :src="product.imageUrl" alt="" />
      </router-link>

      <!-- ALWAYS-VISIBLE VIEW DETAILS OVERLAY -->
      <router-link :to="'/product/' + product.productId" class="view-overlay">
        View Details
      </router-link>
    </div>

    <div class="card-body">
      <h3>{{ product.productName }}</h3>
      <p class="price">${{ product.price }}</p>

      <div class="actions">
        <el-button type="success" size="small" @click="$emit('add-to-cart', product)">
          Add to Cart
        </el-button>
        <el-button
          :type="wishlistStore.has(product.productId) ? 'danger' : 'default'"
          size="small"
          @click="wishlistStore.toggle(product)"
        >
          {{ wishlistStore.has(product.productId) ? '♥' : '♡' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useWishlistStore } from "@/stores/wishlist";

defineProps({
  product: { type: Object, required: true }
});
defineEmits(['add-to-cart']);

const wishlistStore = useWishlistStore();
</script>

<style scoped>
.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  transition: 0.25s;
}
.product-card:hover { transform: translateY(-5px); }

.media {
  position: relative;
}

.image-link {
  display: block;
  overflow: hidden;
}

.product-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

/* Image grows larger & wider smoothly on hover */
.media:hover img {
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

.media:hover .view-overlay,
.view-overlay:hover {
  background: rgba(21, 94, 25, 0.92);
  box-shadow: 0 4px 14px rgba(21, 94, 25, 0.4);
}

.view-overlay:focus-visible {
  outline: 3px solid #fff;
  outline-offset: 2px;
}

.card-body { padding: 15px; text-align: center; }
.card-body h3 { font-size: 15px; margin-bottom: 6px; }
.price { font-weight: bold; color: #2E7D32; margin-bottom: 12px; }
.actions { display: flex; gap: 8px; justify-content: center; }
</style>
