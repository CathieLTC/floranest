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
        <img
          :src="categoryImage(cat.categoryName)"
          :alt="cat.categoryName"
          class="category-image"
        />

        <p>{{ cat.categoryName }}</p>
      </router-link>

      <!-- Loading Skeleton -->
      <div
        v-if="categories.length === 0"
        v-for="n in 4"
        :key="n"
        class="category-card skeleton"
      >
        <div class="skeleton-image"></div>
        <div class="skeleton-text"></div>
      </div>
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

  if (n.includes("indoor")) {
    return "https://images.unsplash.com/photo-1545239351-1141bd82e8a6?w=300";
  }

  if (n.includes("outdoor")) {
    return "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?w=300";
  }

  if (n.includes("flower")) {
    return "https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=300";
  }

  if (n.includes("herb")) {
    return "https://images.unsplash.com/photo-1512428813834-c702c7702b78?w=300";
  }

  if (n.includes("succulent") || n.includes("cactus")) {
    return "https://images.unsplash.com/photo-1459156212016-c812468e2115?w=300";
  }

  if (n.includes("tool")) {
    return "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=300";
  }

  if (n.includes("seed")) {
    return "https://images.unsplash.com/photo-1464226184884-fa280b87c399?w=300";
  }

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

.categories{
  margin:50px 0;
}

.categories h2{
  text-align:center;
  color:#2E7D32;
  font-size:30px;
  margin-bottom:30px;
}

.category-grid{
  display:grid;
  grid-template-columns:repeat(auto-fit,minmax(180px,1fr));
  gap:20px;
}

.category-card{
  background:#fff;
  border-radius:18px;
  padding:25px 20px;
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  text-decoration:none;
  color:#333;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
  transition:.3s;
}

.category-card:hover{
  transform:translateY(-8px);
  box-shadow:0 10px 25px rgba(46,125,50,.18);
}

.category-image{
  width:100px;
  height:100px;
  object-fit:cover;
  border-radius:50%;
  border:4px solid #2E7D32;
  margin-bottom:15px;
  transition:.3s;
}

.category-card:hover .category-image{
  transform:scale(1.08);
}

.category-card p{
  font-size:16px;
  font-weight:600;
  text-align:center;
  color:#2E7D32;
}

/* Loading Skeleton */

.skeleton{
  pointer-events:none;
}

.skeleton-image{
  width:100px;
  height:100px;
  border-radius:50%;
  background:#e8f5e9;
  animation:pulse 1.5s infinite;
  margin-bottom:15px;
}

.skeleton-text{
  width:80%;
  height:16px;
  border-radius:8px;
  background:#e8f5e9;
  animation:pulse 1.5s infinite;
}

@keyframes pulse{

  0%{
    opacity:1;
  }

  50%{
    opacity:.4;
  }

  100%{
    opacity:1;
  }

}

@media(max-width:768px){

.category-grid{

grid-template-columns:repeat(auto-fit,minmax(140px,1fr));

}

.category-image{

width:80px;
height:80px;

}

.categories h2{

font-size:24px;

}

}

</style>