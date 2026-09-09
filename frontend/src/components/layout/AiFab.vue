<template>
  <div class="ai-fab">
    <!-- FAB button -->
    <button
      class="fab-btn"
      :class="{ active: isOpen }"
      @click="isOpen = !isOpen"
      title="FloraNest AI Assistant"
    >
      <span v-if="!isOpen" class="fab-pulse"></span>
      <svg v-if="!isOpen" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M12 2C6.5 2 2 5.5 2 10C2 12.5 3.5 14.5 5.5 15.5L4 20L9 17.5C10 17.8 11 18 12 18C17.5 18 22 14.5 22 10C22 5.5 17.5 2 12 2Z"/>
        <circle cx="8" cy="10" r="1" fill="currentColor"/>
        <circle cx="12" cy="10" r="1" fill="currentColor"/>
        <circle cx="16" cy="10" r="1" fill="currentColor"/>
      </svg>
      <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round">
        <path d="M6 6L18 18M6 18L18 6"/>
      </svg>
    </button>

    <!-- Popup panel -->
    <transition name="fab-slide">
      <div v-if="isOpen" class="fab-panel">
        <div class="fab-header">
          <div class="fab-header-left">
            <span class="fab-header-icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M12 2L13.5 7.5L19 9L13.5 10.5L12 16L10.5 10.5L5 9L10.5 7.5L12 2Z"/>
              </svg>
            </span>
            <span class="fab-title">AI Assistant</span>
          </div>
          <button class="fab-close" @click="isOpen = false">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
              <path d="M6 6L18 18M6 18L18 6"/>
            </svg>
          </button>
        </div>
        <div class="fab-body">
          <p class="fab-intro">
            Chat with our gardening expert, get plant recommendations,
            or search using natural language.
          </p>
          <router-link to="/ai" @click="isOpen = false" class="fab-link">
            Open Full AI Assistant
            <span class="fab-link-arrow">→</span>
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
  bottom: 28px;
  right: 28px;
  z-index: 9999;
}

.fab-btn {
  position: relative;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, var(--fn-green-600), var(--fn-green-700));
  color: white;
  cursor: pointer;
  box-shadow: 0 6px 24px rgba(31, 98, 55, 0.35);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fab-btn:hover {
  transform: scale(1.08);
  box-shadow: 0 8px 30px rgba(31, 98, 55, 0.45);
}

.fab-btn.active {
  background: linear-gradient(135deg, #444, #2a2a2a);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
}

/* Pulse animation when idle */
.fab-pulse {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  border: 2px solid var(--fn-green-400);
  animation: fabPulse 2.5s ease-out infinite;
}

@keyframes fabPulse {
  0% { transform: scale(1); opacity: 0.6; }
  100% { transform: scale(1.8); opacity: 0; }
}

/* Popup panel */
.fab-panel {
  position: absolute;
  bottom: 72px;
  right: 0;
  width: 320px;
  background: #fff;
  border-radius: var(--fn-radius-md);
  box-shadow: var(--fn-shadow-xl);
  overflow: hidden;
  border: 1px solid var(--fn-border);
}

.fab-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 18px;
  background: linear-gradient(135deg, var(--fn-green-700), var(--fn-green-600));
  color: white;
}

.fab-header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.fab-header-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: var(--fn-radius-sm);
}

.fab-title {
  font-weight: 700;
  font-size: 15px;
}

.fab-close {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px;
  border-radius: var(--fn-radius-sm);
  transition: all 0.15s;
}

.fab-close:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.15);
}

.fab-body {
  padding: 20px 18px;
}

.fab-intro {
  color: var(--fn-text-2);
  font-size: 13px;
  line-height: 1.6;
  margin-bottom: 16px;
}

.fab-link {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  background: var(--fn-green-600);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  border-radius: var(--fn-radius);
  text-decoration: none;
  transition: all 0.25s ease;
}

.fab-link:hover {
  background: var(--fn-green-700);
  color: #fff;
  transform: translateY(-1px);
}

.fab-link-arrow {
  transition: transform 0.25s ease;
}

.fab-link:hover .fab-link-arrow {
  transform: translateX(3px);
}

/* Transition */
.fab-slide-enter-active,
.fab-slide-leave-active {
  transition: all 0.3s ease;
}

.fab-slide-enter-from,
.fab-slide-leave-to {
  opacity: 0;
  transform: translateY(16px) scale(0.95);
}

/* Mobile */
@media (max-width: 600px) {
  .fab-panel {
    width: 280px;
    right: -8px;
  }
  .ai-fab {
    bottom: 20px;
    right: 20px;
  }
}
</style>
