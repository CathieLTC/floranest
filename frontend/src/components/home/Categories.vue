<template>
  <section class="categories">
    <div class="section-head">
      <h2>Shop by Category</h2>
      <p>Find the perfect plant for every space</p>
    </div>

    <div class="category-grid">
      <router-link
        v-for="cat in categories"
        :key="cat.categoryId"
        :to="`/shop?category=${cat.categoryId}`"
        class="category-card"
      >
        <div class="category-image-wrap">
          <img
            :src="categoryImage(cat.categoryName)"
            :alt="cat.categoryName"
            class="category-image"
          />
        </div>
        <p>{{ cat.categoryName }}</p>
      </router-link>

      <!-- Loading Skeleton -->
      <template v-if="categories.length === 0">
        <div
          v-for="n in 4"
          :key="`skeleton-${n}`"
          class="category-card skeleton"
        >
          <div class="skeleton-image"></div>
          <div class="skeleton-text"></div>
        </div>
      </template>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/api/axios";

const categories = ref([]);

/* Category Images */
const categoryImage = (name = "") => {
  const n = name.toLowerCase();
  if (n.includes("indoor")) return "https://images.unsplash.com/photo-1545239351-1141bd82e8a6?w=300";
  if (n.includes("outdoor")) return "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?w=300";
  if (n.includes("flower")) return "https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=300";
  if (n.includes("herb")) return "https://images.unsplash.com/photo-1512428813834-c702c7702b78?w=300";
  if (n.includes("succulent") || n.includes("cactus")) return "https://images.unsplash.com/photo-1459156212016-c812468e2115?w=300";
  if (n.includes("tool")) return "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=300";
  if (n.includes("pot") || n.includes("planter")) return "https://images.unsplash.com/photo-1592150621744-aca64f48394a?w=300";
  if (n.includes("seed")) return "https://images.unsplash.com/photo-1464226184884-fa280b87c399?w=300";
  return "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?w=300";
};

/* Fetch Categories */
onMounted(async () => {
  try {
    const res = await api.get("/categories");
    categories.value = res.data;
  } catch (error) {
    console.error("Failed to load categories:", error);
  }
});
</script>

<style scoped>
.categories {
  margin: 60px 0;
}

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

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 20px;
}

.category-card {
  background: rgba(255, 255, 255, 0.96);
  border-radius: var(--fn-radius-md);
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  color: var(--fn-text);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: var(--fn-shadow);
  transition: all var(--fn-t);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.category-card:hover {
  transform: translateY(-6px);
  border-color: var(--fn-green-200);
  box-shadow: var(--fn-shadow-lg);
}

.category-image-wrap {
  margin-bottom: 14px;
  transition: transform var(--fn-t);
}

.category-card:hover .category-image-wrap {
  transform: scale(1.08);
}

.category-image {
  width: 88px;
  height: 88px;
  object-fit: cover;
  border-radius: 50%;
  border: 3px solid var(--fn-green-500);
}

.category-card p {
  font-size: 14px;
  font-weight: 600;
  text-align: center;
  color: var(--fn-green-700);
}

/* Loading Skeleton */
.skeleton { pointer-events: none; }

.skeleton-image {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  background: var(--fn-green-100);
  animation: pulse 1.5s infinite;
  margin-bottom: 14px;
}

.skeleton-text {
  width: 70%;
  height: 14px;
  border-radius: 7px;
  background: var(--fn-green-100);
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.4; }
  100% { opacity: 1; }
}

@media (max-width: 768px) {
  .category-grid {
    grid-template-columns: repeat(auto-fit, minmax(130px, 1fr));
    gap: 14px;
  }
  .category-image { width: 72px; height: 72px; }
  .skeleton-image { width: 72px; height: 72px; }
}
</style>
