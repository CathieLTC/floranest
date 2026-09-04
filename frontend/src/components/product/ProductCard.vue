<template>
  <div class="product-card">

    <!-- IMAGE (hover to enlarge, click to view details) -->
    <router-link :to="'/product/' + product.productId" class="image-link">
      <img :src="product.imageUrl" :alt="product.productName" />
    </router-link>

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

.image-link {
  display: block;
  overflow: hidden;
}

.product-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  transition: transform 0.35s ease;
}

.image-link:hover img {
  transform: scale(1.12);
}

.card-body { padding: 15px; text-align: center; }
.card-body h3 { font-size: 15px; margin-bottom: 6px; }
.price { font-weight: bold; color: #2E7D32; margin-bottom: 12px; }
.actions { display: flex; gap: 8px; justify-content: center; }
</style>
