<template>
  <div class="products-wrapper">

    <!-- HEADER -->
    <div class="products-header">
      <h1>Products</h1>
      <p>Browse all available plants</p>
    </div>

    <!-- SEARCH + FILTER -->
    <div class="toolbar">

      <el-input v-model="search" placeholder="Search plants..." class="search" clearable/>

      <el-select v-model="category" class="filter">
        <el-option label="All" value="all" />
        <el-option v-for="c in categories" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId"/>
      </el-select>
    </div>

    <!-- PRODUCTS GRID -->
    <div class="grid">

      <div class="card" v-for="(item, i) in filteredProducts" :key="i">
        <img :src="item.imageUrl" />
        <h3>{{ item.productName }}</h3>
        <p class="price">$ {{ item.price }}</p>

        <div class="actions">

          <el-button type="success" @click="cartStore.addToCart(item)">Add to Cart</el-button>

         <router-link :to="'/product/' + item.productId">
          <el-button size="small">
            View
          </el-button>
        </router-link>

        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";

  const cartStore = useCartStore();
  const search = ref("");
  const category = ref("all");
  const products = ref([]);
  const categories = ref([]);

  const loadData = async () => {
    try {

      const productResponse = await api.get("/products");
      products.value = productResponse.data;

      const categoryResponse = await api.get("/categories");
      categories.value = categoryResponse.data;

    } catch (error) {
      console.error(error);
    }
  };

  onMounted(loadData);

  const filteredProducts = computed(() => {

    return products.value.filter(product => {

      const matchSearch =
        product.productName
          .toLowerCase()
          .includes(search.value.toLowerCase());

      const matchCategory =
        category.value === "all" ||
        product.categoryId == category.value;

      return matchSearch && matchCategory;

    });

  });


</script>

<style scoped>

/* WRAPPER */
.products-wrapper {
  padding: 40px 60px;
  background: #f4fff6;
  min-height: 100vh;
}

/* HEADER */
.products-header {
  text-align: center;
  margin-bottom: 25px;
}

.products-header h1 {
  color: #2E7D32;
  font-size: 32px;
}

.products-header p {
  color: #666;
}

/* TOOLBAR */
.toolbar {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 30px;
}

.search {
  width: 300px;
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

/* IMAGE */
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

/* ACTIONS */
.actions {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

</style>
