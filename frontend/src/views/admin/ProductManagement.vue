<template>
  <div class="product-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Product Management</span>
          <el-button type="primary" @click="openProductDialog('add')">Add Product</el-button>
        </div>
      </template>

      <el-table :data="products" style="width: 100%">
        <el-table-column prop="productId" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column prop="price" label="Price"></el-table-column>
        <el-table-column prop="stock" label="Stock"></el-table-column>
        <el-table-column label="Actions" width="180">
          <template #default="scope">
            <el-button size="small" @click="openProductDialog('edit', scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteProduct(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? 'Add Product' : 'Edit Product'"
      width="500px"
    >
      <el-form :model="currentProduct" label-width="120px">
        <el-form-item label="Name">
          <el-input v-model="currentProduct.name"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" v-model="currentProduct.description"></el-input>
        </el-form-item>
        <el-form-item label="Price">
          <el-input-number v-model="currentProduct.price" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="Stock">
          <el-input-number v-model="currentProduct.stock" :min="0"></el-input-number>
        </el-form-item>
        <!-- Add other fields as necessary, e.g., category, image, etc. -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveProduct">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from '@/api/axios';

const products = ref([]);
const dialogVisible = ref(false);
const dialogType = ref('add'); // 'add' or 'edit'
const currentProduct = ref({});

onMounted(() => {
  fetchProducts();
});

const fetchProducts = async () => {
  try {
    const response = await axios.get('/products');
    products.value = response.data;
  } catch (error) {
    ElMessage.error('Failed to fetch products.');
    console.error('Error fetching products:', error);
  }
};

const openProductDialog = (type, row) => {
  dialogType.value = type;
  currentProduct.value = type === 'add' ? { name: '', description: '', price: 0, stock: 0, categoryId: null, imageUrl: '' } : { ...row };
  dialogVisible.value = true;
};

const saveProduct = async () => {
  try {
    if (dialogType.value === 'add') {
      await axios.post('/products', currentProduct.value);
      ElMessage.success('Product added successfully!');
    } else {
      await axios.put(`/products/${currentProduct.value.productId}`, currentProduct.value);
      ElMessage.success('Product updated successfully!');
    }
    dialogVisible.value = false;
    fetchProducts();
  } catch (error) {
    ElMessage.error('Failed to save product.');
    console.error('Error saving product:', error);
  }
};

const deleteProduct = (row) => {
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
      await axios.delete(`/products/${row.productId}`);
      ElMessage.success('Product deleted successfully!');
      fetchProducts();
    } catch (error) {
      ElMessage.error('Failed to delete product.');
      console.error('Error deleting product:', error);
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
