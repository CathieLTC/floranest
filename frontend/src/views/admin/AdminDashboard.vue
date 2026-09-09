<template>
  <div class="admin-dashboard">
    <div class="container">
      <!-- HERO HEADER matching the provided design -->
      <section class="hero">
        <div class="hero-icon" aria-hidden="true">
          <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg" class="server-icon">
            <rect x="8" y="10" width="48" height="14" rx="4" fill="currentColor"/>
            <rect x="8" y="29" width="48" height="14" rx="4" fill="currentColor"/>
            <rect x="8" y="48" width="48" height="6" rx="3" fill="currentColor"/>
            <circle cx="46" cy="17" r="2.5" fill="#C8E6C9"/>
            <circle cx="46" cy="36" r="2.5" fill="#C8E6C9"/>
            <circle cx="53" cy="17" r="2.5" fill="#C8E6C9"/>
            <circle cx="53" cy="36" r="2.5" fill="#C8E6C9"/>
          </svg>
        </div>
        <div class="hero-text">
          <h1>Admin Dashboard</h1>
          <p>Manage products, categories and order status from a dedicated UI</p>
        </div>
      </section>

      <!-- STATS OVERVIEW -->
      <section class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon products-icon">
            <el-icon><Goods /></el-icon>
          </div>
          <div class="stat-body">
            <span class="stat-value">{{ products.length }}</span>
            <span class="stat-label">Products</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon categories-icon">
            <el-icon><FolderOpened /></el-icon>
          </div>
          <div class="stat-body">
            <span class="stat-value">{{ categories.length }}</span>
            <span class="stat-label">Categories</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon orders-icon">
            <el-icon><Box /></el-icon>
          </div>
          <div class="stat-body">
            <span class="stat-value">{{ orders.length }}</span>
            <span class="stat-label">Orders</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon revenue-icon">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-body">
            <span class="stat-value">${{ formatPrice(totalRevenue) }}</span>
            <span class="stat-label">Revenue</span>
          </div>
        </div>
      </section>

      <!-- SECTION NAVIGATION -->
      <nav class="section-tabs" aria-label="Admin sections">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="tab-btn"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >
          <el-icon>
            <component :is="tab.icon" />
          </el-icon>
          {{ tab.label }}
        </button>
      </nav>

      <!-- PRODUCTS SECTION -->
      <section v-show="activeTab === 'products'" class="panel">
        <div class="panel-header">
          <div>
            <h2>Products</h2>
            <p class="panel-subtitle">Add, edit or remove store products</p>
          </div>
          <el-button type="success" :icon="Plus" @click="openProductDialog()">
            Add Product
          </el-button>
        </div>

        <div class="table-wrap">
          <el-table :data="products" style="width: 100%" v-loading="loading.products">
            <el-table-column prop="productId" label="ID" width="70" />
            <el-table-column label="Image" width="90">
              <template #default="{ row }">
                <img :src="row.imageUrl || placeholderImg" alt="" class="table-thumb" @error="onImgError" />
              </template>
            </el-table-column>
            <el-table-column prop="productName" label="Name" min-width="160" />
            <el-table-column prop="categoryName" label="Category" min-width="120" />
            <el-table-column prop="price" label="Price" width="100">
              <template #default="{ row }">${{ formatPrice(row.price) }}</template>
            </el-table-column>
            <el-table-column prop="stock" label="Stock" width="90" />
            <el-table-column label="Actions" width="160" fixed="right">
              <template #default="{ row }">
                <el-button size="small" :icon="Edit" @click="openProductDialog(row)">Edit</el-button>
                <el-button size="small" type="danger" :icon="Delete" @click="deleteProduct(row.productId)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>

      <!-- CATEGORIES SECTION -->
      <section v-show="activeTab === 'categories'" class="panel">
        <div class="panel-header">
          <div>
            <h2>Categories</h2>
            <p class="panel-subtitle">Organise products into categories</p>
          </div>
          <el-button type="success" :icon="Plus" @click="openCategoryDialog()">
            Add Category
          </el-button>
        </div>

        <div class="table-wrap">
          <el-table :data="categories" style="width: 100%" v-loading="loading.categories">
            <el-table-column prop="categoryId" label="ID" width="80" />
            <el-table-column prop="categoryName" label="Category Name" min-width="240" />
            <el-table-column label="Actions" width="160" fixed="right">
              <template #default="{ row }">
                <el-button size="small" :icon="Edit" @click="openCategoryDialog(row)">Edit</el-button>
                <el-button size="small" type="danger" :icon="Delete" @click="deleteCategory(row.categoryId)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>

      <!-- ORDERS SECTION -->
      <section v-show="activeTab === 'orders'" class="panel">
        <div class="panel-header">
          <div>
            <h2>Orders</h2>
            <p class="panel-subtitle">View and update order status</p>
          </div>
        </div>

        <div class="table-wrap">
          <el-table :data="orders" style="width: 100%" v-loading="loading.orders">
            <el-table-column prop="orderId" label="ID" width="70" />
            <el-table-column prop="orderNumber" label="Order #" min-width="130" />
            <el-table-column prop="orderDate" label="Date" min-width="120" />
            <el-table-column prop="userId" label="User ID" min-width="100" />
            <el-table-column prop="totalAmount" label="Total" width="100">
              <template #default="{ row }">${{ formatPrice(row.totalAmount) }}</template>
            </el-table-column>
            <el-table-column prop="orderStatus" label="Status" width="140">
              <template #default="{ row }">
                <span class="status-badge" :class="statusClass(row.orderStatus)">
                  {{ row.orderStatus }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="Actions" width="180" fixed="right">
              <template #default="{ row }">
                <el-select
                  v-model="row.orderStatus"
                  size="small"
                  class="status-select"
                  @change="updateOrderStatus(row.orderId, row.orderStatus)"
                >
                  <el-option
                    v-for="s in orderStatuses"
                    :key="s"
                    :label="s"
                    :value="s"
                  />
                </el-select>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>
    </div>

    <!-- PRODUCT FORM DIALOG -->
    <el-dialog
      v-model="dialogs.product"
      :title="productForm.productId ? 'Edit Product' : 'Add Product'"
      width="min(560px, 92vw)"
      destroy-on-close
    >
      <el-form :model="productForm" label-position="top" class="admin-form">
        <el-form-item label="Product Name">
          <el-input v-model="productForm.productName" placeholder="e.g. Monstera Deliciosa" />
        </el-form-item>
        <el-form-item label="Category">
          <el-select v-model="productForm.categoryId" placeholder="Select category" class="full-width">
            <el-option
              v-for="c in categories"
              :key="c.categoryId"
              :label="c.categoryName"
              :value="c.categoryId"
            />
          </el-select>
        </el-form-item>
        <div class="form-row">
          <el-form-item label="Price">
            <el-input-number v-model="productForm.price" :min="0" :precision="2" controls-position="right" class="full-width" />
          </el-form-item>
          <el-form-item label="Stock">
            <el-input-number v-model="productForm.stock" :min="0" :precision="0" controls-position="right" class="full-width" />
          </el-form-item>
        </div>
        <el-form-item label="Description">
          <el-input v-model="productForm.description" type="textarea" :rows="3" placeholder="Short product description" />
        </el-form-item>
        <el-form-item label="Image URL">
          <el-input v-model="productForm.imageUrl" placeholder="https://..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogs.product = false">Cancel</el-button>
        <el-button type="success" @click="saveProduct">Save</el-button>
      </template>
    </el-dialog>

    <!-- CATEGORY FORM DIALOG -->
    <el-dialog
      v-model="dialogs.category"
      :title="categoryForm.categoryId ? 'Edit Category' : 'Add Category'"
      width="min(420px, 92vw)"
      destroy-on-close
    >
      <el-form :model="categoryForm" label-position="top" class="admin-form">
        <el-form-item label="Category Name">
          <el-input v-model="categoryForm.categoryName" placeholder="e.g. Indoor Plants" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogs.category = false">Cancel</el-button>
        <el-button type="success" @click="saveCategory">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue';
import api from '@/api/axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  Goods,
  FolderOpened,
  Box,
  Money,
  Plus,
  Edit,
  Delete
} from '@element-plus/icons-vue';

