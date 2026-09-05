<template>
  <div class="detail-wrapper" v-if="product">

    <div class="detail-layout">

      <!-- PRODUCT CARD (LEFT) -->
      <div class="detail-card">

        <!-- IMAGE -->
        <div class="image-section">
          <img :src="product.imageUrl" :alt="product.productName" />
        </div>

        <!-- INFO -->
        <div class="info-section">

          <h1>{{ product.productName }}</h1>
          <p class="desc">{{ product.description }}</p>

          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">Price</span>
              <span class="info-value price">${{ product.price }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Category</span>
              <span class="info-value">{{ product.categoryName }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Difficulty</span>
              <span class="info-value">
                <el-tag :type="difficultyType" size="small">{{ product.difficulty }}</el-tag>
              </span>
            </div>
            <div class="info-item">
              <span class="info-label">Sunlight</span>
              <span class="info-value">{{ product.sunlight }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Watering</span>
              <span class="info-value">{{ product.watering }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Temperature</span>
              <span class="info-value">{{ product.temperature }}</span>
            </div>
          </div>

          <!-- BUTTONS — side by side -->
          <div class="action-buttons">
            <el-button type="success" size="large" @click="cartStore.addToCart(product)">
              Add to Cart
            </el-button>
            <el-button type="warning" size="large" @click="buyNow">
              Buy Now
            </el-button>
            <el-button
              :type="wishlistStore.has(product.productId) ? 'danger' : 'default'"
              size="large"
              @click="wishlistStore.toggle(product)"
            >
              {{ wishlistStore.has(product.productId) ? '♥' : '♡' }}
            </el-button>
          </div>

        </div>

      </div>

      <!-- TUTORIAL VIDEO (RIGHT) -->
      <div class="video-section" v-if="product.videoUrl">
        <h2>How to Care for Your {{ product.productName }}</h2>
        <p class="video-subtitle">
          A quick beginner's guide to keeping this plant happy and healthy.
        </p>
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
      </div>

    </div>

    <!-- PLANT CARE GUIDE (full width below) -->
    <div class="care-guide">
      <h2>Complete Care Guide</h2>
      <p class="guide-intro">Everything you need to know to keep your {{ product.productName }} thriving.</p>

      <div class="guide-grid">
        <!-- Sunlight -->
        <div class="guide-card">
          <div class="guide-icon">☀️</div>
          <h3>Sunlight</h3>
          <p>{{ careGuide.sunlight }}</p>
        </div>

        <!-- Watering -->
        <div class="guide-card">
          <div class="guide-icon">💧</div>
          <h3>Watering</h3>
          <p>{{ careGuide.watering }}</p>
        </div>

        <!-- Soil -->
        <div class="guide-card">
          <div class="guide-icon">🪴</div>
          <h3>Soil</h3>
          <p>{{ careGuide.soil }}</p>
        </div>

        <!-- Temperature -->
        <div class="guide-card">
          <div class="guide-icon">🌡️</div>
          <h3>Temperature</h3>
          <p>{{ careGuide.temperature }}</p>
        </div>

        <!-- Humidity -->
        <div class="guide-card">
          <div class="guide-icon">🍃</div>
          <h3>Humidity</h3>
          <p>{{ careGuide.humidity }}</p>
        </div>

        <!-- Fertilizing -->
        <div class="guide-card">
          <div class="guide-icon">🌿</div>
          <h3>Fertilizing</h3>
          <p>{{ careGuide.fertilizing }}</p>
        </div>

        <!-- Pruning -->
        <div class="guide-card">
          <div class="guide-icon">✂️</div>
          <h3>Pruning</h3>
          <p>{{ careGuide.pruning }}</p>
        </div>

        <!-- Repotting -->
        <div class="guide-card">
          <div class="guide-icon">🔄</div>
          <h3>Repotting</h3>
          <p>{{ careGuide.repotting }}</p>
        </div>

        <!-- Common Problems -->
        <div class="guide-card">
          <div class="guide-icon">🩺</div>
          <h3>Common Problems</h3>
          <p>{{ careGuide.problems }}</p>
        </div>
      </div>

      <!-- Quick Tips -->
      <div class="quick-tips">
        <h3>🌱 Quick Tips</h3>
        <ul>
          <li>Check soil moisture before watering — stick your finger 2 inches deep.</li>
          <li>Rotate the plant weekly for even growth.</li>
          <li>Remove yellow or dead leaves promptly to prevent disease.</li>
          <li>Keep leaves clean for better light absorption.</li>
          <li>Avoid placing near cold drafts or heating vents.</li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";
  import { useWishlistStore } from "@/stores/wishlist";

  const cartStore = useCartStore();
  const wishlistStore = useWishlistStore();
  const route = useRoute();
  const router = useRouter();
  const product = ref({});

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

      const response = await api.get(
        `/products/${route.params.id}`
      );

      product.value = response.data;

    } catch (error) {
      console.error(error);
      ElMessage.error("Failed to load product.");
    }

  };

  onMounted(loadProduct);

</script>

<style scoped>

/* WRAPPER */
.detail-wrapper {
  padding: 40px 50px;
  background: #f4fff6;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  gap: 30px;
}

/* LAYOUT — card on left, video on right */
.detail-layout {
  display: flex;
  gap: 30px;
  align-items: flex-start;
  max-width: 1200px;
  width: 100%;
}

/* CARD */
.detail-card {
  background: white;
  padding: 35px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  flex: 1;
  min-width: 0;
}

/* IMAGE — centered */
.image-section {
  display: flex;
  justify-content: center;
  margin-bottom: 25px;
}

.image-section img {
  width: 100%;
  max-width: 450px;
  height: auto;
  max-height: 450px;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
}

/* INFO */
.info-section h1 {
  color: #2E7D32;
  font-size: 28px;
  margin-bottom: 10px;
  text-align: center;
}

.desc {
  margin: 0 0 20px;
  color: #666;
  line-height: 1.6;
  text-align: center;
  font-size: 15px;
}

/* INFO GRID */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 25px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 12px;
  background: #f8faf8;
  border-radius: 10px;
  border-left: 3px solid #2E7D32;
}

.info-label {
  font-size: 12px;
  color: #999;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 15px;
  color: #333;
  font-weight: 600;
}

.info-value.price {
  color: #2E7D32;
  font-size: 20px;
}

/* BUTTONS — side by side */
.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.action-buttons .el-button {
  flex: 1;
}

/* TUTORIAL VIDEO */
.video-section {
  background: white;
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  flex: 1;
  min-width: 0;
  position: sticky;
  top: 30px;
}

.video-section h2 {
  color: #2E7D32;
  font-size: 22px;
  margin-bottom: 6px;
}

.video-subtitle {
  color: #888;
  font-size: 14px;
  margin-bottom: 20px;
}

.video-wrapper {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
  aspect-ratio: 16 / 9;
}

.tutorial-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 0;
}

