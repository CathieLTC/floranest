<template>
  <Teleport to="body">
    <transition name="preview-fade">
      <div v-if="visible" class="preview-overlay" @click="close">
        <div class="preview-content" @click.stop>
          <img :src="src" :alt="alt" class="preview-img" />
          <button class="preview-close" @click="close">✕</button>
        </div>
      </div>
    </transition>
  </Teleport>
</template>

<script setup>
import { watch, onUnmounted, onMounted } from 'vue';

const props = defineProps({
  visible: { type: Boolean, default: false },
  src:    { type: String, default: '' },
  alt:    { type: String, default: '' }
});

const emit = defineEmits(['update:visible']);

const close = () => emit('update:visible', false);

const onKey = (e) => { if (e.key === 'Escape') close(); };

onMounted(() => document.addEventListener('keydown', onKey));
onUnmounted(() => document.removeEventListener('keydown', onKey));

// Lock body scroll while open
watch(() => props.visible, (val) => {
  document.body.style.overflow = val ? 'hidden' : '';
});
</script>

<style scoped>
.preview-overlay {
  position: fixed;
  inset: 0;
  z-index: 10000;
  background: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: zoom-out;
}

.preview-content {
  position: relative;
  max-width: 90vw;
  max-height: 90vh;
}

.preview-img {
  max-width: 90vw;
  max-height: 90vh;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.5);
}

.preview-close {
  position: absolute;
  top: -18px;
  right: -18px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: none;
  background: white;
  color: #333;
  font-size: 18px;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: 0.2s;
}

.preview-close:hover {
  background: #f44336;
  color: white;
}

.preview-fade-enter-active,
.preview-fade-leave-active {
  transition: opacity 0.25s ease;
}

.preview-fade-enter-from,
.preview-fade-leave-to {
  opacity: 0;
}
</style>