const activeTab = ref('products');
const tabs = [
  { key: 'products', label: 'Products', icon: 'Goods' },
  { key: 'categories', label: 'Categories', icon: 'FolderOpened' },
  { key: 'orders', label: 'Orders', icon: 'Box' }
];

const products = ref([]);
const categories = ref([]);
const orders = ref([]);

const loading = reactive({
  products: false,
  categories: false,
  orders: false
});

const dialogs = reactive({
  product: false,
  category: false
});

const productForm = reactive({
  productId: null,
  productName: '',
  categoryId: null,
  price: 0,
  stock: 0,
  description: '',
  imageUrl: ''
});

const categoryForm = reactive({
  categoryId: null,
  categoryName: ''
});

const orderStatuses = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

const placeholderImg =
  "data:image/svg+xml;charset=UTF-8," +
  encodeURIComponent(
    `<svg xmlns='http://www.w3.org/2000/svg' width='100' height='100'>
       <rect width='100%' height='100%' fill='#e8f5e9'/>
       <text x='50%' y='50%' font-size='40' text-anchor='middle' dominant-baseline='middle'>🪴</text>
     </svg>`
  );

const totalRevenue = computed(() =>
  orders.value.reduce((sum, o) => sum + Number(o.totalAmount || 0), 0)
);

