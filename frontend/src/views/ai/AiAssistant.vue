<template>
  <div class="ai-page">

    <!-- Page header -->
    <div class="page-header">
      <el-tag type="success" effect="plain" round>✨ Powered by DeepSeek AI</el-tag>
      <h1>FloraNest AI Assistant</h1>
      <p>Chat with our gardening expert, get personalised plant recommendations,
         or search using natural language.</p>
    </div>

    <!-- Three AI features in tabs -->
    <div class="content">
      <el-tabs v-model="activeTab" stretch>

        <!-- Tab 1: Gardening Chat -->
        <el-tab-pane name="chat">
          <template #label>
            <span class="tab-label">💬 Gardening Chat</span>
          </template>
          <div class="tab-intro">
            <h2>Ask the Gardening Assistant</h2>
            <p>Get instant advice on watering, light, pests, fertilising and more.</p>
          </div>
          <GardeningChat />
        </el-tab-pane>

        <!-- Tab 2: Plant Recommendations -->
        <el-tab-pane name="recommend">
          <template #label>
            <span class="tab-label">🌱 Recommendations</span>
          </template>
          <div class="tab-intro">
            <h2>Find Your Perfect Plant</h2>
            <p>Answer a few quick questions and our AI will suggest the best plants for your space and lifestyle.</p>
          </div>
          <PlantRecommendation />
        </el-tab-pane>

        <!-- Tab 3: Smart Search -->
        <el-tab-pane name="search">
          <template #label>
            <span class="tab-label">🔍 Smart Search</span>
          </template>
          <div class="tab-intro">
            <h2>Search in Natural Language</h2>
            <p>Instead of typing plant names, describe what you want — our AI finds the best matches.</p>
          </div>
          <SmartSearch />
        </el-tab-pane>

      </el-tabs>
    </div>

  </div>
</template>

<script setup>
import { ref } from "vue";

// All three AI components — paths match the folder structure
import GardeningChat       from "@/components/ai/GardeningChat.vue";
import PlantRecommendation from "@/components/ai/PlantRecommendation.vue";
import SmartSearch         from "@/components/ai/SmartSearch.vue";

const activeTab = ref("chat");
</script>

<style scoped>
.ai-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #f1f8e9 0%, #ffffff 300px);
  padding-bottom: 60px;
}

.page-header {
  text-align: center;
  padding: 48px 24px 32px;
}

.page-header .el-tag { margin-bottom: 16px; font-weight: 600; }

.page-header h1 {
  font-size: clamp(1.8rem, 4vw, 2.6rem);
  color: #1B5E20;
  margin: 0 0 12px;
}

.page-header p {
  color: #555;
  font-size: 1.05rem;
  max-width: 580px;
  margin: 0 auto;
  line-height: 1.6;
}

.content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(46,125,50,0.08);
  border: 1px solid #e8f5e9;
}

.tab-label { font-weight: 600; font-size: 14px; }

.tab-intro {
  padding: 20px 0 16px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 24px;
}

.tab-intro h2 { margin: 0 0 6px; color: #2E7D32; font-size: 1.2rem; }
.tab-intro p  { margin: 0; color: #666; font-size: 0.95rem; }

:deep(.el-tabs__item.is-active) { color: #2E7D32; font-weight: 700; }
:deep(.el-tabs__active-bar)     { background: #2E7D32; }
:deep(.el-tabs__content)        { padding: 0 20px 24px; }

@media (max-width: 640px) {
  .page-header { padding: 32px 16px 24px; }
  .content     { padding: 0 12px; border-radius: 12px; }
}
</style>r
