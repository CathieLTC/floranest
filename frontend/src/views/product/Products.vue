<template>
  <div class="products-wrapper">
    <div class="container">

      <!-- HEADER -->
      <header class="products-header">
        <span class="header-eyebrow">Browse</span>
        <h1>Our Products</h1>
        <p>Discover our curated collection of premium plants</p>
      </header>

      <!-- SEARCH + FILTER -->
      <div class="toolbar">
        <el-input v-model="search" placeholder="Search plants…" class="search" clearable>
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="category" class="filter" placeholder="All Categories">
          <el-option label="All Categories" value="all" />
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
            <div
              class="image-link"
              :aria-label="'Click to enlarge ' + item.productName"
              @click="openPreview(item.imageUrl)"
            >
              <img :src="item.imageUrl" alt="" @error="onImgError" />
            </div>

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
        <div class="empty-icon">
          <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 2C7 7 2 12 2 17C2 20 4 22 7 22C10 22 12 20 12 17C12 20 14 22 17 22C20 22 22 20 22 17C22 12 17 7 12 2Z"/>
          </svg>
        </div>
        <h3>No plants found</h3>
        <p>Try a different search term or category.</p>
      </div>

    </div>

    <!-- Fullscreen image preview -->
    <ImagePreview v-model:visible="previewVisible" :src="previewSrc" :alt="previewAlt" />

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import { Search } from "@element-plus/icons-vue";
  import api from "@/api/axios";
  import { ElMessage } from "element-plus";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";
  import ImagePreview from "@/components/common/ImagePreview.vue";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();
  const search = ref("");
  const category = ref("all");
  const products = ref([]);
  const categories = ref([]);

  /* ── Image preview state ── */
  const previewVisible = ref(false);
  const previewSrc = ref("");
  const previewAlt = ref("");

  const openPreview = (src, alt = "") => {
    previewSrc.value = src;
    previewAlt.value = alt;
    previewVisible.value = true;
  };

  const PLACEHOLDER_IMG =
    "data:image/svg+xml;charset=UTF-8," +
    encodeURIComponent(
      `<svg xmlns='http://www.w3.org/2000/svg' width='400' height='400'>
         <rect width='100%' height='100%' fill='#edf7f0'/>
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
  padding: 44px 24px;
  min-height: 100vh;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
}

/* HEADER */
.products-header {
  text-align: center;
  margin-bottom: 36px;
}

.header-eyebrow {
  display: inline-block;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--fn-green-600);
  margin-bottom: 8px;
}

.products-header h1 {
  color: var(--fn-ink);
  font-size: clamp(1.8rem, 4vw, 2.5rem);
  font-weight: 800;
  margin: 0 0 8px;
  letter-spacing: -0.03em;
}

.products-header p {
  color: var(--fn-text-3);
  font-size: 0.95rem;
}

/* TOOLBAR */
.toolbar {
  display: flex;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

.search {
  width: min(340px, 100%);
}

.filter {
  width: 200px;
}

.result-count {
  text-align: center;
  color: var(--fn-text-3);
  font-size: 13px;
  margin: 12px 0 28px;
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
  border-radius: var(--fn-radius-md);
  overflow: hidden;
  border: 1px solid var(--fn-border);
  box-shadow: var(--fn-shadow-sm);
  display: flex;
  flex-direction: column;
  transition: all var(--fn-t);
}

.card:hover {
  transform: translateY(-6px);
  border-color: var(--fn-green-200);
  box-shadow: var(--fn-shadow-lg);
}

/* IMAGE */
.media {
  position: relative;
}

.image-link {
  display: block;
  aspect-ratio: 1 / 1;
  overflow: hidden;
  background: var(--fn-surface-3);
  cursor: zoom-in;
}

.image-link img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

.media:hover .image-link img {
  transform: scale(1.12);
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
  padding: 7px 16px;
  background: rgba(0, 0, 0, 0.45);
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: var(--fn-radius-pill);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  transition: background var(--fn-t-fast);
}

.media:hover .view-overlay,
.view-overlay:hover {
  background: rgba(26, 77, 46, 0.9);
  color: #fff;
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
  font-size: 14px;
  font-weight: 600;
  color: var(--fn-ink);
  line-height: 1.35;
  margin: 0 0 4px;
  min-height: 38px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.price {
  font-size: 18px;
  font-weight: 800;
  color: var(--fn-green-600);
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
  width: 42px;
  flex: none;
  padding: 0;
  font-size: 17px;
}

/* EMPTY STATE */
.empty-state {
  text-align: center;
  padding: 70px 20px;
  background: #fff;
  border-radius: var(--fn-radius-md);
  border: 1px solid var(--fn-border);
  box-shadow: var(--fn-shadow-sm);
}

.empty-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
  background: var(--fn-green-50);
  border-radius: 50%;
  color: var(--fn-green-400);
}

.empty-state h3 {
  color: var(--fn-ink);
  font-size: 18px;
  margin: 0 0 6px;
}

.empty-state p {
  color: var(--fn-text-3);
  font-size: 14px;
  margin: 0;
}
</style>
