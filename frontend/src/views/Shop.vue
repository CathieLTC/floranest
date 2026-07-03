<template>
  <div class="shop-wrapper">

    <!-- HEADER -->
    <div class="shop-header">
      <h1>🌿 Shop Plants</h1>
      <p>Browse all available plants in one place</p>
    </div>

    <!-- FILTER BAR -->
    <div class="toolbar">

      <!-- SEARCH -->
      <el-input
        v-model="search"
        placeholder="Search plants..."
        class="search"
        clearable
      />

      <!-- CATEGORY FILTER -->
      <el-select v-model="category" class="filter">
        <el-option label="All" value="all" />
        <el-option label="Indoor" value="indoor" />
        <el-option label="Outdoor" value="outdoor" />
        <el-option label="Succulent" value="succulent" />
      </el-select>

      <!-- SORT (optional nice touch) -->
      <el-select v-model="sort" class="filter">
        <el-option label="Default" value="default" />
        <el-option label="Price Low → High" value="low" />
        <el-option label="Price High → Low" value="high" />
      </el-select>

    </div>

    <!-- PRODUCTS GRID -->
    <div class="grid">

      <div
        class="card"
        v-for="(item, i) in filteredProducts"
        :key="i"
      >

        <img :src="item.image" />

        <h3>{{ item.name }}</h3>

        <p class="price">{{ item.price }}$</p>

        <el-button type="success" class="btn">
          Add to Cart
        </el-button>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed } from "vue";

/* STATE */
const search = ref("");
const category = ref("all");
const sort = ref("default");

/* PRODUCTS */
const products = ref([
  {
    name: "Monstera Deliciosa",
    price: 25,
    category: "indoor",
    image: "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"
  },
  {
    name: "Snake Plant",
    price: 18,
    category: "indoor",
    image: "https://images.unsplash.com/photo-1593691509543-c55fb32e5e22"
  },
  {
    name: "Aloe Vera",
    price: 12,
    category: "succulent",
    image: "https://images.unsplash.com/photo-1596541223130-5d31a73fb6c6"
  },
  {
    name: "Palm Tree",
    price: 35,
    category: "outdoor",
    image: "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a"
  }
]);

/* FILTER + SORT LOGIC */
const filteredProducts = computed(() => {

  let result = products.value.filter(p => {
    const matchSearch =
      p.name.toLowerCase().includes(search.value.toLowerCase());

    const matchCategory =
      category.value === "all" || p.category === category.value;

    return matchSearch && matchCategory;
  });

  if (sort.value === "low") {
    result.sort((a, b) => a.price - b.price);
  }

  if (sort.value === "high") {
    result.sort((a, b) => b.price - a.price);
  }

  return result;
});

</script>

<style scoped>

/* WRAPPER */
.shop-wrapper {
  padding: 40px 60px;
  background: #f4fff6;
  min-height: 100vh;
}

/* HEADER */
.shop-header {
  text-align: center;
  margin-bottom: 25px;
}

.shop-header h1 {
  color: #2E7D32;
  font-size: 34px;
}

.shop-header p {
  color: #666;
}

/* TOOLBAR */
.toolbar {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.search {
  width: 280px;
}

.filter {
  width: 180px;
}

/* GRID */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 25px;
}

/* CARD */
.card {
  background: white;
  padding: 15px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  transition: 0.3s;
}

.card:hover {
  transform: translateY(-5px);
}

.card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
}

/* PRICE */
.price {
  font-weight: bold;
  color: #2E7D32;
  margin: 8px 0;
}

/* BUTTON */
.btn {
  width: 100%;
}

</style>