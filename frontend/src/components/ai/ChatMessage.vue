<template>
  <div class="chat-message" :class="message.role">
    <div class="avatar">
      <el-avatar :size="36" :class="message.role">
        <el-icon v-if="message.role === 'assistant'"><Cpu /></el-icon>
        <el-icon v-else><User /></el-icon>
      </el-avatar>
    </div>

    <div class="bubble">
      <div class="bubble-header">
        <span class="sender">{{ message.role === "assistant" ? "FloraNest AI" : "You" }}</span>
        <span class="time">{{ formattedTime }}</span>
      </div>
      <p class="content">{{ message.content }}</p>
    </div>
  </div>
</template>

<script setup>
  import { computed } from "vue";
  import { Cpu, User } from "@element-plus/icons-vue";

  const props = defineProps({
    message: {
      type: Object,
      required: true,
    },
  });

  const formattedTime = computed(() => {
    if (!props.message.timestamp) return "";
    return new Date(props.message.timestamp).toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    });
  });
</script>

<style scoped>
  .chat-message {
    display: flex;
    gap: 12px;
    margin-bottom: 16px;
    max-width: 85%;
  }

  .chat-message.user {
    flex-direction: row-reverse;
    margin-left: auto;
  }

  .avatar .el-avatar {
    background: #e8f5e9;
    color: #2e7d32;
  }

  .avatar .el-avatar.user {
    background: #2e7d32;
    color: #fff;
  }

  .bubble {
    background: #f5f7fa;
    border-radius: 14px;
    padding: 12px 16px;
    flex: 1;
  }

  .chat-message.user .bubble {
    background: #e8f5e9;
  }

  .bubble-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    margin-bottom: 6px;
  }

  .sender {
    font-weight: 600;
    font-size: 0.82rem;
    color: #2e7d32;
  }

  .chat-message.user .sender {
    color: #1b5e20;
  }

  .time {
    font-size: 0.75rem;
    color: #999;
    white-space: nowrap;
  }

  .content {
    margin: 0;
    line-height: 1.55;
    color: #333;
    font-size: 0.95rem;
    white-space: pre-wrap;
  }
</style>
