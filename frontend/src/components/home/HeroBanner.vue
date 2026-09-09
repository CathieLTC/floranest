<template>
  <section class="hero">
    <div class="hero-content">
      <span class="hero-badge">Premium Plants & Gardening Essentials</span>
      <h1>
        Grow Your Perfect<br />
        <span class="hero-highlight">Green Space</span>
      </h1>
      <p>
        Discover premium indoor and outdoor plants that bring life,
        beauty, and fresh air into your home. From beginner-friendly
        houseplants to stunning garden collections, we have everything
        you need.
      </p>
      <div class="hero-buttons">
        <router-link to="/shop" class="btn-primary">
          Shop Now
          <span class="btn-arrow">→</span>
        </router-link>
        <router-link to="/products" class="btn-secondary">
          View Products
        </router-link>
      </div>
    </div>
    <div class="hero-scroll" aria-hidden="true">
      <span class="scroll-line"></span>
    </div>
  </section>
</template>

<style scoped>
.hero {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: calc(100vh - var(--fn-nav-h));
  padding: 80px 24px 60px;
  box-sizing: border-box;
  text-align: center;
  position: relative;
}

.hero-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 860px;
  margin: 0 auto;
}

.hero-badge {
  display: inline-block;
  padding: 7px 18px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: var(--fn-radius-pill);
  color: rgba(255, 255, 255, 0.85);
  font-size: 12px;
  font-weight: 500;
  letter-spacing: 0.03em;
  margin-bottom: 28px;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.hero-content h1 {
  font-size: clamp(2.8rem, 6vw, 5rem);
  font-weight: 800;
  color: #f5fbf6;
  line-height: 1.08;
  margin: 0 0 24px;
  letter-spacing: -0.03em;
  text-shadow: 0 2px 30px rgba(0, 0, 0, 0.25);
}

.hero-highlight {
  background: linear-gradient(135deg, var(--fn-green-400), var(--fn-green-300));
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-content p {
  max-width: 620px;
  font-size: clamp(1rem, 1.5vw, 1.2rem);
  color: rgba(238, 247, 240, 0.82);
  line-height: 1.75;
  margin: 0 0 36px;
  text-shadow: 0 1px 12px rgba(0, 0, 0, 0.2);
}

.hero-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  background: var(--fn-green-500);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  border-radius: var(--fn-radius-pill);
  transition: all var(--fn-t);
  box-shadow: 0 4px 20px rgba(42, 125, 79, 0.4);
}

.btn-primary:hover {
  background: var(--fn-green-400);
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 6px 28px rgba(42, 125, 79, 0.5);
}

.btn-arrow {
  transition: transform var(--fn-t);
}

.btn-primary:hover .btn-arrow {
  transform: translateX(4px);
}

.btn-secondary {
  display: inline-flex;
  align-items: center;
  padding: 14px 32px;
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: var(--fn-radius-pill);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  transition: all var(--fn-t);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  border-color: rgba(255, 255, 255, 0.4);
}

/* Scroll indicator */
.hero-scroll {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
}

.scroll-line {
  display: block;
  width: 1px;
  height: 40px;
  background: linear-gradient(180deg, transparent, rgba(255, 255, 255, 0.5));
  animation: scrollPulse 2s ease-in-out infinite;
}

@keyframes scrollPulse {
  0%, 100% { opacity: 0.3; transform: scaleY(0.6); }
  50% { opacity: 1; transform: scaleY(1); }
}

@media (max-width: 788px) {
  .hero {
    min-height: auto;
    padding: 50px 20px 40px;
  }

  .hero-badge {
    font-size: 11px;
    margin-bottom: 20px;
  }

  .hero-scroll {
    display: none;
  }
}
</style>
