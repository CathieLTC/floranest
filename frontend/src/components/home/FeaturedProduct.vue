<template>
  <section class="featured">
    <h2>Popular Plants 🌱</h2>

    <div class="product-grid">
      <div
        class="product-card"
        v-for="item in products"
        :key="item.productId"
      >
        <img :src="item.imageUrl" :alt="item.productName" />
        <h3>{{ item.productName }}</h3>
        <p class="price">${{ item.price }}</p>

        <div class="actions">
          <el-button type="success" size="small" @click="addToCart(item)">
            Add to Cart
          </el-button>
          <router-link :to="'/product/' + item.productId">
            <el-button size="small">View</el-button>
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import api from '@/api/axios';

const router = useRouter();
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
      userId: user.userId,
      productId: product.productId,
      quantity: 1
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
  color: #2E7D32;
  font-size: 26px;
  margin-bottom: 25px;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}
.product-card {
  background: white;
  padding: 15px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  transition: 0.25s;
}
.product-card:hover { transform: translateY(-4px); }
.product-card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 10px;
}
.product-card h3 { font-size: 15px; margin-bottom: 5px; }
.price { font-weight: bold; color: #2E7D32; margin-bottom: 10px; }
.actions { display: flex; gap: 8px; justify-content: center; }
</style>