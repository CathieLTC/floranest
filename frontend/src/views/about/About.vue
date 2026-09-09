<template>
  <div class="about-wrapper">

    <!-- HERO -->
    <section class="hero">
      <h1>About FloraNest</h1>
      <p>Bringing nature closer to you, one plant at a time.</p>
    </section>

    <!-- OUR STORY -->
    <section class="story">
      <div class="story-content">
        <h2>🌱 Our Story</h2>
        <p>
          FloraNest started with a simple idea: everyone deserves to experience the joy of
          growing plants. Whether you live in a tiny apartment or a house with a garden,
          plants bring life, colour, and calm to any space.
        </p>
        <p>
          What began as a passion project by a group of plant lovers has grown into a
          full-fledged online plant shop. We carefully source, nurture, and deliver healthy
          plants straight to your door — along with the knowledge you need to keep them thriving.
        </p>
      </div>
      <div class="story-image">
        <img
          src="https://images.unsplash.com/photo-1459411552458-0d0a5e99193a?auto=format&fit=crop&w=800&q=80"
          alt="A greenhouse full of plants"
        />
      </div>
    </section>

    <!-- MISSION & VALUES -->
    <section class="values">
      <h2>💚 Our Mission</h2>
      <p class="mission-text">
        To make plant ownership accessible, enjoyable, and sustainable for everyone —
        from first-time buyers to seasoned gardeners.
      </p>

      <div class="values-grid">
        <div class="value-card">
          <div class="value-icon">🌿</div>
          <h3>Quality Plants</h3>
          <p>Every plant is hand-picked and health-checked before it reaches you.</p>
        </div>
        <div class="value-card">
          <div class="value-icon">📚</div>
          <h3>Expert Guidance</h3>
          <p>Detailed care guides and our AI assistant help you every step of the way.</p>
        </div>
        <div class="value-card">
          <div class="value-icon">🚚</div>
          <h3>Safe Delivery</h3>
          <p>Plants are packed with care and shipped quickly so they arrive in top condition.</p>
        </div>
        <div class="value-card">
          <div class="value-icon">🌍</div>
          <h3>Eco-Friendly</h3>
          <p>We use recyclable packaging and partner with sustainable growers.</p>
        </div>
      </div>
    </section>

    <!-- STATS -->
    <section class="stats">
      <div class="stat-item">
        <span class="stat-number">49+</span>
        <span class="stat-label">Plants Available</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">8</span>
        <span class="stat-label">Categories</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">1000+</span>
        <span class="stat-label">Happy Customers</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">4.8★</span>
        <span class="stat-label">Average Rating</span>
      </div>
    </section>

    <!-- WHAT MAKES US DIFFERENT -->
    <section class="different">
      <h2>What Makes Us Different?</h2>
      <div class="different-grid">
        <div class="diff-card">
          <div class="diff-icon">🤖</div>
          <h3>AI-Powered Assistant</h3>
          <p>
            Our FloraNest AI helps you choose the right plant, diagnose diseases from photos,
            and get instant gardening advice — all in one place.
          </p>
        </div>
        <div class="diff-card">
          <div class="diff-icon">🩺</div>
          <h3>Disease Detection</h3>
          <p>
            Snap a photo of an unhealthy plant and our AI will identify the disease, suggest
            treatments, and recommend prevention tips.
          </p>
        </div>
        <div class="diff-card">
          <div class="diff-icon">❤️</div>
          <h3>Wishlist & Favourites</h3>
          <p>Save plants you love to your wishlist and come back to them anytime.</p>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="cta">
      <h2>Ready to start your plant journey?</h2>
      <p>Browse our collection and find your perfect green companion.</p>
      <div class="cta-buttons">
        <router-link to="/products">
          <el-button type="success" size="large">Browse Products</el-button>
        </router-link>
        <router-link to="/shop">
          <el-button size="large">Visit Shops</el-button>
        </router-link>
      </div>
    </section>

  </div>
</template>

<script setup>
</script>

<style scoped>
.about-wrapper {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 24px 60px;
}

/* ── Hero ── */
.hero {
  text-align: center;
  padding: 60px 20px 40px;
}

.hero h1 {
  font-size: clamp(2rem, 5vw, 3rem);
  color: #1B5E20;
  margin: 0 0 12px;
}

.hero p {
  font-size: 1.15rem;
  color: #555;
  max-width: 560px;
  margin: 0 auto;
}

/* ── Our Story ── */
.story {
  display: flex;
  align-items: center;
  gap: 40px;
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(46, 125, 50, 0.08);
  margin-bottom: 50px;
}

.story-content {
  flex: 1;
}

.story-content h2 {
  color: #2E7D32;
  font-size: 1.6rem;
  margin-bottom: 16px;
}

.story-content p {
  color: #555;
  line-height: 1.8;
  margin-bottom: 14px;
  font-size: 0.98rem;
}

.story-image {
  flex: 1;
  border-radius: 14px;
  overflow: hidden;
}

.story-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  aspect-ratio: 4 / 3;
}

/* ── Mission & Values ── */
.values {
  text-align: center;
  margin-bottom: 50px;
}

.values h2 {
  color: #2E7D32;
  font-size: 1.6rem;
  margin-bottom: 10px;
}

.mission-text {
  color: #666;
  font-size: 1.1rem;
  max-width: 620px;
  margin: 0 auto 36px;
  line-height: 1.6;
}

.values-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
}

.value-card {
  background: white;
  padding: 28px 24px;
  border-radius: 14px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.06);
  border-top: 4px solid #2E7D32;
  transition: 0.25s;
}

.value-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(46, 125, 50, 0.12);
}

.value-icon {
  font-size: 34px;
  margin-bottom: 12px;
}

.value-card h3 {
  color: #2E7D32;
  margin-bottom: 8px;
}

.value-card p {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.6;
}

/* ── Stats ── */
.stats {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 20px;
  background: linear-gradient(135deg, #1B5E20, #2E7D32);
  border-radius: 16px;
  padding: 36px 20px;
  margin-bottom: 50px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-number {
  font-size: 2.2rem;
  font-weight: 700;
  color: #fff;
}

.stat-label {
  font-size: 0.9rem;
  color: #c8e6c9;
}

/* ── What Makes Us Different ── */
.different {
  margin-bottom: 50px;
}

.different h2 {
  text-align: center;
  color: #2E7D32;
  font-size: 1.6rem;
  margin-bottom: 30px;
}

.different-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.diff-card {
  background: white;
  padding: 28px;
  border-radius: 14px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.06);
  border-left: 4px solid #2E7D32;
  transition: 0.25s;
}

.diff-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(46, 125, 50, 0.12);
}

.diff-icon {
  font-size: 30px;
  margin-bottom: 10px;
}

.diff-card h3 {
  color: #2E7D32;
  margin-bottom: 8px;
}

.diff-card p {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.6;
}

/* ── CTA ── */
.cta {
  text-align: center;
  background: white;
  border-radius: 16px;
  padding: 50px 30px;
  box-shadow: 0 4px 20px rgba(46, 125, 50, 0.08);
}

.cta h2 {
  color: #1B5E20;
  font-size: 1.8rem;
  margin-bottom: 10px;
}

.cta p {
  color: #666;
  font-size: 1.05rem;
  margin-bottom: 24px;
}

.cta-buttons {
  display: flex;
  justify-content: center;
  gap: 14px;
  flex-wrap: wrap;
}

/* ── Responsive ── */
@media (max-width: 768px) {
  .story {
    flex-direction: column;
    padding: 28px 20px;
  }
  .stats {
    gap: 28px;
  }
}
</style>
