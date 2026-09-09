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

      <!-- Features -->
      <section class="features">
        <div class="feature-card" v-for="f in features" :key="f.label">
          <span class="feature-icon" v-html="f.icon"></span>
          <div class="feature-text">
            <span class="feature-label">{{ f.label }}</span>
            <span class="feature-desc">{{ f.desc }}</span>
          </div>
        </div>
      </section>

      <FeaturedProduct />
      <Categories />

      <!-- Gardening Tools -->
      <section class="tools-section">
        <div class="section-head">
          <h2>Essential Gardening Tools &amp; Pots</h2>
          <p>Everything you need to keep your plants thriving — now available to buy</p>
        </div>
        <div class="tools-grid">
          <div class="tool-card" v-for="tool in tools" :key="tool.title">
            <div class="tool-icon">{{ tool.icon }}</div>
            <h3>{{ tool.title }}</h3>
            <p>{{ tool.text }}</p>
          </div>
        </div>
        <div class="tools-cta">
          <router-link to="/tools-and-pots" class="tools-shop-btn">
            Shop Tools &amp; Pots
            <span class="tools-shop-arrow">→</span>
          </router-link>
        </div>
      </section>

      <!-- Need More Help / AI -->
      <section class="ai-help">
        <div class="ai-help-inner">
          <span class="ai-badge">AI Powered</span>
          <h2>Need More Help?</h2>
          <p>
            Still unsure about which plant to choose or how to care for one?
            Our AI Assistant is always available to give you personalised advice,
            disease detection, and smart plant recommendations.
          </p>
          <router-link to="/ai" class="ai-cta">
            Ask FloraNest AI
            <span class="ai-cta-arrow">→</span>
          </router-link>
        </div>
      </section>
    </div>

  </div>
</template>

<script setup>
import HeroBanner from '@/components/home/HeroBanner.vue';
import Categories     from '@/components/home/Categories.vue';
import FeaturedProduct from '@/components/home/FeaturedProduct.vue';

const features = [
  {
    icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2C7 6 7 12 12 22C17 12 17 6 12 2Z"/><path d="M12 22V14"/></svg>',
    label: 'Healthy Plants',
    desc: 'Hand-picked & quality-checked'
  },
  {
    icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="3" width="15" height="13" rx="1"/><path d="M16 8h4l3 3v5h-7"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>',
    label: 'Fast Delivery',
    desc: 'Safe shipping to your door'
  },
  {
    icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2C7 7 2 12 2 17C2 20 4 22 7 22C10 22 12 20 12 17C12 20 14 22 17 22C20 22 22 20 22 17C22 12 17 7 12 2Z"/></svg>',
    label: 'Eco Friendly',
    desc: 'Recyclable packaging'
  }
];

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
  background: linear-gradient(180deg, var(--fn-green-900) 0%, var(--fn-green-800) 55%, var(--fn-green-700) 100%);
}

.home-bg-video {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.9;
  filter: saturate(0.65) brightness(0.9);
}

.home-bg-veil {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(10, 40, 24, 0.75) 0%,
    rgba(13, 40, 24, 0.55) 30%,
    rgba(13, 40, 24, 0.42) 60%,
    rgba(10, 40, 24, 0.6) 100%
  );
}

/* ---------- Content ---------- */
.home-content {
  position: relative;
  padding: 0 60px 60px;
}

/* ---------- Features ---------- */
.features {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 64px;
  flex-wrap: wrap;
}

.feature-card {
  flex: 1;
  min-width: 220px;
  max-width: 320px;
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 22px;
  background: rgba(255, 255, 255, 0.94);
  border-radius: var(--fn-radius-md);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: var(--fn-shadow-lg);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

.feature-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  flex-shrink: 0;
  background: var(--fn-green-50);
  border-radius: var(--fn-radius);
  color: var(--fn-green-600);
}

.feature-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.feature-label {
  font-size: 15px;
  font-weight: 700;
  color: var(--fn-ink);
}

