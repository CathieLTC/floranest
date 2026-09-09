<template>
  <div class="tp-wrapper">

    <!-- PAGE HERO -->
    <section class="tp-hero">
      <div class="tp-hero-inner">
        <span class="tp-eyebrow">Garden Essentials</span>
        <h1>Gardening Tools &amp; Pots</h1>
        <p>Everything you need to plant, prune, water and repot — shop tools and planters that keep your green family thriving.</p>
        <div class="tp-hero-perks">
          <span class="perk" v-for="perk in perks" :key="perk">
            <span class="perk-dot"></span>{{ perk }}
          </span>
        </div>
      </div>
    </section>

    <div class="tp-content">

      <!-- FILTER BAR -->
      <div class="toolbar">
        <el-input
          v-model="search"
          placeholder="Search tools &amp; pots…"
          class="search"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <div class="chips" role="tablist" aria-label="Filter by category">
          <button
            v-for="tab in tabs"
            :key="tab.value"
            class="chip"
            :class="{ active: activeTab === tab.value }"
            @click="activeTab = tab.value"
          >
            {{ tab.label }}
            <span class="chip-count">{{ tab.count }}</span>
          </button>
        </div>

        <el-select v-model="sort" class="sort" aria-label="Sort products">
          <el-option label="Sort: Default" value="default" />
          <el-option label="Price Low → High" value="low" />
          <el-option label="Price High → Low" value="high" />
        </el-select>
      </div>

      <!-- PRODUCTS GRID -->
      <p v-if="filteredProducts.length" class="result-count">
        Showing {{ filteredProducts.length }}
        {{ filteredProducts.length === 1 ? 'item' : 'items' }}
      </p>

      <div v-if="filteredProducts.length" class="grid">
        <article class="card" v-for="item in filteredProducts" :key="item.productId">
          <div class="media">
            <div
              class="image-link"
              :aria-label="'Click to enlarge ' + item.productName"
              @click="openPreview(item.imageUrl, item.productName)"
            >
              <img :src="item.imageUrl" alt="" @error="onImgError" />
            </div>
            <router-link :to="'/product/' + item.productId" class="view-overlay">
              View Details
            </router-link>
            <span class="cat-badge">{{ catName(item.categoryId) }}</span>
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
        <div class="empty-icon">🧰</div>
        <h3>No items found</h3>
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

  /* ── Image preview state ── */
  const previewVisible = ref(false);
  const previewSrc = ref("");
  const previewAlt = ref("");
  const openPreview = (src, alt = "") => {
    previewSrc.value = src;
    previewAlt.value = alt;
    previewVisible.value = true;
  };

  /* ── State ── */
  const search = ref("");
  const activeTab = ref("all");
  const sort = ref("default");

  const products = ref([]);
  const categories = ref([]);

  const PLACEHOLDER_IMG =
    "data:image/svg+xml;charset=UTF-8," +
    encodeURIComponent(
      `<svg xmlns='http://www.w3.org/2000/svg' width='400' height='400'>
         <rect width='100%' height='100%' fill='#edf7f0'/>
         <text x='50%' y='50%' font-size='70' text-anchor='middle' dominant-baseline='middle'>🪴</text>
       </svg>`
    );

  const perks = ["Free shipping over $50", "Quality-checked gear", "Easy 30-day returns"];

  const formatPrice = (p) => Number(p).toFixed(2);

  const onImgError = (e) => {
    e.target.src = PLACEHOLDER_IMG;
    e.target.onerror = null;
  };

  const isToolCat = (name = "") => /tool|prun|shear|water/i.test(name);
  const isPotCat = (name = "") => /pot|planter|terracotta|ceramic/i.test(name);

  const toolsCatId = computed(() => {
    const c = categories.value.find((x) => isToolCat(x.categoryName));
    return c ? c.categoryId : null;
  });

  const potsCatId = computed(() => {
    const c = categories.value.find((x) => isPotCat(x.categoryName));
    return c ? c.categoryId : null;
  });

  const tabs = computed(() => {
    const list = [{ label: "All", value: "all", count: toolsPotsProducts.value.length }];
    if (toolsCatId.value) list.push({ label: "Gardening Tools", value: String(toolsCatId.value), count: byCategory(toolsCatId.value).length });
    if (potsCatId.value) list.push({ label: "Pots & Planters", value: String(potsCatId.value), count: byCategory(potsCatId.value).length });
    return list;
  });

  const toolsPotsProducts = computed(() =>
    products.value.filter(
      (p) => p.categoryId === toolsCatId.value || p.categoryId === potsCatId.value
    )
  );

  const byCategory = (catId) =>
    products.value.filter((p) => p.categoryId === catId);

  const catName = (catId) => {
    const c = categories.value.find((x) => x.categoryId === catId);
    return c ? c.categoryName : "";
  };

  const filteredProducts = computed(() => {
    let result = toolsPotsProducts.value.filter((p) => {
      const matchSearch = p.productName.toLowerCase().includes(search.value.toLowerCase());
      const matchTab = activeTab.value === "all" || p.categoryId == activeTab.value;
      return matchSearch && matchTab;
    });

    if (sort.value === "low") result.sort((a, b) => a.price - b.price);
    if (sort.value === "high") result.sort((a, b) => b.price - a.price);
    return result;
  });

  const loadData = async () => {
    try {
      const [productResponse, categoryResponse] = await Promise.all([
        api.get("/products"),
        api.get("/categories"),
      ]);
      products.value = productResponse.data;
      categories.value = categoryResponse.data;
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load products.");
    }
  };

  onMounted(loadData);
