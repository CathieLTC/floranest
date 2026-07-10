<template>
  <div class="gardening-chat">

    <!-- Message list -->
    <div class="messages" ref="messagesEl">
      <ChatMessage
        v-for="(msg, i) in messages"
        :key="i"
        :message="msg"
      />

      <!-- Typing indicator -->
      <div v-if="loading" class="message assistant">
        <div class="avatar">🌿</div>
        <div class="bubble typing">
          <span></span><span></span><span></span>
        </div>
      </div>
    </div>

    <!-- Quick prompt suggestions -->
    <div class="quick-prompts">
      <el-tag
        v-for="prompt in quickPrompts"
        :key="prompt"
        class="prompt-tag"
        effect="plain"
        type="success"
        @click="sendQuickPrompt(prompt)"
      >
        {{ prompt }}
      </el-tag>
    </div>

    <!-- Input area -->
    <div class="input-area">
      <el-input
        v-model="inputText"
        placeholder="Ask about plant care, watering, pests…"
        :disabled="loading"
        @keyup.enter="handleSend"
      />
      <el-button
        type="success"
        :loading="loading"
        :disabled="!inputText.trim()"
        @click="handleSend"
      >
        Send
      </el-button>
    </div>

  </div>
</template>

<script setup>
import { ref, nextTick } from "vue";
import { ElMessage } from "element-plus";
import ChatMessage from "./ChatMessage.vue";
import { sendChatMessage } from "@/api/ai";

const messagesEl = ref(null);
const inputText  = ref("");
const loading    = ref(false);

// First message is a greeting — always shown, never sent to the AI
const messages = ref([
  {
    role:      "assistant",
    content:   "Hi! 🌿 I'm your FloraNest gardening assistant. Ask me anything about plant care, watering schedules, pests, or which plants suit your space!",
    timestamp: new Date().toISOString()
  }
]);

const quickPrompts = [
  "How often should I water a snake plant?",
  "Which plants are good for low light?",
  "Why are my plant's leaves turning yellow?",
  "Best plants for beginners?"
];

async function handleSend() {
  const text = inputText.value.trim();
  if (!text || loading.value) return;

  // Add user message to chat
  messages.value.push({
    role:      "user",
    content:   text,
    timestamp: new Date().toISOString()
  });

  inputText.value = "";
  loading.value   = true;
  scrollToBottom();

  try {
    const reply = await sendChatMessage(text, messages.value);
    messages.value.push(reply);
  } catch (e) {
    ElMessage.error("Could not get a response. Please try again.");
    console.error(e);
  } finally {
    loading.value = false;
    scrollToBottom();
  }
}

function sendQuickPrompt(prompt) {
  inputText.value = prompt;
  handleSend();
}

async function scrollToBottom() {
  await nextTick();
  if (messagesEl.value) {
    messagesEl.value.scrollTop = messagesEl.value.scrollHeight;
  }
}
</script>

<style scoped>
.gardening-chat {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* Message list */
.messages {
  height: 380px;
  overflow-y: auto;
  padding: 16px;
  background: #fafafa;
  border-radius: 12px;
  border: 1px solid #e8f5e9;
  scroll-behavior: smooth;
}

/* Typing dots animation */
.bubble.typing {
  display: flex;
  gap: 5px;
  align-items: center;
  padding: 14px 18px;
}

.bubble.typing span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #2E7D32;
  animation: bounce 1.2s infinite;
}

.bubble.typing span:nth-child(2) { animation-delay: 0.2s; }
.bubble.typing span:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; }
  40%           { transform: scale(1.2); opacity: 1; }
}

/* Quick prompts */
.quick-prompts {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.prompt-tag {
  cursor: pointer;
  transition: 0.2s;
}

.prompt-tag:hover {
  background: #2E7D32;
  color: white;
}

/* Input */
.input-area {
  display: flex;
  gap: 10px;
}

.input-area .el-input { flex: 1; }
</style>