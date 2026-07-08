<template>
  <div class="home-wrapper">

    <HeroBanner />

    <!-- Feature badges -->
    <section class="features">
      <div class="feature-card">🌱 Healthy Plants</div>
      <div class="feature-card">🚚 Fast Delivery</div>
      <div class="feature-card">💚 Eco Friendly</div>
    </section>

  <FeaturedProduct />
  <Categories />

  </div>
</template>

<script setup>
import HeroBanner from '@/components/home/HeroBanner.vue';
import Categories     from '@/components/home/Categories.vue';
import FeaturedProduct from '@/components/home/FeaturedProduct.vue';

</script>

<style scoped>
.home-wrapper {
  background: #f4fff6;
  min-height: 100vh;
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
  background: white;
  text-align: center;
  border-radius: 10px;
  font-weight: bold;
  box-shadow: 0 5px 15px rgba(0,0,0,0.05);
}
@media (max-width: 768px) {
  .home-wrapper { padding: 20px; }
  .features { flex-direction: column; }
}
</style>