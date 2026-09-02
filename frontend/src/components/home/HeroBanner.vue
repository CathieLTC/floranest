<template>
  <section class="hero">
    <div class="hero-text">
      <h1>Grow Your Perfect Green Space </h1>

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
  </section>
</template>

<style scoped>
.hero {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: 100vh;
  padding: 100px 24px;
  box-sizing: border-box;
  text-align: center;
}

.hero-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  max-width: 980px;
  margin: 0 auto;
}

.hero-text h1 {
  font-size: clamp(3.6rem, 6.4vw, 5.5rem);
  font-weight: 800;
  color: #f2fbf3;
  line-height: 1.12;
  margin: 0 0 28px;
  text-shadow: 0 3px 22px rgba(0, 0, 0, 0.35);
}

.hero-text p {
  max-width: 720px;
  font-size: 1.45rem;
  color: rgba(238, 247, 240, 0.94);
  line-height: 1.85;
  margin: 0 0 44px;
  text-shadow: 0 1px 12px rgba(0, 0, 0, 0.3);
}

.hero-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 30px;
}

@media (max-width: 788px) {
  .hero {
    min-height: auto;
    padding: 60px 20px;
  }

  .hero-text h1 {
    font-size: 2.5rem;
  }

  .hero-text p {
    font-size: 1.09rem;
  }

}
</style>
