<template>
  <div class="message" :class="message.role">

    <!-- Avatar -->
    <div class="avatar">
      <span v-if="message.role === 'assistant'">🌿</span>
      <span v-else>👤</span>
    </div>

    <!-- Bubble -->
    <div class="bubble">
      <p>{{ message.content }}</p>
      <span class="time">{{ formattedTime }}</span>
    </div>

  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  message: { type: Object, required: true }
  // message shape: { role: "user"|"assistant", content: String, timestamp: ISO String }
});

const formattedTime = computed(() => {
  if (!props.message.timestamp) return "";
  return new Date(props.message.timestamp).toLocaleTimeString([], {
    hour:   "2-digit",
    minute: "2-digit"
  });
});
</script>

<style scoped>
.message {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  align-items: flex-end;
}

/* User messages — push to right */
.message.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #e8f5e9;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.message.user .avatar {
  background: #c8e6c9;
}

.bubble {
  max-width: 75%;
  padding: 12px 16px;
  border-radius: 16px;
  background: #f1f8e9;
  border: 1px solid #c8e6c9;
}

.message.user .bubble {
  background: #2E7D32;
  border-color: #2E7D32;
  color: white;
  border-radius: 16px 16px 4px 16px;
}

.message.assistant .bubble {
  border-radius: 16px 16px 16px 4px;
}

.bubble p {
  margin: 0 0 4px;
  line-height: 1.6;
  font-size: 14px;
  white-space: pre-wrap;
}

.message.user .bubble p { color: white; }

.time {
  font-size: 11px;
  color: #999;
  display: block;
  text-align: right;
}

.message.user .time { color: rgba(255,255,255,0.7); }
</style>