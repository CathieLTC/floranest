<template>
  <section class="hero">
    <div class="hero-text">
      <h1>Grow Your Perfect Green Space 🌿</h1>
      <p>
        Discover beautiful indoor and outdoor plants for your home,
        garden, and lifestyle.
      </p>
      <div class="hero-buttons">
        <router-link to="/shop">
          <el-button type="success" size="large">Shop Now</el-button>
        </router-link>
        <router-link to="/shop">
          <el-button size="large">View Products</el-button>
        </router-link>
      </div>
    </div>

    <div class="hero-image">
      <img
        src="https://images.unsplash.com/photo-1446071103084-c257b5f70672"
        alt="Plants"
      />
    </div>
  </section>
</template>

<style scoped>
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 50px;
  gap: 40px;
}
.hero-text { max-width: 500px; }
.hero-text h1 {
  font-size: 38px;
  color: #2E7D32;
  line-height: 1.2;
}
.hero-text p {
  margin: 15px 0;
  color: #666;
  line-height: 1.6;
}
.hero-buttons { display: flex; gap: 10px; margin-top: 20px; }
.hero-image img {
  width: 480px;
  border-radius: 15px;
  object-fit: cover;
}

@media (max-width: 900px) {
  .hero { flex-direction: column; text-align: center; }
  .hero-image img { width: 100%; }
  .hero-buttons { justify-content: center; }
}
</style>