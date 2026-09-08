<template>
  <div class="detail-wrapper">

    <div class="container">

      <!-- PRODUCT HERO -->
      <div v-if="product.productId" class="product-hero">

        <!-- IMAGE -->
        <div class="image-panel">
          <img
            :src="product.imageUrl"
            :alt="product.productName"
            class="main-image"
            @error="onImgError"
            @click="openPreview(product.imageUrl, product.productName)"
          />
        </div>

        <!-- INFO -->
        <div class="info-panel">

          <h1 class="title">{{ product.productName }}</h1>

          <div class="meta-line">
            <el-tag :type="difficultyType" size="small" round>{{ product.difficulty }}</el-tag>
            <span class="category">{{ product.categoryName }}</span>
          </div>

          <p class="desc">{{ product.description }}</p>

          <div class="price-row">
            <span class="price-label">Price</span>
            <span class="price">${{ formatPrice(product.price) }}</span>
          </div>

          <div class="spec-grid">
            <div class="spec">
              <span class="spec-icon">☀️</span>
              <div class="spec-text">
                <span class="spec-label">Sunlight</span>
                <span class="spec-value">{{ product.sunlight || '—' }}</span>
              </div>
            </div>
            <div class="spec">
              <span class="spec-icon">💧</span>
              <div class="spec-text">
                <span class="spec-label">Watering</span>
                <span class="spec-value">{{ product.watering || '—' }}</span>
              </div>
            </div>
            <div class="spec">
              <span class="spec-icon">🌡️</span>
              <div class="spec-text">
                <span class="spec-label">Temperature</span>
                <span class="spec-value">{{ product.temperature || '—' }}</span>
              </div>
            </div>
          </div>

          <div class="action-row">
            <el-button type="success" size="large" class="primary-btn" @click="cartStore.addToCart(product)">
              Add to Cart
            </el-button>
            <el-button type="success" plain size="large" class="buy-btn" @click="buyNow">
              Buy Now
            </el-button>
            <el-button
              :type="wishlistStore.has(product.productId) ? 'danger' : 'default'"
              class="wish-btn"
              @click="wishlistStore.toggle(product)"
            >
              {{ wishlistStore.has(product.productId) ? '♥' : '♡' }}
            </el-button>
          </div>

        </div>

      </div>

      <!-- LOADING -->
      <div v-else class="loading-state">
        <div class="spinner"></div>
        <p>Loading product…</p>
      </div>

      <!-- CARE GUIDE -->
      <section v-if="product.productId" class="section care-guide">
        <h2 class="section-title">Complete Care Guide</h2>
        <p class="section-subtitle">Everything you need to know to keep your {{ product.productName }} thriving.</p>

        <div class="guide-grid">
          <div class="guide-card">
            <div class="guide-icon">☀️</div>
            <h3>Sunlight</h3>
            <p>{{ careGuide.sunlight }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">💧</div>
            <h3>Watering</h3>
            <p>{{ careGuide.watering }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">🪴</div>
            <h3>Soil</h3>
            <p>{{ careGuide.soil }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">🌡️</div>
            <h3>Temperature</h3>
            <p>{{ careGuide.temperature }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">🍃</div>
            <h3>Humidity</h3>
            <p>{{ careGuide.humidity }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">🌿</div>
            <h3>Fertilizing</h3>
            <p>{{ careGuide.fertilizing }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">✂️</div>
            <h3>Pruning</h3>
            <p>{{ careGuide.pruning }}</p>
          </div>
          <div class="guide-card">
            <div class="guide-icon">🔄</div>
            <h3>Repotting</h3>
            <p>{{ careGuide.repotting }}</p>
          </div>
        </div>
      </section>

      <!-- COMMON PROBLEMS -->
      <section v-if="product.productId" class="section problems">
        <div class="problems-inner">
          <span class="problems-icon">🩺</span>
          <div>
            <h3>Common Problems</h3>
            <p>{{ careGuide.problems }}</p>
          </div>
        </div>
      </section>

      <!-- TUTORIAL VIDEO -->
      <section v-if="product.videoUrl" class="section video-section">
        <h2 class="section-title">How to Care for Your {{ product.productName }}</h2>
        <p class="section-subtitle">A quick beginner's guide to keeping this plant happy and healthy.</p>
        <div class="video-wrapper">
          <iframe
            v-if="embedUrl"
            :src="embedUrl"
            class="tutorial-video"
            frameborder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen
          ></iframe>
        </div>
      </section>

      <!-- QUICK TIPS -->
      <section v-if="product.productId" class="section quick-tips">
        <h3 class="tips-title">🌱 Quick Tips</h3>
        <ul>
          <li>Check soil moisture before watering — stick your finger 2 inches deep.</li>
          <li>Rotate the plant weekly for even growth.</li>
          <li>Remove yellow or dead leaves promptly to prevent disease.</li>
          <li>Keep leaves clean for better light absorption.</li>
          <li>Avoid placing near cold drafts or heating vents.</li>
        </ul>
      </section>

    </div>

    <!-- Fullscreen image preview -->
    <ImagePreview v-model:visible="previewVisible" :src="previewSrc" :alt="previewAlt" />

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";
  import ImagePreview from "@/components/common/ImagePreview.vue";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();
  const route = useRoute();
  const router = useRouter();
  const product = ref({});

  const PLACEHOLDER_IMG =
    "data:image/svg+xml;charset=UTF-8," +
    encodeURIComponent(
      `<svg xmlns='http://www.w3.org/2000/svg' width='800' height='800'>
         <rect width='100%' height='100%' fill='#e8f5e9'/>
         <text x='50%' y='50%' font-size='120' text-anchor='middle' dominant-baseline='middle'>🪴</text>
       </svg>`
    );

  const formatPrice = (p) => Number(p).toFixed(2);

  const onImgError = (e) => {
    e.target.src = PLACEHOLDER_IMG;
    e.target.onerror = null;
  };

  // Extract a YouTube embed URL from various YouTube link formats
  const embedUrl = computed(() => {
    const url = product.value.videoUrl;
    if (!url) return "";

    const shortMatch = url.match(/youtu\.be\/([a-zA-Z0-9_-]{11})/);
    if (shortMatch) return `https://www.youtube.com/embed/${shortMatch[1]}`;

    const watchMatch = url.match(/[?&]v=([a-zA-Z0-9_-]{11})/);
    if (watchMatch) return `https://www.youtube.com/embed/${watchMatch[1]}`;

    const embedMatch = url.match(/youtube\.com\/embed\/([a-zA-Z0-9_-]{11})/);
    if (embedMatch) return `https://www.youtube.com/embed/${embedMatch[1]}`;

    return url;
  });

  const difficultyType = computed(() => {
    const d = (product.value.difficulty || "").toLowerCase();
    if (d === "easy") return "success";
    if (d === "medium") return "warning";
    if (d === "hard") return "danger";
    return "info";
  });

  // Generate care guide based on product attributes
  const careGuide = computed(() => {
    const p = product.value;
    const name = p.productName || "this plant";
    const isSucculent = (p.categoryName || "").toLowerCase().includes("succulent") ||
                        (p.productName || "").toLowerCase().match(/aloe|jade|cactus|pearls|burro/);
    const isFlowering = (p.categoryName || "").toLowerCase().includes("flower") ||
                        (p.productName || "").toLowerCase().match(/orchid|peace lily|anthurium|bird of paradise/);

    return {
      sunlight: p.sunlight
        ? `${p.sunlight} light is ideal. Place near a window that matches this requirement. Rotate the pot a quarter turn each week so all sides receive even light.`
        : `Place in a spot with bright, indirect light. Rotate weekly for even growth.`,
      watering: p.watering
        ? `Water ${p.watering.toLowerCase()}. Always check the top 2 inches of soil — if it feels dry, it's time to water.${isSucculent ? " For succulents, it's better to underwater than overwater." : ""} Water thoroughly until it drains from the bottom, then empty the saucer.`
        : `Water when the top 2 inches of soil feels dry. Don't let the plant sit in standing water.`,
      soil: isSucculent
        ? `Use a well-draining cactus or succulent mix. Add perlite or coarse sand for extra drainage. Avoid regular potting soil — it holds too much moisture.`
        : `Use a high-quality, well-draining potting mix. A blend of peat, perlite, and compost works well. Ensure the pot has drainage holes.`,
      temperature: p.temperature
        ? `Ideal range: ${p.temperature}. Keep away from cold drafts, air conditioning vents, and radiators. Sudden temperature changes cause stress.`
        : `Most houseplants prefer 15-27°C. Keep away from cold drafts and heat sources.`,
      humidity: isSucculent
        ? `Low humidity is fine — succulents are adapted to dry environments. No misting needed.`
        : `Moderate to high humidity (40-60%) is preferred. Group plants together, use a pebble tray, or mist occasionally to increase humidity.`,
      fertilizing: isFlowering
        ? `Feed every 2-4 weeks during the growing season (spring and summer) with a bloom-boosting fertilizer (higher phosphorus). Stop feeding in autumn and winter when growth slows.`
        : `Feed every 2-4 weeks during the growing season (spring and summer) with a balanced liquid fertilizer diluted to half strength. Stop feeding in autumn and winter when growth slows.`,
      pruning: `Remove yellow, brown, or damaged leaves promptly with clean scissors. Prune leggy stems to encourage bushier growth. Always cut just above a leaf node.`,
      repotting: `Repot every 1-2 years in spring when roots outgrow the pot. Choose a pot 1-2 inches wider. Signs it's time: roots circling the bottom or growing from drainage holes.`,
      problems: `Common issues for ${name}: yellow leaves usually mean overwatering. Brown crispy edges indicate low humidity or underwatering. Drooping can be both over or underwatering — check the soil. Watch for common pests like spider mites, mealybugs, and fungus gnats. Treat with neem oil or insecticidal soap at first sign.`
    };
  });

  const buyNow = () => {
    cartStore.addToCart(product.value);
    setTimeout(() => {
      router.push("/payment");
    }, 500);
  };

  const loadProduct = async () => {
    try {
      const response = await api.get(`/products/${route.params.id}`);
      product.value = response.data;
    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load product.");
    }
  };

  onMounted(loadProduct);

  /* ── Image preview state ── */
  const previewVisible = ref(false);
  const previewSrc = ref("");
  const previewAlt = ref("");

  const openPreview = (src, alt = "") => {
    previewSrc.value = src;
    previewAlt.value = alt;
    previewVisible.value = true;
  };
</script>

<style scoped>

/* WRAPPER */
.detail-wrapper {
  padding: 44px clamp(16px, 5vw, 64px);
  background: transparent;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

/* ======================
   PRODUCT HERO
====================== */
.product-hero {
  display: grid;
  grid-template-columns: 1.05fr 1fr;
  gap: 40px;
  align-items: start;
  margin-bottom: 52px;
}

/* IMAGE PANEL */
.image-panel {
  background: #fff;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.07);
}

.main-image {
  width: 100%;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  border-radius: 12px;
  display: block;
  cursor: zoom-in;
}

/* INFO PANEL */
.title {
  color: #1f2937;
  font-size: 30px;
  font-weight: 700;
  margin: 4px 0 12px;
  line-height: 1.2;
  letter-spacing: -0.5px;
}

.meta-line {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}

.meta-line .el-tag {
  margin: 0;
}

.category {
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
}

.desc {
  color: #4b5563;
  font-size: 15px;
  line-height: 1.7;
  margin: 0 0 24px;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 12px;
  padding-bottom: 22px;
  border-bottom: 1px solid #eef2ee;
  margin-bottom: 24px;
}

.price-label {
  color: #9ca3af;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.price {
  color: #2E7D32;
  font-size: 32px;
  font-weight: 800;
}

/* SPECS */
.spec-grid {
  display: grid;
  gap: 12px;
}

.spec {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  background: #f8faf8;
  border: 1px solid #eef2ee;
  border-radius: 12px;
}

.spec-icon {
  font-size: 22px;
  line-height: 1;
}

.spec-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.spec-label {
  font-size: 12px;
  color: #9ca3af;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.spec-value {
  font-size: 15px;
  color: #1f2937;
  font-weight: 600;
}

/* ACTION ROW */
.action-row {
  display: flex;
  gap: 12px;
  margin-top: 28px;
  align-items: stretch;
}

.action-row .el-button {
  margin-left: 0;
}

.primary-btn {
  flex: 1.4;
  font-weight: 600;
}

.buy-btn {
  flex: 1;
  font-weight: 600;
}

.wish-btn {
  width: 48px;
  flex: none;
  padding: 0;
  font-size: 18px;
}

/* LOADING */
.loading-state {
  text-align: center;
  padding: 120px 20px;
  color: #6b7280;
}

.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto 16px;
  border: 4px solid #d7ecd9;
  border-top-color: #2E7D32;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ======================
   SECTIONS
====================== */
.section {
  background: #fff;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  margin-bottom: 32px;
}

.section-title {
  color: #1f2937;
  font-size: 24px;
  font-weight: 700;
  text-align: center;
  margin: 0 0 6px;
}

.section-subtitle {
  color: #6b7280;
  text-align: center;
  font-size: 14px;
  margin: 0 0 30px;
}

/* CARE GUIDE */
.guide-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.guide-card {
  background: #f8faf8;
  border: 1px solid #eef2ee;
  border-top: 3px solid #2E7D32;
  padding: 22px;
  border-radius: 14px;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.guide-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(46, 125, 50, 0.1);
}

.guide-icon {
  font-size: 30px;
  line-height: 1;
  margin-bottom: 12px;
}

.guide-card h3 {
  color: #1f2937;
  font-size: 16px;
  font-weight: 700;
  margin: 0 0 8px;
}

.guide-card p {
  color: #4b5563;
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

/* COMMON PROBLEMS */
.problems {
  background: #fffaf3;
  border: 1px solid #f3e3c8;
}

.problems-inner {
  display: flex;
  gap: 18px;
  align-items: flex-start;
}

.problems-icon {
  font-size: 34px;
  line-height: 1;
}

.problems-inner h3 {
  color: #92400e;
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 6px;
}

.problems-inner p {
  color: #6b4a1f;
  font-size: 14px;
  line-height: 1.7;
  margin: 0;
}

/* VIDEO */
.video-wrapper {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
  aspect-ratio: 16 / 9;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  background: #000;
}

.tutorial-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 0;
}

/* QUICK TIPS */
.quick-tips {
  background: #e8f5e9;
}

.tips-title {
  color: #2E7D32;
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 14px;
}

.quick-tips ul {
  margin: 0;
  padding: 0;
  list-style: none;
  display: grid;
  gap: 10px;
}

.quick-tips li {
  position: relative;
  color: #3f5a41;
  font-size: 14px;
  line-height: 1.6;
  padding-left: 26px;
}

.quick-tips li::before {
  content: "✓";
  position: absolute;
  left: 0;
  top: 0;
  color: #2E7D32;
  font-weight: 700;
}

/* ======================
   RESPONSIVE
====================== */
@media (max-width: 900px) {
  .product-hero {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .main-image {
    max-width: 460px;
    margin: 0 auto;
  }

  .section {
    padding: 28px;
  }
}

@media (max-width: 600px) {
  .detail-wrapper {
    padding: 28px 16px;
  }

  .title {
    font-size: 26px;
  }

  .action-row {
    flex-wrap: wrap;
  }

  .action-row .el-button {
    flex: 1 1 100%;
  }

  .action-row .wish-btn {
    flex: none;
    width: 48px;
  }

  .guide-grid {
    grid-template-columns: 1fr;
  }
}

</style>
