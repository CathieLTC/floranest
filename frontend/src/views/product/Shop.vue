<template>
  <div class="shop-wrapper">

    <!-- HEADER -->
    <div class="shop-header">
      <span class="header-eyebrow">Explore</span>
      <h1>{{ activeCategoryName ? activeCategoryName : 'Plant Stores' }}</h1>
      <p v-if="activeCategoryName">
        Showing plants in "{{ activeCategoryName }}"
        <a href="#" class="clear-filter" @click.prevent="clearCategory">(clear filter)</a>
      </p>
      <p v-else>Browse plants from different specialised stores</p>
    </div>

    <!-- STORES SECTION (visible when no category filter is active) -->
    <div class="stores-section" v-if="category === 'all'">
      <div class="stores-grid">
        <div
          class="store-card"
          v-for="store in stores"
          :key="store.name"
          @click="filterByStore(store)"
        >
          <div class="store-image-wrapper">
            <img :src="store.image" :alt="store.name" class="store-image" />
          </div>
          <div class="store-info">
            <h3>{{ store.name }}</h3>
            <p class="store-desc">{{ store.tagline }}</p>
            <div class="store-tags">
              <span class="store-tag" v-for="tag in store.tags" :key="tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- FILTER BAR -->
    <div class="toolbar">
      <!-- SEARCH -->
      <el-input
        v-model="search"
        placeholder="Search plants…"
        class="search"
        clearable
      />

      <!-- CATEGORY FILTER -->
      <el-select v-model="category" class="filter" placeholder="All Categories">
        <el-option label="All Categories" value="all"/>
        <el-option v-for="c in categories" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId"/>
      </el-select>

      <!-- SORT -->
      <el-select v-model="sort" class="filter">
        <el-option label="Default" value="default" />
        <el-option label="Price Low → High" value="low" />
        <el-option label="Price High → Low" value="high" />
      </el-select>
    </div>

    <!-- PRODUCTS GRID -->
    <div class="grid">
      <div class="card" v-for="(item, i) in filteredProducts" :key="i">

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

        <div class="card-body">
          <h3>{{ item.productName }}</h3>
          <p class="price">${{ item.price }}</p>

          <div class="actions">
            <el-button type="success" @click="cartStore.addToCart(item)">Add to Cart</el-button>
            <el-button
              :type="wishlistStore.has(item.productId) ? 'danger' : 'default'"
              size="default"
              class="wish-btn"
              @click="wishlistStore.toggle(item)"
            >
              {{ wishlistStore.has(item.productId) ? '♥' : '♡' }}
            </el-button>
          </div>
        </div>

      </div>
    </div>

    <!-- Fullscreen image preview -->
    <ImagePreview v-model:visible="previewVisible" :src="previewSrc" :alt="previewAlt" />

  </div>
</template>

