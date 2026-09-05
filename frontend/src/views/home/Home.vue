<template>
  <div class="home-wrapper">

    <!-- Ambient background video (soft, behind every section) -->
    <div class="home-bg" aria-hidden="true">
      <div class="home-bg-base"></div>
      <video
        class="home-bg-video"
        autoplay
        muted
        loop
        playsinline
        preload="auto"
        poster="https://images.unsplash.com/photo-1485955900006-10f4d324d411?auto=format&fit=crop&w=1920&q=80"
      >
        <source src="/videos/home-bg.mp4" type="video/mp4" />
      </video>
      <div class="home-bg-veil"></div>
    </div>

    <div class="home-content">
      <HeroBanner />

      <section class="features">
        <div class="feature-card">🌱 Healthy Plants</div>
        <div class="feature-card">🚚 Fast Delivery</div>
        <div class="feature-card">💚 Eco Friendly</div>
      </section>

      <FeaturedProduct />
      <Categories />

      <!-- Gardening Tools -->
      <section class="tools-section">
        <h2>🛠️ Essential Gardening Tools</h2>
        <p class="tools-subtitle">Everything you need to keep your plants thriving</p>
        <div class="tools-grid">
          <div class="tool-card" v-for="tool in tools" :key="tool.title">
            <div class="tool-icon">{{ tool.icon }}</div>
            <h3>{{ tool.title }}</h3>
            <p>{{ tool.text }}</p>
          </div>
        </div>
      </section>

      <!-- Need More Help / AI -->
      <section class="ai-help">
        <h2>🤖 Need More Help?</h2>
        <p>
          Still unsure about which plant to choose or how to care for one?
          Our AI Assistant is always available to give you personalised advice.
        </p>
        <router-link to="/ai">
          <el-button type="success" size="large">Ask FloraNest AI</el-button>
        </router-link>
      </section>
    </div>

  </div>
</template>

<script setup>
import HeroBanner from '@/components/home/HeroBanner.vue';
import Categories     from '@/components/home/Categories.vue';
import FeaturedProduct from '@/components/home/FeaturedProduct.vue';

const tools = [
  {
    icon: '✂️',
    title: 'Pruning Shears',
    text: 'For clean cuts that remove dead or overgrown leaves without crushing the stem.'
  },
  {
    icon: '🚿',
    title: 'Watering Can',
    text: 'A narrow spout gives you control so you water the soil, not the leaves.'
  },
  {
    icon: '🧤',
    title: 'Gardening Gloves',
    text: 'Protect your hands when repotting, pruning, or handling thorny or irritant plants.'
  },
  {
    icon: '🪴',
    title: 'Pots with Drainage',
    text: 'Good drainage holes prevent root rot by letting excess water escape.'
  },
  {
    icon: '🌡️',
    title: 'Moisture Meter',
    text: 'Takes the guesswork out of watering by telling you exactly when soil is dry.'
  },
  {
    icon: '🧹',
    title: 'Cleaning Brush',
    text: 'Keep leaves dust-free so they can absorb light efficiently.'
  }
];
</script>

<style scoped>
.home-wrapper {
  min-height: 100vh;
}

/* ---------- Ambient background ---------- */
.home-bg {
  position: fixed;
  inset: 0;
  z-index: -1;
  overflow: hidden;
  pointer-events: none;
}

.home-bg-base {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, #18271d 0%, #1e3024 55%, #223529 100%);
}

.home-bg-video {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.95;
  filter: saturate(0.65) brightness(0.95);
}

.home-bg-veil {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(6, 15, 9, 0.82) 0%,
    rgba(8, 18, 11, 0.66) 30%,
    rgba(10, 22, 14, 0.5) 60%,
    rgba(9, 20, 13, 0.68) 100%
  );
}

/* ---------- Content ---------- */
.home-content {
  position: relative;
  padding: 40px 60px;
}

.features {
  display: flex;
  justify-content: space-between;
  gap: 15px;
  margin-bottom: 40px;
}
.feature-card {
  flex: 1;
  padding: 20px;
  background: rgba(251, 252, 250, 0.94);
  text-align: center;
  border-radius: 10px;
  font-weight: bold;
  color: #2f4634;
  border: 1px solid rgba(255, 255, 255, 0.65);
  box-shadow: 0 8px 22px rgba(0, 0, 0, 0.22);
}

/* AI Help section */
.ai-help {
  margin-top: 50px;
  text-align: center;
  padding: 40px;
  background: rgba(251, 252, 250, 0.94);
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

/* Gardening Tools section */
.tools-section {
  margin-top: 50px;
  padding: 40px;
  background: rgba(251, 252, 250, 0.94);
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

.tools-section h2 {
  text-align: center;
  color: #2E7D32;
  font-size: 26px;
  margin-bottom: 6px;
}

.tools-subtitle {
  text-align: center;
  color: #888;
  margin-bottom: 30px;
  font-size: 15px;
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.tool-card {
  background: white;
  padding: 25px;
  border-radius: 14px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.07);
  border-left: 4px solid #2E7D32;
  transition: 0.25s;
}

.tool-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(46, 125, 50, 0.15);
}

.tool-icon {
  font-size: 30px;
  margin-bottom: 10px;
}

.tool-card h3 {
  color: #2E7D32;
  margin-bottom: 8px;
}

.tool-card p {
  color: #555;
  font-size: 14px;
  line-height: 1.6;
}

.ai-help h2 {
  color: #2E7D32;
  margin-bottom: 10px;
  font-size: 26px;
}

.ai-help p {
  color: #666;
  margin-bottom: 20px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .home-content { padding: 20px; }
  .features { flex-direction: column; }
}

@media (prefers-reduced-motion: reduce) {
  .home-bg-video { display: none; }
}
</style>
