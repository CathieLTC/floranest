<template>
  <div class="ai-assistant-page">
    <section class="page-header">
      <div class="header-content">
        <el-tag type="success" effect="plain" round class="badge">
          <el-icon><Cpu /></el-icon>
          Powered by AI
        </el-tag>
        <h1>FloraNest AI Assistant</h1>
        <p>
          Search smarter, get personalized plant recommendations, and chat with our
          gardening expert — all in one place.
        </p>
      </div>
    </section>

    <section class="features-section">
      <el-tabs v-model="activeTab" class="ai-tabs" stretch>
        <el-tab-pane name="search">
          <template #label>
            <span class="tab-label">
              <el-icon><Search /></el-icon>
              Smart Search
            </span>
          </template>
          <div class="tab-panel">
            <div class="panel-intro">
              <h2>Smart Plant Search</h2>
              <p>Describe what you're looking for in plain language and let AI find the best matches.</p>
            </div>
            <SmartSearch />
          </div>
        </el-tab-pane>

        <el-tab-pane name="recommend">
          <template #label>
            <span class="tab-label">
              <el-icon><Star /></el-icon>
              Recommendations
            </span>
          </template>
          <div class="tab-panel">
            <div class="panel-intro">
              <h2>Personalized Recommendations</h2>
              <p>Share your space, experience, and goals — we'll suggest plants that fit your lifestyle.</p>
            </div>
            <PlantRecommendation />
          </div>
        </el-tab-pane>

        <el-tab-pane name="chat">
          <template #label>
            <span class="tab-label">
              <el-icon><ChatDotRound /></el-icon>
              Gardening Chat
            </span>
          </template>
          <div class="tab-panel">
            <div class="panel-intro">
              <h2>Gardening Chat</h2>
              <p>Ask questions about care, troubleshooting, and seasonal tips anytime.</p>
            </div>
            <GardeningChat />
          </div>
        </el-tab-pane>
      </el-tabs>
    </section>

    <section class="info-banner">
      <el-icon><InfoFilled /></el-icon>
      <span>
        Gardening Chat is powered by a real AI model and can answer open-ended plant questions. Search and recommendations use live catalog data.
      </span>
    </section>
  </div>
</template>

<script setup>
  import { ref } from "vue";
  import {
    Cpu,
    Search,
    Star,
    ChatDotRound,
    InfoFilled,
  } from "@element-plus/icons-vue";
  import SmartSearch from "@/components/ai/SmartSearch.vue";
  import PlantRecommendation from "@/components/ai/PlantRecommendation.vue";
  import GardeningChat from "@/components/ai/GardeningChat.vue";

  const activeTab = ref("search");
</script>

<style scoped>
  .ai-assistant-page {
    min-height: calc(100vh - 80px);
    background: linear-gradient(180deg, #f1f8e9 0%, #fafafa 280px);
    padding-bottom: 48px;
  }

  .page-header {
    padding: 48px 24px 32px;
    text-align: center;
  }

  .header-content {
    max-width: 640px;
    margin: 0 auto;
  }

  .badge {
    margin-bottom: 16px;
    font-weight: 600;
  }

  .page-header h1 {
    margin: 0 0 12px;
    font-size: clamp(1.75rem, 4vw, 2.5rem);
    color: #1b5e20;
  }

  .page-header p {
    margin: 0;
    color: #555;
    font-size: 1.05rem;
    line-height: 1.6;
  }

  .features-section {
    max-width: 1100px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .ai-tabs {
    background: #fff;
    border-radius: 16px;
    padding: 8px 24px 24px;
    box-shadow: 0 4px 24px rgba(46, 125, 50, 0.08);
    border: 1px solid #e8f5e9;
  }

  .tab-label {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-weight: 600;
  }

  .tab-panel {
    padding-top: 8px;
  }

  .panel-intro {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }

  .panel-intro h2 {
    margin: 0 0 6px;
    font-size: 1.25rem;
    color: #2e7d32;
  }

  .panel-intro p {
    margin: 0;
    color: #666;
    font-size: 0.95rem;
  }

  .info-banner {
    max-width: 1100px;
    margin: 24px auto 0;
    padding: 12px 20px;
    display: flex;
    align-items: center;
    gap: 10px;
    background: #fff8e1;
    border: 1px solid #ffe082;
    border-radius: 10px;
    color: #795548;
    font-size: 0.88rem;
  }

  .info-banner code {
    background: rgba(0, 0, 0, 0.06);
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 0.85rem;
  }

  :deep(.el-tabs__item.is-active) {
    color: #2e7d32;
  }

  :deep(.el-tabs__active-bar) {
    background-color: #2e7d32;
  }

  @media (max-width: 640px) {
    .page-header {
      padding: 32px 16px 24px;
    }

    .features-section {
      padding: 0 12px;
    }

    .ai-tabs {
      padding: 8px 12px 16px;
    }

    .info-banner {
      margin-left: 12px;
      margin-right: 12px;
      flex-direction: column;
      text-align: center;
    }
  }
</style>
