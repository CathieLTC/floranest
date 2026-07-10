<template>
  <div class="plant-recommendation">

    <!-- Preference form -->
    <div class="form-card">
      <el-row :gutter="20">

        <el-col :xs="24" :sm="12" :md="8">
          <div class="field">
            <label>Where will you grow it?</label>
            <el-radio-group v-model="preferences.location">
              <el-radio-button value="indoor">🏠 Indoor</el-radio-button>
              <el-radio-button value="outdoor">🌳 Outdoor</el-radio-button>
            </el-radio-group>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <div class="field">
            <label>Your experience level</label>
            <el-select v-model="preferences.experience" style="width:100%">
              <el-option label="🌱 Beginner"     value="beginner" />
              <el-option label="🌿 Intermediate"  value="intermediate" />
              <el-option label="🌳 Experienced"   value="expert" />
            </el-select>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <div class="field">
            <label>Available space</label>
            <el-select v-model="preferences.space" style="width:100%">
              <el-option label="Small (desk / shelf)"  value="small" />
              <el-option label="Medium (floor plant)"  value="medium" />
              <el-option label="Large (garden bed)"    value="large" />
            </el-select>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <div class="field">
            <label>Light conditions</label>
            <el-select v-model="preferences.light" style="width:100%">
              <el-option label="Low light"        value="low light" />
              <el-option label="Bright indirect"  value="bright indirect" />
              <el-option label="Direct sunlight"  value="direct sunlight" />
            </el-select>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <div class="field">
            <label>How often can you water?</label>
            <el-select v-model="preferences.watering" style="width:100%">
              <el-option label="Rarely (once a week+)" value="rarely" />
              <el-option label="Occasionally (2-3x/week)" value="occasionally" />
              <el-option label="Daily"                 value="daily" />
            </el-select>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="8">
          <div class="field">
            <label>Primary goal</label>
            <el-select v-model="preferences.purpose" style="width:100%">
              <el-option label="🎨 Decoration"        value="decoration" />
              <el-option label="💨 Air purifying"     value="air purifying" />
              <el-option label="🌿 Herbs & cooking"   value="herbs and cooking" />
              <el-option label="😌 Low maintenance"   value="low maintenance" />
            </el-select>
          </div>
        </el-col>

      </el-row>

      <el-button
        type="success"
        size="large"
        :loading="loading"
        class="recommend-btn"
        @click="handleRecommend"
      >
        ✨ Get AI Recommendations
      </el-button>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="state-block">
      <el-skeleton :rows="3" animated />
      <p class="loading-text">Analysing your preferences…</p>
    </div>

    <!-- Results -->
    <div v-else-if="results.length" class="results">
      <div class="results-header">
        <h3>Recommended for you</h3>
        <el-tag type="success">{{ results.length }} matches</el-tag>
      </div>
      <el-row :gutter="20">
        <el-col
          v-for="plant in results"
          :key="plant.id"
          :xs="24" :sm="12" :lg="8"
          style="margin-bottom: 20px"
        >
          <RecommendationCard
            :plant="plant"
            @view-details="onViewDetails"
            @add-to-shop="onAddToShop"
          />
        </el-col>
      </el-row>
    </div>

    <!-- Empty state -->
    <div v-else class="state-block hint">
      <p>🌱 Tell us about your space and experience — we'll suggest the perfect plants for you.</p>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import RecommendationCard from "./RecommendationCard.vue";
import { getPlantRecommendations } from "@/api/ai";

const router  = useRouter();
const loading = ref(false);
const results = ref([]);

const preferences = reactive({
  location:   "indoor",
  experience: "beginner",
  space:      "medium",
  light:      "bright indirect",
  watering:   "occasionally",
  purpose:    "decoration"
});

async function handleRecommend() {
  loading.value = true;
  results.value = [];

  try {
    results.value = await getPlantRecommendations({ ...preferences });
  } catch {
    ElMessage.error("Could not generate recommendations. Please try again.");
  } finally {
    loading.value = false;
  }
}

function onViewDetails(plant) {
  if (plant.id && !String(plant.id).startsWith("ai-")) {
    router.push(`/product/${plant.id}`);
  } else {
    ElMessage.info(`Search for "${plant.name}" in the shop to find it.`);
  }
}

function onAddToShop() {
  router.push("/shop");
}
</script>

<style scoped>
.plant-recommendation { display: flex; flex-direction: column; gap: 24px; }

.form-card {
  background: #fafafa;
  padding: 24px;
  border-radius: 14px;
  border: 1px solid #e8f5e9;
}

.field { margin-bottom: 20px; }

.field label {
  display: block;
  font-weight: 600;
  font-size: 13px;
  color: #333;
  margin-bottom: 8px;
}

.recommend-btn { width: 100%; margin-top: 8px; }

.results-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.results-header h3 { margin: 0; color: #1B5E20; }

.loading-text {
  text-align: center;
  color: #2E7D32;
  margin-top: 12px;
  font-weight: 500;
}

.state-block {
  text-align: center;
  padding: 32px;
  color: #666;
  background: #fafafa;
  border-radius: 12px;
  border: 1px dashed #c8e6c9;
}
</style>