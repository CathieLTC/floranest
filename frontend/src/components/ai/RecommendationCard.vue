<template>
  <el-card class="recommendation-card" shadow="hover">
    <div class="card-image">
      <img :src="plant.imageUrl" :alt="plant.name" loading="lazy" />
      <el-tag v-if="plant.matchScore" class="match-badge" type="success" effect="dark">
        {{ plant.matchScore }}% match
      </el-tag>
    </div>

    <div class="card-body">
      <div class="card-header">
        <h3>{{ plant.name }}</h3>
        <el-tag size="small" :type="careTagType">{{ plant.careLevel }}</el-tag>
      </div>

      <p class="description">{{ plant.description }}</p>

      <div class="meta">
        <span><el-icon><Sunny /></el-icon> {{ plant.light }}</span>
        <span><el-icon><House /></el-icon> {{ plant.category }}</span>
      </div>

      <p v-if="plant.reason" class="reason">
        <el-icon><MagicStick /></el-icon>
        {{ plant.reason }}
      </p>

      <div class="actions">
        <el-button type="success" plain size="small" @click="$emit('view-details', plant)">
          View Details
        </el-button>
        <el-button type="success" size="small" @click="$emit('add-to-shop', plant)">
          Find in Shop
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
  import { computed } from "vue";
  import { Sunny, House, MagicStick } from "@element-plus/icons-vue";

  const props = defineProps({
    plant: {
      type: Object,
      required: true,
    },
  });

  defineEmits(["view-details", "add-to-shop"]);

  const careTagType = computed(() => {
    if (props.plant.careLevel === "Easy") return "success";
    if (props.plant.careLevel === "Moderate") return "warning";
    return "info";
  });
</script>

<style scoped>
  .recommendation-card {
    border-radius: 14px;
    overflow: hidden;
    height: 100%;
    border: 1px solid #e8f5e9;
    transition: transform 0.25s ease, box-shadow 0.25s ease;
  }

  .recommendation-card:hover {
    transform: translateY(-4px);
  }

  .card-image {
    position: relative;
    height: 180px;
    overflow: hidden;
  }

  .card-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .match-badge {
    position: absolute;
    top: 12px;
    right: 12px;
    font-weight: 600;
  }

  .card-body {
    padding: 4px 2px 0;
  }

  .card-header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 8px;
    margin-bottom: 8px;
  }

  .card-header h3 {
    margin: 0;
    font-size: 1.05rem;
    color: #1b5e20;
    line-height: 1.3;
  }

  .description {
    color: #555;
    font-size: 0.9rem;
    line-height: 1.5;
    margin: 0 0 12px;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .meta {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    font-size: 0.82rem;
    color: #666;
    margin-bottom: 10px;
  }

  .meta span {
    display: inline-flex;
    align-items: center;
    gap: 4px;
  }

  .reason {
    display: flex;
    align-items: flex-start;
    gap: 6px;
    font-size: 0.82rem;
    color: #2e7d32;
    background: #f1f8e9;
    padding: 8px 10px;
    border-radius: 8px;
    margin: 0 0 14px;
    line-height: 1.4;
  }

  .actions {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
  }
</style>
