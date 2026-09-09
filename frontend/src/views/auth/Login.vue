<template>
<div class="login-wrapper">
  <div class="login-card">
      <!-- LEFT SIDE BRAND -->
      <div class="brand-side">
        <div class="brand-inner">
          <div class="brand-logo">
            <span class="brand-logo-icon">
              <svg width="32" height="32" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 2C14 2 6 6 6 14C6 20 10 26 14 26C18 26 22 20 22 14C22 6 14 2 14 2Z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                <path d="M14 8V26M14 8C14 8 10 11 10 16M14 8C14 8 18 11 18 16" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </span>
            <span>FloraNest</span>
          </div>
          <h1>Welcome back!</h1>
          <p>Login to continue exploring our premium collection of plants and gardening essentials.</p>
          <div class="brand-features">
            <span class="brand-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
              Healthy, hand-picked plants
            </span>
            <span class="brand-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
              Fast & safe delivery
            </span>
            <span class="brand-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
              AI-powered plant care
            </span>
          </div>
        </div>
      </div>

      <!-- RIGHT SIDE FORM -->
      <div class="form-side">
        <span class="form-eyebrow">Account</span>
        <h2>Login</h2>

        <!-- EMAIL INPUT -->
        <el-input type="text" v-model="email" placeholder="Enter your email" class="input" clearable>
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>

        <!-- PASSWORD INPUT -->
        <el-input v-model="password" type="password" placeholder="Enter your password" class="input" show-password>
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>

        <!-- LOGIN BUTTON -->
        <el-button type="success" class="login-btn" @click="handleLogin">Login</el-button>

        <p class="footer-text">Don't have an account? <router-link to="/register">Sign up</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref } from "vue";
  import { Message, Lock } from "@element-plus/icons-vue";
  import { ElMessage } from "element-plus";
  import { useRouter } from "vue-router";
  import api from "@/api/axios";
  import { useUserStore } from "@/stores/user";

  const userStore = useUserStore();

  const email = ref("");
  const password = ref("");

  const router = useRouter();

  const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  const handleLogin = async () => {
    if (!email.value.trim() || !password.value) {
      ElMessage.warning("Please enter your email and password.");
      return;
    }

    if (!EMAIL_REGEX.test(email.value.trim())) {
      ElMessage.warning("Please enter a valid email address.");
      return;
    }

    try {

      const response = await api.post("/auth/login", {
        email: email.value,
        password: password.value
      });

      if (response.data.success) {

        userStore.login(response.data.user);

        ElMessage.success("Welcome Back!");

        router.push("/");

      } else {

        ElMessage.error(response.data.message);

      }

    } catch (error) {

      ElMessage.error("Unable to connect to server.");
      console.error(error);

    }
  };
</script>

<style scoped>
  /* FULL PAGE CENTER */
  .login-wrapper {
    min-height: calc(100vh - var(--fn-nav-h));
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px 20px;
    background: transparent;
  }

  /* MAIN CARD */
  .login-card {
    width: 880px;
    max-width: 100%;
    display: flex;
    border-radius: var(--fn-radius-lg);
    overflow: hidden;
    box-shadow: var(--fn-shadow-xl);
    background: #fff;
  }

  /* LEFT BRAND SIDE */
  .brand-side {
    flex: 1;
    position: relative;
    background-image: url("https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=900&q=80&auto=format&fit=crop");
    background-size: cover;
    background-position: center;
    color: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 48px;
  }

  .brand-side::before {
    content: "";
    position: absolute;
    inset: 0;
    background: linear-gradient(160deg, rgba(13, 40, 24, 0.82), rgba(31, 98, 55, 0.52));
  }

  .brand-inner {
    position: relative;
    z-index: 1;
  }

  .brand-logo {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 40px;
  }

  .brand-logo-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 44px;
    height: 44px;
    background: rgba(255, 255, 255, 0.12);
    border-radius: var(--fn-radius);
    color: #fff;
  }

  .brand-logo span:last-child {
    font-size: 22px;
    font-weight: 800;
    color: #fff;
    letter-spacing: -0.03em;
  }

  .brand-inner h1 {
    font-size: 2rem;
    margin-bottom: 12px;
    color: #fff;
    letter-spacing: -0.02em;
  }

  .brand-inner p {
    font-size: 14px;
    opacity: 0.85;
    line-height: 1.7;
    margin-bottom: 32px;
    max-width: 340px;
  }

  .brand-features {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .brand-feature {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 13px;
    color: rgba(255, 255, 255, 0.9);
  }

  .brand-feature svg {
    color: var(--fn-green-300);
    flex-shrink: 0;
  }

  /* RIGHT FORM SIDE */
  .form-side {
    flex: 1;
    background: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 48px;
  }

  .form-eyebrow {
    display: inline-block;
    font-size: 12px;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.1em;
    color: var(--fn-green-600);
    margin-bottom: 8px;
  }

  .form-side h2 {
    margin-bottom: 28px;
    color: var(--fn-ink);
    font-size: 1.8rem;
    letter-spacing: -0.02em;
  }

  /* INPUTS */
  .input {
    margin-bottom: 16px;
  }

  /* LOGIN BUTTON */
  .login-btn {
    width: 100%;
    margin-top: 8px;
    height: 44px;
    font-size: 15px;
  }

  /* FOOTER */
  .footer-text {
    margin-top: 20px;
    font-size: 13px;
    color: var(--fn-text-2);
    text-align: center;
  }

  .footer-text a {
    color: var(--fn-green-600);
    text-decoration: none;
    font-weight: 700;
    transition: color var(--fn-t-fast);
  }

  .footer-text a:hover {
    color: var(--fn-green-700);
  }

  @media (max-width: 768px) {
    .login-card {
      flex-direction: column;
      max-width: 420px;
    }
    .brand-side {
      min-height: 200px;
    }
    .brand-inner {
      display: none;
    }
    .brand-side::before {
      background: linear-gradient(160deg, rgba(13, 40, 24, 0.9), rgba(31, 98, 55, 0.7));
    }
    .brand-logo {
      margin-bottom: 0;
    }
    .form-side {
      padding: 36px 28px;
    }
  }
</style>
