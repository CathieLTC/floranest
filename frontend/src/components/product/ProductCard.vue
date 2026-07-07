<template>
  <div class="product-card">
    <img :src="product.imageUrl" :alt="product.productName" />

    <div class="card-body">
      <h3>{{ product.productName }}</h3>
      <p class="price">${{ product.price }}</p>

      <div class="actions">
        <el-button type="success" size="small" @click="$emit('add-to-cart', product)">
          Add to Cart
        </el-button>
        <router-link :to="'/product/' + product.productId">
          <el-button size="small">View</el-button>
        </router-link>
      </div>
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
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  transition: 0.25s;
}
.product-card:hover { transform: translateY(-5px); }

.product-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}
.card-body { padding: 15px; text-align: center; }
.card-body h3 { font-size: 15px; margin-bottom: 6px; }
.price { font-weight: bold; color: #2E7D32; margin-bottom: 12px; }
.actions { display: flex; gap: 8px; justify-content: center; }
</style>