/* CARE GUIDE */
.care-guide {
  background: white;
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  max-width: 1200px;
  width: 100%;
}

.care-guide h2 {
  color: #2E7D32;
  font-size: 26px;
  text-align: center;
  margin-bottom: 6px;
}

.guide-intro {
  color: #888;
  text-align: center;
  font-size: 15px;
  margin-bottom: 30px;
}

.guide-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.guide-card {
  background: #f8faf8;
  padding: 25px;
  border-radius: 14px;
  box-shadow: 0 3px 10px rgba(0,0,0,0.05);
  border-left: 4px solid #2E7D32;
  transition: 0.25s;
}

.guide-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 18px rgba(46,125,50,0.12);
}

.guide-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.guide-card h3 {
  color: #2E7D32;
  font-size: 16px;
  margin-bottom: 8px;
}

.guide-card p {
  color: #555;
  font-size: 14px;
  line-height: 1.6;
}

/* QUICK TIPS */
.quick-tips {
  margin-top: 35px;
  padding: 25px;
  background: #e8f5e9;
  border-radius: 14px;
}

.quick-tips h3 {
  color: #2E7D32;
  font-size: 18px;
  margin-bottom: 12px;
}

.quick-tips ul {
  margin: 0;
  padding-left: 20px;
}

.quick-tips li {
  color: #555;
  font-size: 14px;
  line-height: 1.8;
}

/* RESPONSIVE */
@media (max-width: 900px) {
  .detail-layout {
    flex-direction: column;
  }

  .video-section {
    position: static;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }
}

</style>
