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
            <img :src="item.imageUrl" :alt="item.productName" />
            <h3>{{ item.productName }}</h3>
            <p class="price">$ {{ Number(item.price).toFixed(2) }}</p>

            <div class="actions">
              <el-button type="success" size="small" @click="cartStore.addToCart(item)">
                Add to Cart
              </el-button>
              <router-link :to="'/product/' + item.productId">
                <el-button size="small">View</el-button>
              </router-link>
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
          <el-button @click="router.push('/plant-care')">Plant Care</el-button>
        </div>
      </div>

      <!-- EMPTY PROMPT -->
      <div v-else class="no-results">
        <p>Start typing to search by name, category, care level, or description.</p>
      </div>
    </template>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted, watch } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";

  const route = useRoute();
  const router = useRouter();
  const cartStore = useCartStore();

  const loading = ref(true);
  const products = ref([]);
  const categories = ref([]);

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
    background: #f4fff6;
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

  .card img {
    width: 100%;
    height: 180px;
    object-fit: cover;
    border-radius: 10px;
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