<script setup>
  import { ref, computed, onMounted, watch } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";
  import ImagePreview from "@/components/common/ImagePreview.vue";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();

  /* ── Image preview state ── */
  const previewVisible = ref(false);
  const previewSrc = ref("");
  const previewAlt = ref("");

  const openPreview = (src, alt = "") => {
    previewSrc.value = src;
    previewAlt.value = alt;
    previewVisible.value = true;
  };
  const route = useRoute();
  const router = useRouter();

  const search = ref("");
  const category = ref(route.query.category ? String(route.query.category) : "all");
  const sort = ref("default");

  const products = ref([]);
  const categories = ref([]);

  /* Mock store data (can be replaced with a real stores API later) */
  const stores = [
    {
      name: "Indoor Oasis",
      tagline: "Specialists in houseplants for every room",
      image: "https://images.unsplash.com/photo-1614594975525-e45190c55d0b",
      categoryIds: [1, 5, 7],
      tags: ["Indoor", "Air Purifying", "Hanging"]
    },
    {
      name: "Succulent Studio",
      tagline: "Drought-tolerant beauties & cacti",
      image: "https://images.unsplash.com/photo-1459156212016-c812468e2115?w=400",
      categoryIds: [3],
      tags: ["Succulents", "Cacti", "Low-water"]
    },
    {
      name: "Bloom & Blossom",
      tagline: "Flowering plants that brighten any space",
      image: "https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=400",
      categoryIds: [4],
      tags: ["Flowering", "Colorful", "Decorative"]
    },
    {
      name: "The Herb Garden",
      tagline: "Fresh herbs for cooking and wellness",
      image: "https://images.unsplash.com/photo-1512428813834-c702c7702b78?w=400",
      categoryIds: [6],
      tags: ["Herbs", "Edible", "Medicinal"]
    },
    {
      name: "Outdoor Greens",
      tagline: "Garden and balcony plants for the outdoors",
      image: "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?w=400",
      categoryIds: [2],
      tags: ["Outdoor", "Garden", "Balcony"]
    },
    {
      name: "Bonsai Atelier",
      tagline: "Hand-crafted miniature trees for collectors",
      image: "https://images.unsplash.com/photo-1513836279014-a89f7a76ae86?w=400",
      categoryIds: [8],
      tags: ["Bonsai", "Miniature", "Ornamental"]
    }
  ];

  const filterByStore = (store) => {
    if (store.categoryIds.length > 0) {
      category.value = String(store.categoryIds[0]);
    }
  };

  const activeCategoryName = computed(() => {
    if (category.value === "all") return "";
    const match = categories.value.find(c => String(c.categoryId) === String(category.value));
    return match ? match.categoryName : "";
  });

  const clearCategory = () => {
    category.value = "all";
    router.replace({ path: "/shop" });
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

  watch(
    () => route.query.category,
    (newVal) => {
      category.value = newVal ? String(newVal) : "all";
    }
  );

  watch(category, (newVal) => {
    const query = newVal === "all" ? {} : { category: newVal };
    router.replace({ path: "/shop", query });
  });

  const filteredProducts = computed(() => {

    let result = products.value.filter(product => {

      const matchSearch =
        product.productName
          .toLowerCase()
          .includes(search.value.toLowerCase());

      const matchCategory =
        category.value === "all" ||
        product.categoryId == category.value;

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
    padding: 44px 24px;
    min-height: 100vh;
  }

  /* HEADER */
  .shop-header {
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

  .shop-header h1 {
    color: var(--fn-ink);
    font-size: clamp(1.8rem, 4vw, 2.5rem);
    font-weight: 800;
    margin-bottom: 8px;
    letter-spacing: -0.03em;
  }

  .shop-header p {
    color: var(--fn-text-3);
    font-size: 0.95rem;
  }

  .clear-filter {
    color: var(--fn-green-600);
    font-weight: 600;
    text-decoration: underline;
  }

  /* STORES SECTION */
  .stores-section {
    max-width: 1280px;
    margin: 0 auto 44px;
  }

  .stores-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 24px;
  }

  .store-card {
    background: #fff;
    border-radius: var(--fn-radius-md);
    overflow: hidden;
    border: 1px solid var(--fn-border);
    box-shadow: var(--fn-shadow-sm);
    transition: all var(--fn-t);
    cursor: pointer;
  }

  .store-card:hover {
    transform: translateY(-5px);
    border-color: var(--fn-green-200);
    box-shadow: var(--fn-shadow-lg);
  }

  .store-image-wrapper {
    overflow: hidden;
    height: 200px;
  }

  .store-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.35s ease;
  }

  .store-card:hover .store-image {
    transform: scale(1.08);
  }

  .store-info {
    padding: 20px;
  }

  .store-info h3 {
    color: var(--fn-ink);
    font-size: 1.05rem;
    margin-bottom: 6px;
  }

  .store-desc {
    color: var(--fn-text-3);
    font-size: 13px;
    margin-bottom: 12px;
  }

  .store-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  .store-tag {
    background: var(--fn-green-50);
    color: var(--fn-green-700);
    padding: 3px 10px;
    border-radius: var(--fn-radius-pill);
    font-size: 12px;
    font-weight: 600;
  }

  /* TOOLBAR */
  .toolbar {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-bottom: 30px;
    flex-wrap: wrap;
    max-width: 1280px;
    margin-left: auto;
    margin-right: auto;
  }

  .search {
    width: min(300px, 100%);
  }

  .filter {
    width: 200px;
  }

  /* GRID */
  .grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 24px;
    max-width: 1280px;
    margin: 0 auto;
  }

  /* CARD */
  .card {
    background: #fff;
    border-radius: var(--fn-radius-md);
    overflow: hidden;
    border: 1px solid var(--fn-border);
    box-shadow: var(--fn-shadow-sm);
    transition: all var(--fn-t);
  }

  .card:hover {
    transform: translateY(-5px);
    border-color: var(--fn-green-200);
    box-shadow: var(--fn-shadow-lg);
  }

  /* IMAGE */
  .image-container {
    position: relative;
    overflow: hidden;
  }

  .img-link {
    display: block;
    cursor: zoom-in;
  }

  .image-container img {
    width: 100%;
    height: 180px;
    object-fit: cover;
    transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
  }

  .image-container:hover img {
    transform: scale(1.12);
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

  .image-container:hover .view-overlay,
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
    text-align: center;
  }

  .card-body h3 {
    font-size: 14px;
    font-weight: 600;
    color: var(--fn-ink);
    margin-bottom: 4px;
  }

  .price {
    font-size: 18px;
    font-weight: 800;
    color: var(--fn-green-600);
    margin: 6px 0 14px;
  }

  .actions {
    display: flex;
    gap: 10px;
  }

  .actions .el-button {
    flex: 1;
    margin-left: 0;
  }

  .wish-btn {
    flex: none !important;
    width: 42px;
    padding: 0;
    font-size: 17px;
  }
</style>
