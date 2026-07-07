<template>
  <el-input
    v-model="query"
    :placeholder="placeholder"
    clearable
    @input="$emit('update:modelValue', query)"
    @clear="$emit('update:modelValue', '')"
  >
    <template #prefix>
      <el-icon><Search /></el-icon>
    </template>
  </el-input>
</template>

<script setup>
import { ref, watch } from 'vue';
import { Search } from '@element-plus/icons-vue';

const props = defineProps({
  modelValue:  { type: String, default: '' },
  placeholder: { type: String, default: 'Search...' }
});
defineEmits(['update:modelValue']);

const query = ref(props.modelValue);

// Keep local value in sync if parent changes it
watch(() => props.modelValue, val => { query.value = val; });
</script>