.feature-desc {
  font-size: 12px;
  color: var(--fn-text-2);
}

/* ---------- Section heading ---------- */
.section-head {
  text-align: center;
  margin-bottom: 40px;
}

.section-head h2 {
  font-size: clamp(1.5rem, 3vw, 2rem);
  color: var(--fn-green-700);
  margin-bottom: 8px;
  letter-spacing: -0.02em;
}

.section-head p {
  color: var(--fn-text-3);
  font-size: 0.95rem;
}

/* ---------- Tools section ---------- */
.tools-section {
  margin-top: 60px;
  padding: 48px;
  background: rgba(255, 255, 255, 0.96);
  border-radius: var(--fn-radius-lg);
  box-shadow: var(--fn-shadow-lg);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.tool-card {
  background: var(--fn-surface-2);
  padding: 28px 24px;
  border-radius: var(--fn-radius-md);
  border: 1px solid var(--fn-border);
  transition: all var(--fn-t);
}

.tool-card:hover {
  transform: translateY(-4px);
  border-color: var(--fn-green-300);
  box-shadow: var(--fn-shadow-md);
}

.tool-icon {
  font-size: 28px;
  margin-bottom: 12px;
}

.tool-card h3 {
  font-size: 1rem;
  color: var(--fn-green-700);
  margin-bottom: 8px;
}

.tool-card p {
  color: var(--fn-text-2);
  font-size: 13px;
  line-height: 1.6;
}

.tools-cta {
  margin-top: 28px;
  text-align: center;
}

.tools-shop-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 13px 30px;
  background: var(--fn-green-600);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  border-radius: var(--fn-radius-pill);
  text-decoration: none;
  transition: all var(--fn-t);
  box-shadow: var(--fn-shadow-md);
}

.tools-shop-btn:hover {
  background: var(--fn-green-700);
  transform: translateY(-2px);
  box-shadow: var(--fn-shadow-lg);
  color: #fff;
}

.tools-shop-arrow {
  transition: transform var(--fn-t);
}

.tools-shop-btn:hover .tools-shop-arrow {
  transform: translateX(4px);
}

/* ---------- AI Help section ---------- */
.ai-help {
  margin-top: 60px;
}

.ai-help-inner {
  text-align: center;
  padding: 56px 40px;
  background: linear-gradient(135deg, var(--fn-green-800), var(--fn-green-700));
  border-radius: var(--fn-radius-lg);
  box-shadow: var(--fn-shadow-lg);
  position: relative;
  overflow: hidden;
}

.ai-help-inner::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(107, 186, 138, 0.15), transparent 60%);
  border-radius: 50%;
}

.ai-badge {
  display: inline-block;
  padding: 6px 16px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: var(--fn-radius-pill);
  color: var(--fn-green-200);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.05em;
  margin-bottom: 18px;
  position: relative;
}

.ai-help h2 {
  color: #fff;
  margin-bottom: 14px;
  font-size: clamp(1.6rem, 3.5vw, 2.2rem);
  position: relative;
}

.ai-help p {
  color: rgba(255, 255, 255, 0.78);
  margin: 0 auto 28px;
  max-width: 560px;
  line-height: 1.7;
  font-size: 0.95rem;
  position: relative;
}

.ai-cta {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  background: #fff;
  color: var(--fn-green-700);
  font-size: 15px;
  font-weight: 600;
  border-radius: var(--fn-radius-pill);
  transition: all var(--fn-t);
  position: relative;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.ai-cta:hover {
  color: var(--fn-green-700);
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.2);
}

.ai-cta-arrow {
  transition: transform var(--fn-t);
}

.ai-cta:hover .ai-cta-arrow {
  transform: translateX(4px);
}

@media (max-width: 768px) {
  .home-content { padding: 0 20px 40px; }
  .features { flex-direction: column; align-items: center; }
  .feature-card { max-width: none; width: 100%; }
  .tools-section, .ai-help-inner { padding: 32px 20px; }
}
</style>
