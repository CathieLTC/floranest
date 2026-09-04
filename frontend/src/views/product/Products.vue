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

        <!-- IMAGE (hover to enlarge, click to view details) -->
        <div class="image-container">
          <router-link :to="'/product/' + item.productId">
            <img :src="item.imageUrl" :alt="item.productName" />
          </router-link>
        </div>

        <h3>{{ item.productName }}</h3>
        <p class="price">$ {{ item.price }}</p>

        <div class="actions">

          <el-button type="success" @click="cartStore.addToCart(item)">Add to Cart</el-button>

          <el-button
            :type="wishlistStore.has(item.productId) ? 'danger' : 'default'"
            size="default"
            @click="wishlistStore.toggle(item)"
          >
            {{ wishlistStore.has(item.productId) ? '♥ Wishlisted' : '♡ Wishlist' }}
          </el-button>

        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();
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
.image-container {
  overflow: hidden;
  border-radius: 10px;
  margin-bottom: 10px;
}

.image-container a {
  display: block;
}

.image-container img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
  transition: transform 0.35s ease;
  cursor: pointer;
}

.image-container:hover img {
  transform: scale(1.12);
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
  justify-content: center;
  gap: 10px;
}

.actions .el-button {
  flex: 1;
}

</style>
