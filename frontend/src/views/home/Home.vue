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
    </div>

  </div>
</template>

<script setup>
import HeroBanner from '@/components/home/HeroBanner.vue';
import Categories     from '@/components/home/Categories.vue';
import FeaturedProduct from '@/components/home/FeaturedProduct.vue';

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

/* Deep green base keeps the page rich even while the video loads */
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
  /* Keep the footage bright enough to read through the veil */
  opacity: 0.95;
  filter: saturate(0.65) brightness(0.95);
}

/* Deep muted veil: the video stays visible but never fights the content */
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
@media (max-width: 768px) {
  .home-content { padding: 20px; }
  .features { flex-direction: column; }
}

/* Respect users who prefer less motion */
@media (prefers-reduced-motion: reduce) {
  .home-bg-video { display: none; }
}
</style>
