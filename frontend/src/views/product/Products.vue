<template>
  <div class="products-wrapper">

    <div class="container">

      <!-- HEADER -->
      <header class="products-header">
        <h1>Products</h1>
        <p>Browse all available plants</p>
      </header>

      <!-- SEARCH + FILTER -->
      <div class="toolbar">
        <el-input v-model="search" placeholder="Search plants..." class="search" clearable />
        <el-select v-model="category" class="filter">
          <el-option label="All" value="all" />
          <el-option v-for="c in categories" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" />
        </el-select>
      </div>

      <p v-if="filteredProducts.length" class="result-count">
        Showing {{ filteredProducts.length }}
        {{ filteredProducts.length === 1 ? 'plant' : 'plants' }}
      </p>

      <!-- PRODUCTS GRID -->
      <div v-if="filteredProducts.length" class="grid">

        <article class="card" v-for="item in filteredProducts" :key="item.productId">

          <!-- IMAGE -->
          <div class="media">
            <router-link
              :to="'/product/' + item.productId"
              class="image-link"
              :aria-label="'View details for ' + item.productName"
            >
              <img :src="item.imageUrl" alt="" @error="onImgError" />
            </router-link>

            <!-- ALWAYS-VISIBLE VIEW DETAILS OVERLAY -->
            <router-link :to="'/product/' + item.productId" class="view-overlay">
              View Details
            </router-link>
          </div>

          <div class="card-body">
            <h3 class="name" :title="item.productName">{{ item.productName }}</h3>
            <p class="price">${{ formatPrice(item.price) }}</p>

            <div class="actions">
              <el-button type="success" class="add-btn" @click="cartStore.addToCart(item)">
                Add to Cart
              </el-button>
              <el-button
                :type="wishlistStore.has(item.productId) ? 'danger' : 'default'"
                class="wish-btn"
                @click="wishlistStore.toggle(item)"
              >
                {{ wishlistStore.has(item.productId) ? '♥' : '♡' }}
              </el-button>
            </div>
          </div>

        </article>

      </div>

      <!-- EMPTY STATE -->
      <div v-else class="empty-state">
        <div class="empty-icon">🪴</div>
        <h3>No plants found</h3>
        <p>Try a different search term or category.</p>
      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import api from "@/api/axios";
  import { ElMessage } from "element-plus";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();
  const search = ref("");
  const category = ref("all");
  const products = ref([]);
  const categories = ref([]);

  const PLACEHOLDER_IMG =
    "data:image/svg+xml;charset=UTF-8," +
    encodeURIComponent(
      `<svg xmlns='http://www.w3.org/2000/svg' width='400' height='400'>
         <rect width='100%' height='100%' fill='#e8f5e9'/>
         <text x='50%' y='50%' font-size='70' text-anchor='middle' dominant-baseline='middle'>🪴</text>
       </svg>`
    );

  const formatPrice = (p) => Number(p).toFixed(2);

  const onImgError = (e) => {
    e.target.src = PLACEHOLDER_IMG;
    e.target.onerror = null;
  };

  const loadData = async () => {
    try {
      const productResponse = await api.get("/products");
      products.value = productResponse.data;

      const categoryResponse = await api.get("/categories");
      categories.value = categoryResponse.data;

    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load products.");
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
  padding: 44px clamp(20px, 5vw, 64px);
  background: #f4fff6;
  min-height: 100vh;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
}

/* HEADER */
.products-header {
  text-align: center;
  margin-bottom: 28px;
}

.products-header h1 {
  color: #1f2937;
  font-size: 30px;
  font-weight: 700;
  margin: 0 0 6px;
  letter-spacing: -0.5px;
}

.products-header p {
  color: #6b7280;
  font-size: 15px;
  margin: 0;
}

/* TOOLBAR */
.toolbar {
  display: flex;
  justify-content: center;
  gap: 14px;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.search {
  width: min(320px, 100%);
}

.filter {
  width: 180px;
}

.result-count {
  text-align: center;
  color: #9ca3af;
  font-size: 13px;
  margin: 10px 0 26px;
}

/* GRID */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 24px;
}

/* CARD */
.card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 28px rgba(46, 125, 50, 0.12);
}

/* IMAGE */
.media {
  position: relative;
}

.image-link {
  display: block;
  aspect-ratio: 1 / 1;
  overflow: hidden;
  background: #f1f5f1;
}

.image-link img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

/* Image grows larger & wider smoothly on hover */
.media:hover .image-link img {
  transform: scale(1.15);
}

/* VIEW DETAILS OVERLAY */
.view-overlay {
  position: absolute;
  top: 14px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 2;
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  background: rgba(0, 0, 0, 0.4);
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 999px;
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  transition: background 0.25s ease, box-shadow 0.25s ease;
}

.media:hover .view-overlay,
.view-overlay:hover {
  background: rgba(21, 94, 25, 0.92);
  box-shadow: 0 4px 14px rgba(21, 94, 25, 0.4);
}

.view-overlay:focus-visible {
  outline: 3px solid #fff;
  outline-offset: 2px;
}

/* CARD BODY */
.card-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.name {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.35;
  margin: 0 0 4px;
  min-height: 40px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.price {
  font-size: 18px;
  font-weight: 700;
  color: #2E7D32;
  margin: 4px 0 14px;
}

/* ACTIONS */
.actions {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.actions .el-button {
  margin-left: 0;
}

.add-btn {
  flex: 1;
  font-weight: 600;
}

.wish-btn {
  width: 44px;
  flex: none;
  padding: 0;
  font-size: 17px;
}

/* EMPTY STATE */
.empty-state {
  text-align: center;
  padding: 70px 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

.empty-icon {
  font-size: 52px;
  margin-bottom: 12px;
}

.empty-state h3 {
  color: #1f2937;
  font-size: 20px;
  margin: 0 0 6px;
}

.empty-state p {
  color: #6b7280;
  font-size: 15px;
  margin: 0;
}

</style>
