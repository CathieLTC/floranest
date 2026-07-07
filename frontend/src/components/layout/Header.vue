<template>
  <div class="page-header">
    <h1>{{ title }}</h1>
    <p v-if="subtitle">{{ subtitle }}</p>
  </div>
</template>

<script setup>
defineProps({
  title:    { type: String, required: true },
  subtitle: { type: String, default: '' }
});
</script>

<style scoped>
.page-header {
  text-align: center;
  margin-bottom: 30px;
}
.page-header h1 {
  font-size: 32px;
  color: #2E7D32;
  margin-bottom: 8px;
}
.page-header p {
  color: #666;
  font-size: 15px;
}
</style>