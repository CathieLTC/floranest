<template>
  <div class="smart-search">
    <div class="search-bar">
      <el-input
        v-model="query"
        placeholder="Describe the plant you're looking for… e.g. low-light indoor plant"
        size="large"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="success" size="large" :loading="loading" @click="handleSearch">
        <el-icon><MagicStick /></el-icon>
        AI Search
      </el-button>
    </div>

    <div class="filters">
      <el-select v-model="filters.category" placeholder="Location" clearable>
        <el-option label="Indoor" value="Indoor" />
        <el-option label="Outdoor" value="Outdoor" />
      </el-select>

      <el-select v-model="filters.careLevel" placeholder="Care level" clearable>
        <el-option label="Easy" value="Easy" />
        <el-option label="Moderate" value="Moderate" />
      </el-select>

      <el-select v-model="filters.light" placeholder="Light needs" clearable>
        <el-option label="Low light" value="Low" />
        <el-option label="Bright indirect" value="Bright" />
        <el-option label="Full sun" value="Full sun" />
      </el-select>
    </div>

    <div v-if="loading" class="state-block">
      <el-skeleton :rows="3" animated />
    </div>

    <div v-else-if="searched && results.length === 0" class="state-block empty">
      <el-empty description="No plants matched your search. Try different keywords or filters." />
    </div>

    <div v-else-if="results.length" class="results">
      <p class="results-count">
        Found <strong>{{ results.length }}</strong> plant{{ results.length === 1 ? "" : "s" }}
      </p>
      <el-row :gutter="20">
        <el-col
          v-for="plant in results"
          :key="plant.id"
          :xs="24"
          :sm="12"
          :lg="8"
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
      <el-icon :size="32"><Opportunity /></el-icon>
      <p>Use natural language to search — our AI will find the best matches once connected to the backend.</p>
    </div>
  </div>
</template>

<script setup>
  import { ref, reactive } from "vue";
  import { useRouter } from "vue-router";
  import { Search, MagicStick, Opportunity } from "@element-plus/icons-vue";
  import { ElMessage } from "element-plus";
  import RecommendationCard from "./RecommendationCard.vue";
  import { searchPlants } from "@/api/ai";

  const router = useRouter();

  const query = ref("");
  const loading = ref(false);
  const searched = ref(false);
  const results = ref([]);
  const filters = reactive({
    category: "",
    careLevel: "",
    light: "",
  });

  async function handleSearch() {
    if (!query.value.trim() && !filters.category && !filters.careLevel && !filters.light) {
      ElMessage.warning("Enter a search term or select at least one filter.");
      return;
    }

    loading.value = true;
    searched.value = true;

    try {
      results.value = await searchPlants(query.value, { ...filters });
    } catch {
      ElMessage.error("Search failed. Please try again.");
      results.value = [];
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
  .smart-search {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  .search-bar {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }

  .search-bar .el-input {
    flex: 1;
    min-width: 240px;
  }

  .filters {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
  }

  .filters .el-select {
    width: 160px;
  }

  .results-count {
    color: #666;
    margin: 0 0 16px;
  }

  .results .el-col {
    margin-bottom: 20px;
  }

  .state-block {
    padding: 32px 16px;
    text-align: center;
    color: #666;
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

  @media (max-width: 640px) {
    .search-bar {
      flex-direction: column;
    }

    .search-bar .el-button {
      width: 100%;
    }

    .filters .el-select {
      width: 100%;
    }
  }
</style>
