<template>
  <div class="page-header">
    <span v-if="eyebrow" class="page-eyebrow">{{ eyebrow }}</span>
    <h1>{{ title }}</h1>
    <p v-if="subtitle">{{ subtitle }}</p>
  </div>
</template>

<script setup>
defineProps({
  title:    { type: String, required: true },
  subtitle: { type: String, default: '' },
  eyebrow:  { type: String, default: '' }
});
</script>

<style scoped>
.page-header {
  text-align: center;
  margin-bottom: 36px;
}
.page-eyebrow {
  display: inline-block;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--fn-green-600);
  margin-bottom: 8px;
}
.page-header h1 {
  font-size: clamp(1.8rem, 4vw, 2.5rem);
  color: var(--fn-ink);
  margin-bottom: 8px;
  letter-spacing: -0.03em;
}
.page-header p {
  color: var(--fn-text-3);
  font-size: 0.95rem;
}
</style>