const formatPrice = (p) => Number(p || 0).toFixed(2);

const onImgError = (e) => {
  e.target.src = placeholderImg;
  e.target.onerror = null;
};

const statusClass = (status) => {
  switch (status) {
    case 'PENDING': return 'pending';
    case 'PROCESSING': return 'processing';
    case 'SHIPPED': return 'shipped';
    case 'DELIVERED': return 'delivered';
    case 'CANCELLED': return 'cancelled';
    default: return '';
  }
};

const loadProducts = async () => {
  loading.products = true;
  try {
    const { data } = await api.get('/products');
    products.value = data.map(p => ({
      ...p,
      categoryName: categories.value.find(c => c.categoryId === p.categoryId)?.categoryName || '-'
    }));
  } catch (err) {
    console.error(err);
    ElMessage.error('Failed to load products.');
  } finally {
    loading.products = false;
  }
};

const loadCategories = async () => {
  loading.categories = true;
  try {
    const { data } = await api.get('/categories');
    categories.value = data;
  } catch (err) {
    console.error(err);
    ElMessage.error('Failed to load categories.');
  } finally {
    loading.categories = false;
  }
};

const loadOrders = async () => {
  loading.orders = true;
  try {
    const { data } = await api.get('/orders');
    orders.value = data;
  } catch (err) {
    console.error(err);
    ElMessage.error('Failed to load orders.');
  } finally {
    loading.orders = false;
  }
};

const openProductDialog = (product = null) => {
  if (product) {
    Object.assign(productForm, {
      productId: product.productId,
      productName: product.productName,
      categoryId: product.categoryId,
      price: product.price,
      stock: product.stock,
      description: product.description || '',
      imageUrl: product.imageUrl || ''
    });
  } else {
    Object.assign(productForm, {
      productId: null,
      productName: '',
      categoryId: null,
      price: 0,
      stock: 0,
      description: '',
      imageUrl: ''
    });
  }
  dialogs.product = true;
};

const saveProduct = async () => {
  try {
    if (productForm.productId) {
      await api.put(`/products/${productForm.productId}`, { ...productForm });
      ElMessage.success('Product updated.');
    } else {
      await api.post('/products', { ...productForm });
      ElMessage.success('Product added.');
    }
    dialogs.product = false;
    await loadProducts();
  } catch (err) {
    console.error(err);
    ElMessage.error('Failed to save product.');
  }
};

const deleteProduct = async (id) => {
  try {
    await ElMessageBox.confirm('Delete this product?', 'Confirm', { type: 'warning' });
    await api.delete(`/products/${id}`);
    ElMessage.success('Product deleted.');
    await loadProducts();
  } catch (err) {
    if (err !== 'cancel') {
      console.error(err);
      ElMessage.error('Failed to delete product.');
    }
  }
};

const openCategoryDialog = (category = null) => {
  if (category) {
    Object.assign(categoryForm, {
      categoryId: category.categoryId,
      categoryName: category.categoryName
    });
  } else {
    Object.assign(categoryForm, {
      categoryId: null,
      categoryName: ''
    });
  }
  dialogs.category = true;
};

const saveCategory = async () => {
  try {
    if (categoryForm.categoryId) {
      await api.put(`/categories/${categoryForm.categoryId}`, { ...categoryForm });
      ElMessage.success('Category updated.');
    } else {
      await api.post('/categories', { ...categoryForm });
      ElMessage.success('Category added.');
    }
    dialogs.category = false;
    await loadCategories();
    await loadProducts();
  } catch (err) {
    console.error(err);
    ElMessage.error('Failed to save category.');
  }
};

