<template>
  <div class="category-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Category Management</span>
          <el-button type="primary" @click="openCategoryDialog('add')">Add Category</el-button>
        </div>
      </template>

      <el-table :data="categories" style="width: 100%">
        <el-table-column prop="categoryId" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column label="Actions" width="180">
          <template #default="scope">
            <el-button size="small" @click="openCategoryDialog('edit', scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteCategory(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? 'Add Category' : 'Edit Category'"
      width="500px"
    >
      <el-form :model="currentCategory" label-width="120px">
        <el-form-item label="Name">
          <el-input v-model="currentCategory.name"></el-input>
        </el-form-item>
        <!-- Add other fields as necessary, e.g., image -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveCategory">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from '@/api/axios';

const categories = ref([]);
const dialogVisible = ref(false);
const dialogType = ref('add'); // 'add' or 'edit'
const currentCategory = ref({});

onMounted(() => {
  fetchCategories();
});

const fetchCategories = async () => {
  try {
    const response = await axios.get('/categories');
    categories.value = response.data;
  } catch (error) {
    ElMessage.error('Failed to fetch categories.');
    console.error('Error fetching categories:', error);
  }
};

const openCategoryDialog = (type, row) => {
  dialogType.value = type;
  currentCategory.value = type === 'add' ? { name: '' } : { ...row };
  dialogVisible.value = true;
};

const saveCategory = async () => {
  try {
    if (dialogType.value === 'add') {
      await axios.post('/categories', currentCategory.value);
      ElMessage.success('Category added successfully!');
    } else {
      await axios.put(`/categories/${currentCategory.value.categoryId}`, currentCategory.value);
      ElMessage.success('Category updated successfully!');
    }
    dialogVisible.value = false;
    fetchCategories();
  } catch (error) {
    ElMessage.error('Failed to save category.');
    console.error('Error saving category:', error);
  }
};

const deleteCategory = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete ${row.name}?`,
    'Warning',
    {
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(`/categories/${row.categoryId}`);
      ElMessage.success('Category deleted successfully!');
      fetchCategories();
    } catch (error) {
      ElMessage.error('Failed to delete category.');
      console.error('Error deleting category:', error);
    }
  }).catch(() => {
    ElMessage.info('Delete cancelled');
  });
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
