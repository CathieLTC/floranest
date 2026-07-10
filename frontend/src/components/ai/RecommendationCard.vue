<template>
  <div class="rec-card">

    <div class="card-image">
      <img :src="plant.imageUrl" :alt="plant.name" />
      <el-tag v-if="plant.matchScore" class="match-badge" type="success" effect="dark">
        {{ plant.matchScore }}% match
      </el-tag>
    </div>

    <div class="card-body">
      <div class="card-header">
        <h3>{{ plant.name }}</h3>
        <el-tag size="small" :type="careColor">{{ plant.careLevel }}</el-tag>
      </div>

      <div class="meta">
        <span>☀️ {{ plant.light }}</span>
        <span>🪴 {{ plant.category }}</span>
      </div>

      <p class="description">{{ plant.description }}</p>

      <!-- AI reason shown for recommendations -->
      <p v-if="plant.reason" class="reason">
        ✨ {{ plant.reason }}
      </p>

      <div class="actions">
        <el-button size="small" type="success" plain @click="$emit('view-details', plant)">
          View Details
        </el-button>
        <el-button size="small" type="success" @click="$emit('add-to-shop', plant)">
          Find in Shop
        </el-button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  plant: { type: Object, required: true }
  // Required fields: name, description, imageUrl, careLevel, light, category
  // Optional:        matchScore, reason
});

defineEmits(["view-details", "add-to-shop"]);

const careColor = computed(() => {
  if (props.plant.careLevel === "Easy")     return "success";
  if (props.plant.careLevel === "Moderate") return "warning";
  return "info";
});
</script>

<style scoped>
.rec-card {
  background: white;
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #e8f5e9;
  box-shadow: 0 4px 12px rgba(0,0,0,0.07);
  transition: transform 0.25s, box-shadow 0.25s;
  height: 100%;
}

.rec-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 24px rgba(46,125,50,0.12);
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
  transition: transform 0.4s;
}

.rec-card:hover .card-image img { transform: scale(1.05); }

.match-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  font-weight: 700;
}

.card-body { padding: 14px; }

.card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 8px;
}

.card-header h3 {
  margin: 0;
  font-size: 15px;
  color: #1B5E20;
  line-height: 1.3;
}

.meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.description {
  font-size: 13px;
  color: #555;
  line-height: 1.5;
  margin: 0 0 10px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.reason {
  font-size: 12px;
  color: #2E7D32;
  background: #f1f8e9;
  padding: 8px 10px;
  border-radius: 8px;
  margin: 0 0 12px;
  line-height: 1.4;
}

.actions {
  display: flex;
  gap: 8px;
}
</style>