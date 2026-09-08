<template>
  <div class="smart-search">

    <!-- Search input -->
    <div class="search-bar">
      <el-input
        v-model="query"
        placeholder="e.g. low light plant for small apartment, easy plant for beginners…"
        size="large"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="success" size="large" :loading="loading" @click="handleSearch">
        AI Search
      </el-button>
    </div>

    <!-- Optional filters -->
    <div class="filters">
      <el-select v-model="filters.category" placeholder="Indoor / Outdoor" clearable>
        <el-option label="Indoor"  value="indoor" />
        <el-option label="Outdoor" value="outdoor" />
      </el-select>

      <el-select v-model="filters.light" placeholder="Light needs" clearable>
        <el-option label="Low light"       value="low" />
        <el-option label="Bright indirect" value="bright" />
        <el-option label="Full sun"        value="full sun" />
      </el-select>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="state-block">
      <el-skeleton :rows="3" animated />
    </div>

    <!-- No results -->
    <div v-else-if="searched && results.length === 0" class="state-block">
      <el-empty description="No plants matched your search. Try different keywords." />
    </div>

    <!-- Results -->
    <div v-else-if="results.length">
      <p class="results-count">
        Found <strong>{{ results.length }}</strong> plant{{ results.length === 1 ? "" : "s" }}
      </p>
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

    <!-- Hint before any search -->
    <div v-else class="state-block hint">
      <p>🔍 Try: "plants that survive in low light" or "easy plants for beginners under $50"</p>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import RecommendationCard from "./RecommendationCard.vue";
import { searchPlants } from "@/api/ai";

const router  = useRouter();
const query   = ref("");
const loading = ref(false);
const searched = ref(false);
const results  = ref([]);

const filters = reactive({ category: "", light: "" });

async function handleSearch() {
  if (!query.value.trim() && !filters.category && !filters.light) {
    ElMessage.warning("Enter a search term or choose a filter.");
    return;
  }

  loading.value  = true;
  searched.value = true;
  results.value  = [];

  try {
    results.value = await searchPlants(query.value, { ...filters });
  } catch {
    ElMessage.error("Search failed. Please check your connection and try again.");
  } finally {
    loading.value = false;
  }
}

function onViewDetails(plant) {
  if (plant.id) router.push(`/product/${plant.id}`);
}

function onAddToShop() {
  router.push("/shop");
}
</script>

<style scoped>
.smart-search { display: flex; flex-direction: column; gap: 16px; }

.search-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.search-bar .el-input { flex: 1; min-width: 240px; }

.filters { display: flex; gap: 12px; flex-wrap: wrap; }
.filters .el-select { width: 180px; }

.results-count { color: #666; margin: 0 0 16px; }

.state-block {
  padding: 32px;
  text-align: center;
  color: #666;
}

.state-block.hint {
  background: #fafafa;
  border-radius: 12px;
  border: 1px dashed #c8e6c9;
}
</style>