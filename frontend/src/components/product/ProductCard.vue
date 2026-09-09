<template>
  <div class="product-card">

    <!-- IMAGE (hover to enlarge, click to preview) -->
    <div class="media">
      <div
        class="image-link"
        :aria-label="'Click to enlarge ' + product.productName"
        @click="openPreview(product.imageUrl, product.productName)"
      >
        <img :src="product.imageUrl" alt="" />
      </div>

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
          class="wish-btn"
          @click="wishlistStore.toggle(product)"
        >
          {{ wishlistStore.has(product.productId) ? '♥' : '♡' }}
        </el-button>
      </div>
    </div>

    <!-- Fullscreen image preview -->
    <ImagePreview v-model:visible="previewVisible" :src="previewSrc" :alt="previewAlt" />
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useWishlistStore } from "@/stores/wishlist";
import ImagePreview from "@/components/common/ImagePreview.vue";

defineProps({
  product: { type: Object, required: true }
});
defineEmits(['add-to-cart']);

const wishlistStore = useWishlistStore();

/* ── Image preview state ── */
const previewVisible = ref(false);
const previewSrc = ref("");
const previewAlt = ref("");

const openPreview = (src, alt = "") => {
  previewSrc.value = src;
  previewAlt.value = alt;
  previewVisible.value = true;
};
</script>

<style scoped>
.product-card {
  background: #fff;
  border-radius: var(--fn-radius-md);
  overflow: hidden;
  border: 1px solid var(--fn-border);
  box-shadow: var(--fn-shadow-sm);
  transition: all var(--fn-t);
}
.product-card:hover {
  transform: translateY(-5px);
  border-color: var(--fn-green-200);
  box-shadow: var(--fn-shadow-lg);
}

.media {
  position: relative;
}

.image-link {
  display: block;
  overflow: hidden;
  cursor: zoom-in;
}

.product-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

.media:hover img {
  transform: scale(1.12);
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
  padding: 7px 16px;
  background: rgba(0, 0, 0, 0.45);
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: var(--fn-radius-pill);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  transition: background var(--fn-t-fast);
}

.media:hover .view-overlay,
.view-overlay:hover {
  background: rgba(26, 77, 46, 0.9);
  color: #fff;
}

.view-overlay:focus-visible {
  outline: 3px solid #fff;
  outline-offset: 2px;
}

.card-body {
  padding: 16px;
  text-align: center;
}
.card-body h3 {
  font-size: 14px;
  font-weight: 600;
  color: var(--fn-ink);
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 38px;
}
.price {
  font-size: 18px;
  font-weight: 800;
  color: var(--fn-green-600);
  margin-bottom: 14px;
}
.actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.wish-btn {
  width: 36px;
  padding: 0;
  font-size: 16px;
}
</style>
