<template>
  <div class="gardening-chat">
    <div ref="messagesContainer" class="messages">
      <ChatMessage
        v-for="(msg, index) in messages"
        :key="index"
        :message="msg"
      />

      <div v-if="loading" class="typing-indicator">
        <el-avatar :size="36"><el-icon><Cpu /></el-icon></el-avatar>
        <div class="typing-dots">
          <span></span><span></span><span></span>
        </div>
      </div>
    </div>

    <div class="quick-prompts">
      <span class="prompt-label">Try asking:</span>
      <el-tag
        v-for="prompt in quickPrompts"
        :key="prompt"
        class="prompt-tag"
        effect="plain"
        round
        @click="sendQuickPrompt(prompt)"
      >
        {{ prompt }}
      </el-tag>
    </div>

    <div class="input-area">
      <el-input
        v-model="input"
        type="textarea"
        :rows="2"
        placeholder="Ask anything about plant care, watering, pests, or repotting…"
        resize="none"
        :disabled="loading"
        @keydown.enter.exact.prevent="handleSend"
      />
      <el-button
        type="success"
        :disabled="!input.trim() || loading"
        :loading="loading"
        @click="handleSend"
      >
        <el-icon><Promotion /></el-icon>
        Send
      </el-button>
    </div>
  </div>
</template>

<script setup>
  import { ref, nextTick, onMounted } from "vue";
  import { Cpu, Promotion } from "@element-plus/icons-vue";
  import { ElMessage } from "element-plus";
  import ChatMessage from "./ChatMessage.vue";
  import { sendChatMessage } from "@/api/ai";

  const messages = ref([]);
  const input = ref("");
  const loading = ref(false);
  const messagesContainer = ref(null);

  const quickPrompts = [
    "How often should I water?",
    "Why are leaves turning yellow?",
    "Best plants for low light?",
    "When should I repot?",
  ];

  onMounted(() => {
    messages.value.push({
      role: "assistant",
      content:
        "Hello! I'm FloraNest AI, your gardening assistant. Ask me about watering schedules, light requirements, troubleshooting, or plant recommendations.",
      timestamp: new Date().toISOString(),
    });
  });

  async function handleSend() {
    const text = input.value.trim();
    if (!text || loading.value) return;

    const userMessage = {
      role: "user",
      content: text,
      timestamp: new Date().toISOString(),
    };

    messages.value.push(userMessage);
    input.value = "";
    loading.value = true;
    scrollToBottom();

    try {
      const reply = await sendChatMessage(text, messages.value);
      messages.value.push(reply);
    } catch {
      ElMessage.error("Failed to get a response. Please try again.");
    } finally {
      loading.value = false;
      scrollToBottom();
    }
  }

  function sendQuickPrompt(prompt) {
    input.value = prompt;
    handleSend();
  }

  async function scrollToBottom() {
    await nextTick();
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  }
</script>

<style scoped>
  .gardening-chat {
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: 100%;
    min-height: 480px;
  }

  .messages {
    flex: 1;
    overflow-y: auto;
    padding: 16px;
    background: #fff;
    border: 1px solid #e8f5e9;
    border-radius: 12px;
    min-height: 320px;
    max-height: 420px;
  }

  .typing-indicator {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
  }

  .typing-indicator .el-avatar {
    background: #e8f5e9;
    color: #2e7d32;
  }

  .typing-dots {
    display: flex;
    gap: 4px;
    padding: 12px 16px;
    background: #f5f7fa;
    border-radius: 14px;
  }

  .typing-dots span {
    width: 8px;
    height: 8px;
    background: #2e7d32;
    border-radius: 50%;
    animation: bounce 1.4s infinite ease-in-out both;
  }

  .typing-dots span:nth-child(1) { animation-delay: -0.32s; }
  .typing-dots span:nth-child(2) { animation-delay: -0.16s; }

  @keyframes bounce {
    0%, 80%, 100% { transform: scale(0.6); opacity: 0.5; }
    40% { transform: scale(1); opacity: 1; }
  }

  .quick-prompts {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 8px;
  }

  .prompt-label {
    font-size: 0.85rem;
    color: #888;
  }

  .prompt-tag {
    cursor: pointer;
    transition: all 0.2s;
  }

  .prompt-tag:hover {
    background: #e8f5e9;
    border-color: #2e7d32;
    color: #2e7d32;
  }

  .input-area {
    display: flex;
    gap: 12px;
    align-items: flex-end;
  }

  .input-area .el-textarea {
    flex: 1;
  }

  @media (max-width: 640px) {
    .input-area {
      flex-direction: column;
      align-items: stretch;
    }

    .messages {
      max-height: 360px;
    }
  }
</style>
