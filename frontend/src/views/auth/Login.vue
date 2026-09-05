<template>
<div class="login-wrapper">
  <div class="login-card">
      <!-- LEFT SIDE BRAND -->
      <div class="brand-side">
        <h1>🌿 FloraNest</h1>
        <p>Welcome back! Login to continue exploring plants.</p>
      </div>

      <!-- RIGHT SIDE FORM -->
      <div class="form-side">
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

        <p class="footer-text">Don't have an account?<router-link to="register">Register</router-link></p>
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
    height: calc(100vh - 80px);
    display: flex;
    justify-content: center;
    align-items: center;
    background: #f4fff6;
  }

  /* MAIN CARD */
  .login-card {
    width: 850px;
    height: 450px;
    display: flex;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  }

  /* LEFT BRAND SIDE */
  .brand-side {
    flex: 1;
    background: linear-gradient(135deg, #2E7D32, #1B5E20);
    color: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 40px;
  }

  .brand-side h1 {
    font-size: 32px;
    margin-bottom: 10px;
  }

  .brand-side p {
    font-size: 14px;
    opacity: 0.9;
  }

  /* RIGHT FORM SIDE */
  .form-side {
    flex: 1;
    background: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 40px;
  }

  .form-side h2 {
    margin-bottom: 20px;
    color: #2E7D32;
  }

  /* INPUTS */
  .input {
    margin-bottom: 15px;
  }

  /* LOGIN BUTTON */
  .login-btn {
    width: 100%;
    margin-top: 10px;
  }

  /* FOOTER */
  .footer-text {
    margin-top: 15px;
    font-size: 13px;
  }

  .footer-text a {
    color: #2E7D32;
    text-decoration: none;
    font-weight: bold;
  }

</style>