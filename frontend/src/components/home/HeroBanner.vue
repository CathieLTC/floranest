<template>
  <section class="hero">
    <!-- Left Side -->
    <div class="hero-text">
      <h1>Grow Your Perfect Green Space 🌿</h1>

      <p>
        Discover premium indoor and outdoor plants that bring life,
        beauty, and fresh air into your home. From beginner-friendly
        houseplants to stunning garden collections, we have everything
        you need.
      </p>

      <div class="hero-buttons">
        <router-link to="/shop">
          <el-button type="success" size="large">
            Shop Now
          </el-button>
        </router-link>

        <router-link to="/shop">
          <el-button size="large">
            View Products
          </el-button>
        </router-link>
      </div>
    </div>

    <!-- Right Side -->
    <div class="hero-carousel">
     <el-carousel
        height="600px"
        :interval="5000"
        autoplay
        :pause-on-hover="false"
        arrow="hover"
      >
        <el-carousel-item
          v-for="(image, index) in images"
          :key="index"
        >
          <img
            :src="image"
            class="hero-slide"
            alt="Plant"
          />
        </el-carousel-item>
      </el-carousel>
    </div>
  </section>
</template>

<script setup>
const images = [
  "https://images.unsplash.com/photo-1485955900006-10f4d324d411?auto=format&fit=crop&w=1600&q=80",
  "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?auto=format&fit=crop&w=1600&q=80",
  "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1600&q=80",
  "https://images.unsplash.com/photo-1497250681960-ef046c08a56e?auto=format&fit=crop&w=1600&q=80",
  "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=1600&q=80",
  "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=1600&q=80"
]
</script>

<style scoped>
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 70px;

  width: 100%;
  min-height: 85vh;
  padding: 60px 6%;
  box-sizing: border-box;
}

/* Left Side */
.hero-text {
  flex: 1;
}

.hero-text h1 {
  font-size: 3.4rem;
  font-weight: 700;
  color: #2E7D32;
  line-height: 1.15;
  margin-bottom: 25px;
}

.hero-text p {
  font-size: 18px;
  color: #666;
  line-height: 1.8;
  margin-bottom: 35px;
}

.hero-buttons {
  display: flex;
  gap: 18px;
}

/* Right Side */
.hero-carousel {
  flex: 1;
  width: 100%;
}

.hero-carousel .el-carousel {
  width: 100%;
  border-radius: 25px;
  overflow: hidden;
}

.hero-slide {
  width: 100%;
  height: 600px;
  object-fit: cover;
  border-radius: 25px;
}

/* Hide indicator dots/dashes */
:deep(.el-carousel__indicators) {
  display: none;
}

/* Style navigation arrows */
:deep(.el-carousel__arrow) {
  background: rgba(0, 0, 0, 0.35);
  color: white;
}

:deep(.el-carousel__arrow:hover) {
  background: rgba(46, 125, 50, 0.9);
}

/* Mobile */
@media (max-width: 900px) {
  .hero {
    flex-direction: column;
    text-align: center;
    gap: 40px;
    padding: 40px 20px;
    min-height: auto;
  }

  .hero-text h1 {
    font-size: 2.5rem;
  }

  .hero-buttons {
    justify-content: center;
    flex-wrap: wrap;
  }

  .hero-carousel {
    width: 100%;
  }

  .hero-slide {
    height: 380px;
  }
}
</style>