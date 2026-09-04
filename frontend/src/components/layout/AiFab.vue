<template>
  <div class="ai-fab">
    <!-- FAB button -->
    <button
      class="fab-btn"
      :class="{ active: isOpen }"
      @click="isOpen = !isOpen"
      title="FloraNest AI Assistant"
    >
      <span v-if="!isOpen">🤖</span>
      <span v-else>✕</span>
    </button>

    <!-- Popup panel -->
    <transition name="fab-slide">
      <div v-if="isOpen" class="fab-panel">
        <div class="fab-header">
          <span class="fab-title">✨ AI Assistant</span>
          <button class="fab-close" @click="isOpen = false">✕</button>
        </div>
        <div class="fab-body">
          <p class="fab-intro">
            Chat with our gardening expert, get plant recommendations,
            or search using natural language.
          </p>
          <router-link to="/ai" @click="isOpen = false" class="fab-link">
            <el-button type="success" size="large" class="fab-open-btn">
              Open Full AI Assistant
            </el-button>
          </router-link>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from "vue";

const isOpen = ref(false);
</script>

<style scoped>
.ai-fab {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 9999;
}

.fab-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, #2E7D32, #1B5E20);
  color: white;
  font-size: 26px;
  cursor: pointer;
  box-shadow: 0 6px 20px rgba(46,125,50,0.4);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fab-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 8px 25px rgba(46,125,50,0.55);
}

.fab-btn.active {
  background: linear-gradient(135deg, #d32f2f, #b71c1c);
}

/* Popup panel */
.fab-panel {
  position: absolute;
  bottom: 75px;
  right: 0;
  width: 340px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.18);
  overflow: hidden;
  border: 1px solid #e8f5e9;
}

.fab-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: linear-gradient(135deg, #2E7D32, #1B5E20);
  color: white;
}

.fab-title {
  font-weight: 700;
  font-size: 16px;
}

.fab-close {
  background: none;
  border: none;
  color: white;
  font-size: 18px;
  cursor: pointer;
  opacity: 0.8;
}

.fab-close:hover {
  opacity: 1;
}

.fab-body {
  padding: 20px;
}

.fab-intro {
  color: #555;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
}

.fab-link {
  text-decoration: none;
}

.fab-open-btn {
  width: 100%;
}

/* Transition */
.fab-slide-enter-active,
.fab-slide-leave-active {
  transition: all 0.3s ease;
}

.fab-slide-enter-from,
.fab-slide-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}

/* Mobile */
@media (max-width: 600px) {
  .fab-panel {
    width: 280px;
    right: -10px;
  }
}
</style>
