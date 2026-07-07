<template>
  <section class="categories">
    <h2>Shop by Category</h2>

    <div class="category-grid">
      <router-link
        v-for="cat in categories"
        :key="cat.categoryId"
        :to="`/products?category=${cat.categoryId}`"
        class="category-card"
      >
        <div class="icon">{{ categoryIcon(cat.categoryName) }}</div>
        <p>{{ cat.categoryName }}</p>
      </router-link>

      <!-- Fallback skeleton if still loading -->
      <div v-if="categories.length === 0" class="category-card skeleton" v-for="n in 4" :key="n" />
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '@/api/axios';

const categories = ref([]);

const categoryIcon = (name = '') => {
  const n = name.toLowerCase();
  if (n.includes('indoor'))  return '🪴';
  if (n.includes('outdoor')) return '🌳';
  if (n.includes('flower'))  return '🌸';
  if (n.includes('herb'))    return '🌿';
  if (n.includes('succulent') || n.includes('cactus')) return '🌵';
  if (n.includes('tool'))    return '🪣';
  if (n.includes('seed'))    return '🌱';
  return '🪴';
};

onMounted(async () => {
  try {
    const res = await api.get('/categories');
    categories.value = res.data;
  } catch (e) {
    console.error(e);
  }
});
</script>

<style scoped>
.categories { margin: 40px 0; }
.categories h2 {
  text-align: center;
  color: #2E7D32;
  font-size: 26px;
  margin-bottom: 25px;
}
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 15px;
}
.category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 25px 15px;
  background: white;
  border-radius: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.07);
  cursor: pointer;
  text-decoration: none;
  color: #333;
  transition: 0.25s;
}
.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(46,125,50,0.15);
}
.icon { font-size: 36px; margin-bottom: 10px; }
.category-card p { font-weight: 600; font-size: 14px; text-align: center; }
.skeleton {
  background: #e8f5e9;
  min-height: 100px;
  animation: pulse 1.2s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50%       { opacity: 0.4; }
}
</style>