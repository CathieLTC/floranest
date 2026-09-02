<template>
  <section class="featured">
    <h2>Popular Plants 🌱</h2>

    <div class="product-grid">
      <ProductCard
        v-for="item in products"
        :key="item.productId"
        :product="item"
        @add-to-cart="addToCart"
      />
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import api from '@/api/axios';
import ProductCard from '@/components/product/ProductCard.vue';

const router  = useRouter();
const products = ref([]);

onMounted(async () => {
  try {
    const res = await api.get('/products');
    products.value = res.data
      .filter(p => p.status === 'ACTIVE')
      .slice(0, 8);
  } catch (e) {
    console.error(e);
  }
});

const addToCart = async (product) => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (!user) {
    ElMessage.warning('Please login first.');
    router.push('/login');
    return;
  }
  try {
    await api.post('/cart', {
      userId:    user.userId,
      productId: product.productId,
      quantity:  1
    });
    ElMessage.success(`${product.productName} added to cart!`);
  } catch (e) {
    ElMessage.error('Could not add to cart.');
  }
};
</script>

<style scoped>
.featured { margin: 40px 0; }

.featured h2 {
  text-align: center;
  color: #e9f5ea;
  font-size: 26px;
  margin-bottom: 25px;
  text-shadow: 0 1px 10px rgba(0, 0, 0, 0.35);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 20px;
}
</style>