</script>

<style scoped>
  .tp-wrapper {
    min-height: 100vh;
    padding-bottom: 40px;
  }

  /* ===== HERO ===== */
  .tp-hero {
    background: linear-gradient(135deg, var(--fn-green-800), var(--fn-green-600));
    padding: 56px 24px 64px;
    text-align: center;
    position: relative;
    overflow: hidden;
  }

  .tp-hero::before,
  .tp-hero::after {
    content: "";
    position: absolute;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.12), transparent 65%);
  }

  .tp-hero::before {
    width: 340px;
    height: 340px;
    top: -120px;
    left: -80px;
  }

  .tp-hero::after {
    width: 420px;
    height: 420px;
    bottom: -180px;
    right: -100px;
  }

  .tp-hero-inner {
    max-width: 760px;
    margin: 0 auto;
    position: relative;
    z-index: 1;
  }

  .tp-eyebrow {
    display: inline-block;
    font-size: 12px;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.12em;
    color: var(--fn-green-200);
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.16);
    padding: 6px 16px;
    border-radius: var(--fn-radius-pill);
    margin-bottom: 16px;
  }

  .tp-hero h1 {
    color: #fff;
    font-size: clamp(1.9rem, 4.5vw, 2.9rem);
    font-weight: 800;
    letter-spacing: -0.03em;
    margin: 0 0 14px;
  }

  .tp-hero p {
    color: rgba(255, 255, 255, 0.82);
    font-size: 0.98rem;
    line-height: 1.7;
    margin: 0 auto 22px;
    max-width: 600px;
  }

  .tp-hero-perks {
    display: flex;
    justify-content: center;
    gap: 20px;
    flex-wrap: wrap;
  }

  .perk {
    display: inline-flex;
    align-items: center;
    gap: 7px;
    font-size: 13px;
    font-weight: 600;
    color: #fff;
  }

  .perk-dot {
    width: 7px;
    height: 7px;
    border-radius: 50%;
    background: var(--fn-accent-light);
  }

  /* ===== CONTENT ===== */
  .tp-content {
    max-width: 1280px;
    margin: -34px auto 0;
    padding: 0 24px;
    position: relative;
    z-index: 2;
  }

  /* TOOLBAR */
  .toolbar {
    display: flex;
    align-items: center;
    gap: 14px;
    flex-wrap: wrap;
    background: #fff;
    border: 1px solid var(--fn-border);
    border-radius: var(--fn-radius-md);
    box-shadow: var(--fn-shadow-md);
    padding: 16px;
    margin-bottom: 26px;
  }

  .search {
    width: min(260px, 100%);
    flex: 1 1 220px;
  }

  .chips {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    flex: 1;
    justify-content: center;
  }

  .chip {
    display: inline-flex;
    align-items: center;
    gap: 7px;
    border: 1px solid var(--fn-border-2);
    background: var(--fn-surface-2);
    color: var(--fn-text-2);
    font-size: 13px;
    font-weight: 600;
    padding: 7px 14px;
    border-radius: var(--fn-radius-pill);
    cursor: pointer;
    transition: all var(--fn-t-fast);
  }

  .chip:hover {
    border-color: var(--fn-green-400);
    color: var(--fn-green-700);
  }

  .chip.active {
    background: var(--fn-green-600);
    border-color: var(--fn-green-600);
    color: #fff;
  }

  .chip-count {
    font-size: 11px;
    font-weight: 700;
    background: rgba(0, 0, 0, 0.08);
    border-radius: var(--fn-radius-pill);
    padding: 1px 8px;
  }

  .chip.active .chip-count {
    background: rgba(255, 255, 255, 0.22);
  }

  .sort {
    width: 180px;
  }

  /* RESULT COUNT */
  .result-count {
    text-align: center;
    color: var(--fn-text-3);
    font-size: 13px;
    margin: 6px 0 22px;
  }

  /* GRID */
  .grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
    gap: 24px;
  }

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

  /* MEDIA */
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
  }

  .view-overlay:focus-visible {
    outline: 3px solid #fff;
    outline-offset: 2px;
  }

  .cat-badge {
    position: absolute;
    bottom: 12px;
    left: 12px;
    z-index: 2;
    font-size: 11px;
    font-weight: 700;
    background: rgba(255, 255, 255, 0.92);
    color: var(--fn-green-700);
    padding: 4px 10px;
    border-radius: var(--fn-radius-pill);
    box-shadow: var(--fn-shadow-sm);
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

  /* EMPTY */
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
    font-size: 38px;
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

  /* RESPONSIVE */
  @media (max-width: 768px) {
    .tp-hero {
      padding: 44px 16px 58px;
    }
    .tp-content {
      padding: 0 16px;
    }
    .chips {
      order: 3;
      flex-basis: 100%;
      justify-content: flex-start;
    }
    .sort {
      width: 100%;
    }
  }
</style>
