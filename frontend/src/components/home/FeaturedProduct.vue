<template>
  <section class="featured">
    <div class="section-head">
      <h2>Popular Plants</h2>
      <p>Discover our most-loved green companions</p>
    </div>

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
.featured { margin: 60px 0; }

.section-head {
  text-align: center;
  margin-bottom: 36px;
}

.section-head h2 {
  font-size: clamp(1.5rem, 3vw, 2rem);
  color: var(--fn-green-700);
  margin-bottom: 8px;
  letter-spacing: -0.02em;
}

.section-head p {
  color: var(--fn-text-3);
  font-size: 0.95rem;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}
</style>
