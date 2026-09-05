<template>
  <div class="shop-wrapper">

    <!-- HEADER -->
    <div class="shop-header">
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
        placeholder="Search plants..."
        class="search"
        clearable
      />

      <!-- CATEGORY FILTER -->
      <el-select v-model="category" class="filter">
        <el-option label="All" value="all"/>
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

        <!-- IMAGE (hover to enlarge, click to view details) -->
        <div class="image-container">
          <router-link
            :to="'/product/' + item.productId"
            class="img-link"
            :aria-label="'View details for ' + item.productName"
          >
            <img :src="item.imageUrl" alt="" />
          </router-link>

          <!-- ALWAYS-VISIBLE VIEW DETAILS OVERLAY -->
          <router-link :to="'/product/' + item.productId" class="view-overlay">
            View Details
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
            {{ wishlistStore.has(item.productId) ? '♥' : '♡' }}
          </el-button>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted, watch } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();
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
    // Filter by the store's first category id as a quick filter
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
    padding: 40px 60px;
    background: transparent;
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

  .clear-filter {
    color: #2E7D32;
    font-weight: 600;
    text-decoration: underline;
  }

  /* STORES SECTION */
  .stores-section {
    margin-bottom: 40px;
  }

  .stores-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 25px;
  }

  .store-card {
    background: white;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 5px 15px rgba(0,0,0,0.08);
    transition: 0.3s;
    cursor: pointer;
  }

  .store-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 25px rgba(46,125,50,0.18);
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
    color: #2E7D32;
    font-size: 18px;
    margin-bottom: 6px;
  }

  .store-desc {
    color: #888;
    font-size: 14px;
    margin-bottom: 12px;
  }

  .store-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  .store-tag {
    background: #e8f5e9;
    color: #2E7D32;
    padding: 3px 10px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 600;
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

  /* IMAGE */
  .image-container {
    position: relative;
    overflow: hidden;
    border-radius: 10px;
    margin-bottom: 10px;
  }

  .img-link {
    display: block;
  }

  .image-container img {
    width: 100%;
    height: 180px;
    object-fit: cover;
    border-radius: 10px;
    transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1);
    cursor: pointer;
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

  /* PRICE */
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

  .actions .el-button {
    flex: 1;
  }

</style>
