<template>
  <div class="register-wrapper">

    <div class="register-card">

      <!-- LEFT BRAND SIDE -->
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
          <h1>Join us today!</h1>
          <p>Create your account and start exploring our premium collection of plants and gardening essentials.</p>
          <div class="brand-features">
            <span class="brand-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
              Wishlist your favourite plants
            </span>
            <span class="brand-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
              Track your orders easily
            </span>
            <span class="brand-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
              AI-powered plant care advice
            </span>
          </div>
        </div>
      </div>

      <!-- RIGHT FORM SIDE -->
      <div class="form-side">

        <!-- ADMIN PASSWORD ACCESS -->
        <div class="admin-gate">
          <span class="admin-gate-label">
            <el-icon class="admin-gate-icon"><Lock /></el-icon>
            Are you the store admin?
          </span>
          <p class="admin-gate-hint">
            Enter the admin password to open the management dashboard — no account needed.
          </p>
          <el-input
            v-model="adminPassword"
            type="password"
            placeholder="Admin password"
            class="input"
            show-password
            @keyup.enter="handleAdminAccess"
          />
          <el-button
            class="admin-gate-btn"
            :loading="adminAccessLoading"
            @click="handleAdminAccess"
          >
            Open Admin Dashboard
          </el-button>
        </div>

        <!-- DIVIDER -->
        <div class="divider">
          <span>or create a customer account</span>
        </div>

        <!-- CUSTOMER SIGN UP -->
        <span class="form-eyebrow">Get started</span>
        <h2>Create Account</h2>

        <!-- NAME -->
        <el-input
          v-model="name"
          placeholder="Full Name"
          class="input"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>

        <!-- EMAIL -->
        <el-input
          v-model="email"
          placeholder="Email"
          class="input"
          clearable
        >
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>

        <!-- PASSWORD -->
        <el-input
          v-model="password"
          type="password"
          placeholder="Password"
          class="input"
          show-password
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>

        <!-- CONFIRM PASSWORD -->
        <el-input
          v-model="confirmPassword"
          type="password"
          placeholder="Confirm Password"
          class="input"
          show-password
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>

        <!-- REGISTER BUTTON -->
        <el-button type="success" class="register-btn" @click="handleRegister">
          Create Account
        </el-button>

        <p class="footer-text">
          Already have an account?
          <router-link to="/login">Login</router-link>
        </p>

      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref } from "vue";
  import { User, Message, Lock } from "@element-plus/icons-vue";
  import { ElMessage, ElMessageBox } from "element-plus";
  import { useRouter } from "vue-router";
  import api from "@/api/axios";
  import { useUserStore } from "@/stores/user";

  const userStore = useUserStore();
  const router = useRouter();

  const name = ref("");
  const email = ref("");
  const password = ref("");
  const confirmPassword = ref("");

  // Admin password gate (no account is created)
  const adminPassword = ref("");
  const adminAccessLoading = ref(false);

  const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  const handleAdminAccess = async () => {
    if (!adminPassword.value) {
      ElMessage.warning("Please enter the admin password.");
      return;
    }

    adminAccessLoading.value = true;

    try {
      const response = await api.post("/auth/admin/access", {
        password: adminPassword.value.trim()
      });

      if (response.data.success) {
        userStore.grantAdminAccess();
        ElMessage.success("Admin access granted.");
        router.push("/admin");
      } else {
        ElMessage.error(response.data.message);
      }

    } catch (error) {
      ElMessage.error("Unable to connect to server.");
      console.error(error);
    } finally {
      adminAccessLoading.value = false;
    }
  };

  const handleRegister = async () => {

    if (!name.value.trim()) {
      ElMessage.warning("Please enter your full name.");
      return;
    }

    if (!email.value.trim()) {
      ElMessage.warning("Please enter your email address.");
      return;
    }

    if (!EMAIL_REGEX.test(email.value.trim())) {
      ElMessage.warning("Please enter a valid email address.");
      return;
    }

    if (!password.value) {
      ElMessage.warning("Please create a password.");
      return;
    }

    if (password.value.length < 6) {
      ElMessage.warning("Password must be at least 6 characters.");
      return;
    }

    if (password.value !== confirmPassword.value) {
      ElMessage.error("Passwords do not match.");
      return;
    }

    try {

      const response = await api.post("/auth/register", {
        fullName: name.value,
        email: email.value,
        password: password.value
      });

      if (response.data.success) {

        ElMessageBox({
          title: "Registration Successful",
          message: "Choose how you'd like to continue.",
          showCancelButton: true,
          confirmButtonText: "Login Automatically",
          cancelButtonText: "Go to Login",
          type: "success"
        })

        .then(async () => {

          const loginResponse = await api.post("/auth/login", {
            email: email.value,
            password: password.value
          });

          if (loginResponse.data.success) {

            userStore.login(loginResponse.data.user);

            ElMessage.success("Welcome to FloraNest!");

            router.push("/");

          }

        })

        .catch(() => {

          router.push("/login");

        });

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
  /* PAGE WRAPPER */
  .register-wrapper {
    min-height: calc(100vh - var(--fn-nav-h));
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px 20px;
    background: transparent;
  }

  /* CARD */
  .register-card {
    width: 880px;
    max-width: 100%;
    display: flex;
    border-radius: var(--fn-radius-lg);
    overflow: hidden;
    box-shadow: var(--fn-shadow-xl);
    background: #fff;
  }

  /* LEFT SIDE */
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

  /* RIGHT SIDE */
  .form-side {
    flex: 1;
    background: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 36px 48px;
  }

  .form-eyebrow {
    display: inline-block;
    font-size: 12px;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.1em;
    color: var(--fn-green-600);
    margin-bottom: 6px;
  }

  .form-side h2 {
    margin-bottom: 18px;
    color: var(--fn-ink);
    font-size: 1.6rem;
    letter-spacing: -0.02em;
  }

  /* ADMIN GATE */
  .admin-gate {
    background: var(--fn-surface-2);
    border: 1px solid var(--fn-border);
    border-radius: var(--fn-radius);
    padding: 16px;
    margin-bottom: 12px;
  }

  .admin-gate-label {
    display: flex;
    align-items: center;
    gap: 8px;
    color: var(--fn-ink);
    font-size: 14px;
    font-weight: 700;
  }

  .admin-gate-icon {
    color: var(--fn-green-600);
    font-size: 16px;
  }

  .admin-gate-hint {
    color: var(--fn-text-3);
    font-size: 12px;
    line-height: 1.5;
    margin: 6px 0 12px;
  }

  .admin-gate .input {
    margin-bottom: 10px;
  }

  .admin-gate-btn {
    width: 100%;
    height: 40px;
    font-size: 14px;
    background: var(--fn-ink);
    border-color: var(--fn-ink);
    color: #fff;
  }

  .admin-gate-btn:hover,
  .admin-gate-btn:focus {
    background: var(--fn-green-700);
    border-color: var(--fn-green-700);
    color: #fff;
  }

  /* DIVIDER */
  .divider {
    display: flex;
    align-items: center;
    gap: 12px;
    margin: 14px 0 18px;
  }

  .divider::before,
  .divider::after {
    content: "";
    flex: 1;
    height: 1px;
    background: var(--fn-border);
  }

  .divider span {
    color: var(--fn-text-3);
    font-size: 12px;
    white-space: nowrap;
    text-transform: uppercase;
    letter-spacing: 0.04em;
  }

  /* INPUTS */
  .input {
    margin-bottom: 12px;
  }

  /* BUTTON */
  .register-btn {
    width: 100%;
    margin-top: 6px;
    height: 44px;
    font-size: 15px;
  }

  /* FOOTER */
  .footer-text {
    margin-top: 16px;
    font-size: 13px;
    color: var(--fn-text-2);
    text-align: center;
  }

  .footer-text a {
    color: var(--fn-green-600);
    font-weight: 700;
    text-decoration: none;
    transition: color var(--fn-t-fast);
  }

  .footer-text a:hover {
    color: var(--fn-green-700);
  }

  @media (max-width: 768px) {
    .register-card {
      flex-direction: column;
      max-width: 420px;
    }
    .brand-side {
      min-height: 150px;
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
      padding: 28px 24px;
    }
  }
</style>
