<template>
  <div class="plant-recommendation">
    <el-form label-position="top" class="preferences-form">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8">
          <el-form-item label="Where will you grow it?">
            <el-radio-group v-model="preferences.location">
              <el-radio-button value="indoor">Indoor</el-radio-button>
              <el-radio-button value="outdoor">Outdoor</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <el-form-item label="Your experience level">
            <el-select v-model="preferences.experience" placeholder="Select level" style="width: 100%">
              <el-option label="Beginner" value="beginner" />
              <el-option label="Intermediate" value="intermediate" />
              <el-option label="Expert" value="expert" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <el-form-item label="Available space">
            <el-select v-model="preferences.space" placeholder="Select space" style="width: 100%">
              <el-option label="Small (desk / shelf)" value="small" />
              <el-option label="Medium (floor plant)" value="medium" />
              <el-option label="Large (garden bed)" value="large" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <el-form-item label="Light conditions">
            <el-select v-model="preferences.light" placeholder="Select light" style="width: 100%">
              <el-option label="Low light" value="low" />
              <el-option label="Bright indirect" value="bright" />
              <el-option label="Direct sunlight" value="direct" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <el-form-item label="Primary goal">
            <el-select v-model="preferences.purpose" placeholder="What's your goal?" style="width: 100%">
              <el-option label="Decoration" value="decoration" />
              <el-option label="Air purifying" value="air-purifying" />
              <el-option label="Herbs & cooking" value="herbs" />
              <el-option label="Low maintenance" value="low-maintenance" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-button type="success" size="large" :loading="loading" @click="handleRecommend">
        <el-icon><Star /></el-icon>
        Get AI Recommendations
      </el-button>
    </el-form>

    <div v-if="loading" class="state-block">
      <el-skeleton :rows="4" animated />
      <p class="loading-text">Analyzing your preferences…</p>
    </div>

    <div v-else-if="recommendations.length" class="results">
      <div class="results-header">
        <h3>Recommended for you</h3>
        <el-tag type="success">{{ recommendations.length }} matches</el-tag>
      </div>

      <el-row :gutter="20">
        <el-col
          v-for="plant in recommendations"
          :key="plant.id"
          :xs="24"
          :sm="12"
          :lg="6"
        >
          <RecommendationCard
            :plant="plant"
            @view-details="onViewDetails"
            @add-to-shop="onAddToShop"
          />
        </el-col>
      </el-row>
    </div>

    <div v-else class="state-block hint">
      <el-icon :size="32"><Collection /></el-icon>
      <p>Tell us about your space and experience — we'll suggest plants tailored to your needs.</p>
    </div>
  </div>
</template>

<script setup>
  import { ref, reactive } from "vue";
  import { useRouter } from "vue-router";
  import { Star, Collection } from "@element-plus/icons-vue";
  import { ElMessage } from "element-plus";
  import RecommendationCard from "./RecommendationCard.vue";
  import { getPlantRecommendations } from "@/api/ai";

  const router = useRouter();

  const loading = ref(false);
  const recommendations = ref([]);
  const preferences = reactive({
    location: "indoor",
    experience: "beginner",
    space: "medium",
    light: "bright",
    purpose: "decoration",
  });

  async function handleRecommend() {
    loading.value = true;

    try {
      recommendations.value = await getPlantRecommendations({ ...preferences });
    } catch {
      ElMessage.error("Could not generate recommendations. Please try again.");
      recommendations.value = [];
    } finally {
      loading.value = false;
    }
  }

  function onViewDetails(plant) {
    ElMessage.info(`Details for ${plant.name} — connect product API to enable.`);
  }

  function onAddToShop() {
    router.push("/shop");
  }
</script>

<style scoped>
  .plant-recommendation {
    display: flex;
    flex-direction: column;
    gap: 24px;
  }

  .preferences-form {
    background: #fafafa;
    padding: 24px;
    border-radius: 12px;
    border: 1px solid #e8f5e9;
  }

  .results-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    margin-bottom: 16px;
  }

  .results-header h3 {
    margin: 0;
    color: #1b5e20;
    font-size: 1.15rem;
  }

  .results .el-col {
    margin-bottom: 20px;
  }

  .state-block {
    padding: 32px 16px;
    text-align: center;
    color: #666;
  }

  .loading-text {
    margin-top: 12px;
    color: #2e7d32;
    font-weight: 500;
  }

  .state-block.hint {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    background: #fafafa;
    border-radius: 12px;
    border: 1px dashed #c8e6c9;
  }

  .state-block.hint p {
    max-width: 420px;
    margin: 0;
    line-height: 1.5;
  }
</style>