const deleteCategory = async (id) => {
  try {
    await ElMessageBox.confirm('Delete this category?', 'Confirm', { type: 'warning' });
    await api.delete(`/categories/${id}`);
    ElMessage.success('Category deleted.');
    await loadCategories();
    await loadProducts();
  } catch (err) {
    if (err !== 'cancel') {
      console.error(err);
      ElMessage.error('Failed to delete category.');
    }
  }
};

const updateOrderStatus = async (orderId, status) => {
  try {
    await api.put(`/orders/status/${orderId}`, { status });
    ElMessage.success('Order status updated.');
  } catch (err) {
    console.error(err);
    ElMessage.error('Failed to update order status.');
    await loadOrders();
  }
};

onMounted(async () => {
  await loadCategories();
  await Promise.all([loadProducts(), loadOrders()]);
});
</script>

<style scoped>
.admin-dashboard {
  padding: 44px clamp(18px, 5vw, 64px);
  min-height: 100vh;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
}

/* ---------- HERO HEADER ---------- */
.hero {
  display: flex;
  align-items: center;
  gap: 28px;
  padding: 36px 40px;
  background: #f6fbf4;
  border-radius: 20px;
  border: 1px solid rgba(46, 125, 50, 0.1);
  margin-bottom: 30px;
  box-shadow: 0 6px 22px rgba(46, 125, 50, 0.06);
}

.hero-icon {
  flex-shrink: 0;
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: #d8eeda;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1b4d1e;
}

.server-icon {
  width: 52px;
  height: 52px;
}

.hero-text h1 {
  margin: 0 0 8px;
  font-size: clamp(26px, 4vw, 34px);
  color: #1b3d1e;
  font-weight: 700;
  letter-spacing: -0.3px;
}

.hero-text p {
  margin: 0;
  font-size: clamp(15px, 2vw, 17px);
  color: #5a6d5c;
  line-height: 1.5;
  max-width: 520px;
}

/* ---------- STATS GRID ---------- */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 18px;
  margin-bottom: 30px;
}

.stat-card {
  background: #fff;
  border-radius: 16px;
  padding: 22px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(46, 125, 50, 0.08);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 24px rgba(46, 125, 50, 0.1);
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon .el-icon {
  font-size: 24px;
}

.products-icon { background: #e8f5e9; color: #2E7D32; }
.categories-icon { background: #e3f2fd; color: #1976d2; }
.orders-icon { background: #fff3e0; color: #f57c00; }
.revenue-icon { background: #f3e5f5; color: #7b1fa2; }

.stat-body {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
}

/* ---------- SECTION TABS ---------- */
.section-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 22px;
}

.tab-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 22px;
  border: none;
  border-radius: 12px;
  background: #fff;
  color: #4b5563;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.2s ease;
}

.tab-btn:hover {
  background: #f1f8ec;
  color: #2E7D32;
}

.tab-btn.active {
  background: #2E7D32;
  color: #fff;
}

/* ---------- PANELS ---------- */
.panel {
  background: #fff;
  border-radius: 18px;
  padding: 28px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(46, 125, 50, 0.08);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.panel-header h2 {
  margin: 0 0 4px;
  font-size: 22px;
  color: #1b3d1e;
}

.panel-subtitle {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}

.table-wrap {
  overflow-x: auto;
}

.table-thumb {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 8px;
  background: #f1f8ec;
}

.status-badge {
  display: inline-block;
  padding: 5px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  color: #fff;
  text-transform: capitalize;
}

.status-badge.pending { background: #9e9e9e; }
.status-badge.processing { background: #fbc02d; color: #333; }
.status-badge.shipped { background: #1976d2; }
.status-badge.delivered { background: #2E7D32; }
.status-badge.cancelled { background: #e53935; }

.status-select {
  width: 140px;
}

/* ---------- FORMS ---------- */
.admin-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #374151;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

.full-width {
  width: 100%;
}

/* ---------- RESPONSIVE ---------- */
@media (max-width: 768px) {
  .hero {
    flex-direction: column;
    text-align: center;
    padding: 28px 24px;
  }

  .hero-icon {
    width: 80px;
    height: 80px;
  }

  .server-icon {
    width: 42px;
    height: 42px;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-card {
    padding: 16px;
  }

  .panel {
    padding: 18px;
  }

  .panel-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
