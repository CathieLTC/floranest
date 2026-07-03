<template>
  <div class="register-wrapper">

    <div class="register-card">

      <!-- LEFT BRAND SIDE -->
      <div class="brand-side">
        <h1>🌿 FloraNest</h1>
        <p>Create your account and start exploring plants.</p>
      </div>

      <!-- RIGHT FORM SIDE -->
      <div class="form-side">

        <h2>Register</h2>

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
  import { User, Message, Lock} from "@element-plus/icons-vue";
  import { ElMessage,  ElMessageBox} from "element-plus";
  import { useRouter } from "vue-router";
  import api from "@/api/axios";

  const name = ref("");
  const email = ref("");
  const password = ref("");
  const confirmPassword = ref("");
  const router = useRouter();

  const handleRegister = async () => {
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

        await ElMessageBox.confirm(
            "Registration successful! Go to the login page?",
            "Success",
            {
                confirmButtonText: "Login",
                cancelButtonText: "Stay Here",
                type: "success",
            }
        );

        router.push("/login");

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
  height: calc(100vh - 80px);
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f4fff6;
}

/* CARD */
.register-card {
  width: 900px;
  height: 500px;
  display: flex;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

/* LEFT SIDE */
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

/* RIGHT SIDE */
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

/* BUTTON */
.register-btn {
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
  font-weight: bold;
  text-decoration: none;
}

</style>