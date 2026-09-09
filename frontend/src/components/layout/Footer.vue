<template>
  <footer class="footer">
    <div class="footer-inner">
      <div class="footer-top">
        <!-- Brand -->
        <div class="col col-brand">
          <div class="footer-logo">
            <span class="footer-logo-icon">
              <svg width="24" height="24" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 2C14 2 6 6 6 14C6 20 10 26 14 26C18 26 22 20 22 14C22 6 14 2 14 2Z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                <path d="M14 8V26M14 8C14 8 10 11 10 16M14 8C14 8 18 11 18 16" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </span>
            <span>FloraNest</span>
          </div>
          <p class="brand-desc">
            Your go-to destination for beautiful plants and gardening essentials. Bringing nature closer to you, one plant at a time.
          </p>
          <router-link to="/about" class="about-link">Learn more about us →</router-link>
        </div>

        <!-- Shop -->
        <div class="col">
          <h4>Shop</h4>
          <router-link to="/shop">All Shops</router-link>
          <router-link to="/products">Products</router-link>
          <router-link to="/tools-and-pots">Tools &amp; Pots</router-link>
          <router-link to="/reviews">Reviews</router-link>
          <router-link to="/ai">AI Assistant</router-link>
        </div>

        <!-- Account -->
        <div class="col">
          <h4>Account</h4>
          <router-link to="/login">Login</router-link>
          <router-link to="/register">Sign Up</router-link>
          <router-link to="/my-profile">My Profile</router-link>
          <router-link to="/my-orders">My Orders</router-link>
          <router-link to="/wishlist">Wishlist</router-link>
        </div>

        <!-- Contact -->
        <div class="col">
          <h4>Get in Touch</h4>
          <p class="contact-item">
            <span class="contact-icon">✉</span> support@floranest.com
          </p>
          <p class="contact-item">
            <span class="contact-icon">☎</span> +1 800 FLORA
          </p>
          <p class="contact-item">
            <span class="contact-icon">◷</span> Mon–Fri, 9am–6pm
          </p>
        </div>
      </div>

      <div class="footer-bottom">
        <p>© {{ year }} FloraNest. All rights reserved.</p>
        <div class="footer-socials">
          <span>Privacy Policy</span>
          <span class="dot">·</span>
          <span>Terms of Service</span>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
const year = new Date().getFullYear();
</script>

<style scoped>
.footer {
  background: var(--fn-green-900);
  color: rgba(255, 255, 255, 0.72);
  margin-top: 80px;
}

.footer-inner {
  max-width: 1340px;
  margin: 0 auto;
  padding: 56px 24px 20px;
}

.footer-top {
  display: grid;
  grid-template-columns: 2.2fr 1fr 1fr 1.4fr;
  gap: 40px;
  margin-bottom: 36px;
}

/* Brand column */
.col-brand .footer-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.footer-logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.08);
  border-radius: var(--fn-radius);
  color: var(--fn-green-300);
}

.footer-logo span:last-child {
  font-size: 20px;
  font-weight: 800;
  color: #fff;
  letter-spacing: -0.03em;
}

.brand-desc {
  font-size: 13px;
  line-height: 1.7;
  margin-bottom: 12px;
  color: rgba(255, 255, 255, 0.55);
  max-width: 320px;
}

.about-link {
  font-size: 13px;
  font-weight: 600;
  color: var(--fn-green-300);
  transition: color var(--fn-t-fast);
}

.about-link:hover {
  color: #fff;
}

/* Regular columns */
.col h4 {
  font-size: 13px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 16px;
  color: #fff;
}

.col a {
  display: block;
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  font-size: 14px;
  line-height: 2;
  transition: color var(--fn-t-fast);
}

.col a:hover {
  color: var(--fn-green-300);
}

/* Contact items */
.contact-item {
  font-size: 14px;
  line-height: 2;
  color: rgba(255, 255, 255, 0.6);
  display: flex;
  align-items: center;
  gap: 8px;
}

.contact-icon {
  color: var(--fn-green-300);
  font-size: 14px;
}

/* Footer bottom */
.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  padding-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.footer-bottom p {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.45);
}

.footer-socials {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.45);
}

.dot {
  color: rgba(255, 255, 255, 0.2);
}

@media (max-width: 768px) {
  .footer-top {
    grid-template-columns: 1fr 1fr;
    gap: 28px;
  }

  .col-brand {
    grid-column: 1 / -1;
  }

  .footer-bottom {
    flex-direction: column;
    text-align: center;
  }
}
</style>
