<template>
  <div class="product-card">

    <!-- Image wrapper — hover reveals the Add to Cart button -->
    <div class="image-wrapper">
      <img :src="product.imageUrl" :alt="product.productName" />

      <!-- Slides up from bottom on hover -->
      <div class="hover-actions">
        <button class="add-to-cart-btn" @click="$emit('add-to-cart', product)">
          🛒 Add to Cart
        </button>
      </div>
    </div>

    <!-- Card info always visible -->
    <div class="card-body">
      <router-link :to="'/product/' + product.productId" class="product-name">
        {{ product.productName }}
      </router-link>
      <p class="price">${{ product.price }}</p>
    </div>

  </div>
</template>

<script setup>
defineProps({
  product: { type: Object, required: true }
});
defineEmits(['add-to-cart']);
</script>

<style scoped>
.product-card {
  background: white;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(0,0,0,0.13);
}

/* ── Image area ── */
.image-wrapper {
  position: relative;
  overflow: hidden;
}

.image-wrapper img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
  transition: transform 0.5s ease;
}

/* Slight zoom on the image when card is hovered */
.product-card:hover .image-wrapper img {
  transform: scale(1.05);
}

/* ── Hover overlay button ── */
.hover-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 12px;

  /* Hidden by default — sits below the image */
  transform: translateY(100%);
  opacity: 0;
  transition: transform 0.3s ease, opacity 0.3s ease;
}

/* Slide up when card is hovered */
.product-card:hover .hover-actions {
  transform: translateY(0);
  opacity: 1;
}

.add-to-cart-btn {
  width: 100%;
  padding: 10px 0;
  background: white;
  color: #1B5E20;
  border: 2px solid #1B5E20;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.05em;
  cursor: pointer;
  transition: background 0.2s ease, color 0.2s ease;
}

.add-to-cart-btn:hover {
  background: #1B5E20;
  color: white;
}

/* ── Card body ── */
.card-body {
  padding: 14px 16px;
}

.product-name {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  text-decoration: none;
  margin-bottom: 6px;
  transition: color 0.2s;
}

.product-name:hover {
  color: #2E7D32;
}

.price {
  font-size: 15px;
  font-weight: 700;
  color: #2E7D32;
  margin: 0;
}
</style>