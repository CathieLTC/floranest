<template>
  <div class="search-results-wrapper">

    <!-- HEADER -->
    <div class="search-header">
      <h1>🔍 Search Results</h1>
      <p v-if="query" class="query-line">
        Showing plants matching <strong>"{{ query }}"</strong>
      </p>
      <p v-else class="query-line">Type a keyword in the search bar to find plants.</p>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="loading">Loading plants...</div>

    <template v-else>
      <!-- RESULTS -->
      <template v-if="results.length">
        <p class="results-info">
          Found {{ results.length }} plant{{ results.length === 1 ? "" : "s" }}.
        </p>

        <div class="grid">
          <div class="card" v-for="item in results" :key="item.productId">

            <!-- IMAGE (hover to enlarge, click to preview) -->
            <div class="image-container">
              <div
                class="img-link"
                :aria-label="'Click to enlarge ' + item.productName"
                @click="openPreview(item.imageUrl, item.productName)"
              >
                <img :src="item.imageUrl" alt="" />
              </div>

              <!-- ALWAYS-VISIBLE VIEW DETAILS OVERLAY -->
              <router-link :to="'/product/' + item.productId" class="view-overlay">
                View Details
              </router-link>
            </div>

            <h3>{{ item.productName }}</h3>
            <p class="price">$ {{ Number(item.price).toFixed(2) }}</p>

            <div class="actions">
              <el-button type="success" size="small" @click="cartStore.addToCart(item)">
                Add to Cart
              </el-button>
              <el-button
                :type="wishlistStore.has(item.productId) ? 'danger' : 'default'"
                size="small"
                @click="wishlistStore.toggle(item)"
              >
                {{ wishlistStore.has(item.productId) ? '♥' : '♡ Wishlist' }}
              </el-button>
            </div>
          </div>
        </div>
      </template>

      <!-- NO RESULTS -->
      <div v-else-if="query" class="no-results">
        <p>No plants matched <strong>"{{ query }}"</strong>.</p>
        <p class="hint">Try a different keyword, or explore the site:</p>
        <div class="no-results-links">
          <el-button @click="router.push('/products')">Browse All Products</el-button>
          <el-button @click="router.push('/shop')">Shop</el-button>
          <el-button @click="router.push('/ai')">AI Assistant</el-button>
          <el-button @click="router.push('/reviews')">Reviews</el-button>
        </div>
      </div>

      <!-- EMPTY PROMPT -->
      <div v-else class="no-results">
        <p>Start typing to search by name, category, care level, or description.</p>
      </div>
    </template>

    <!-- Fullscreen image preview -->
    <ImagePreview v-model:visible="previewVisible" :src="previewSrc" :alt="previewAlt" />

  </div>
</template>

<script setup>
  import { ref, computed, onMounted, watch } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import api from "@/api/axios";
  import { ElMessage } from "element-plus";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";
  import ImagePreview from "@/components/common/ImagePreview.vue";

  const route = useRoute();
  const router = useRouter();
  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();

  const loading = ref(true);
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

  const query = computed(() => String(route.query.q || "").trim());

  /* ==========================
     FILTER LOGIC
  ========================== */

  const results = computed(() => {
    const q = query.value.toLowerCase();
    if (!q) return [];

    // Each word of the query must appear somewhere in the product's
    // searchable text (name, description, category, care attributes).
    const terms = q.split(/\s+/).filter(Boolean);

    return products.value.filter((product) => {
      const categoryName = categoryNameOf(product.categoryId);

      const haystack = [
        product.productName,
        product.description,
        categoryName,
        product.sunlight,
        product.watering,
        product.difficulty
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      return terms.every((term) => haystack.includes(term));
    });
  });

  const categoryNameOf = (categoryId) => {
    const match = categories.value.find(
      (c) => String(c.categoryId) === String(categoryId)
    );
    return match ? match.categoryName : "";
  };

  /* ==========================
     DATA LOADING
  ========================== */

  const loadData = async () => {
    loading.value = true;
    try {
      const [productRes, categoryRes] = await Promise.all([
        api.get("/products"),
        api.get("/categories")
      ]);
      products.value = productRes.data;
      categories.value = categoryRes.data;
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load products.");
      products.value = [];
    } finally {
      loading.value = false;
    }
  };

  // Re-run the search when the query in the URL changes
  // (e.g. searching again while already on this page).
  watch(query, () => {
    if (query.value) {
      loadData();
    }
  });

  onMounted(loadData);
</script>

<style scoped>
  .search-results-wrapper {
    padding: 40px 60px;
    background: transparent;
    min-height: 70vh;
  }

  .search-header {
    text-align: center;
    margin-bottom: 25px;
  }

  .search-header h1 {
    color: #2E7D32;
    font-size: 34px;
  }

  .query-line {
    color: #666;
  }

  .loading {
    text-align: center;
    padding: 60px;
    color: #888;
  }

  .results-info {
    text-align: center;
    color: #666;
    margin-bottom: 20px;
  }

  .grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: 25px;
  }

  .card {
    background: white;
    padding: 15px;
    border-radius: 12px;
    text-align: center;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
    transition: 0.3s;
  }

  .card:hover {
    transform: translateY(-5px);
  }

  .image-container {
    position: relative;
    overflow: hidden;
    border-radius: 10px;
    margin-bottom: 10px;
  }

  .card img {
    width: 100%;
    height: 180px;
    object-fit: cover;
    border-radius: 10px;
    transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
    cursor: pointer;
  }

  .img-link {
    display: block;
    cursor: zoom-in;
  }

  /* Image grows larger & wider smoothly on hover */
  .image-container:hover img {
    transform: scale(1.15);
  }

  /* VIEW DETAILS OVERLAY */
  .view-overlay {
    position: absolute;
    top: 12px;
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

  .image-container:hover .view-overlay,
  .view-overlay:hover {
    background: rgba(21, 94, 25, 0.92);
    box-shadow: 0 4px 14px rgba(21, 94, 25, 0.4);
  }

  .view-overlay:focus-visible {
    outline: 3px solid #fff;
    outline-offset: 2px;
  }

  .price {
    font-weight: bold;
    color: #2E7D32;
    margin: 8px 0;
  }

  .actions {
    display: flex;
    gap: 10px;
    margin-top: 10px;
  }

  .actions .el-button,
  .actions a {
    flex: 1;
  }

  .actions a {
    text-decoration: none;
  }

  .no-results {
    text-align: center;
    padding: 60px 20px;
    color: #666;
  }

  .no-results p {
    margin: 8px 0;
  }

  .no-results .hint {
    color: #888;
  }

  .no-results-links {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    gap: 12px;
    flex-wrap: wrap;
  }
</style>
