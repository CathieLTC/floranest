<template>
  <div class="order-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Order Management</span>
        </div>
      </template>

      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="orderId" label="Order ID" width="100"></el-table-column>
        <el-table-column prop="userFullName" label="Customer Name"></el-table-column>
        <el-table-column prop="totalAmount" label="Total Amount"></el-table-column>
        <el-table-column prop="createdAt" label="Order Date"></el-table-column>
        <el-table-column label="Status" width="180">
          <template #default="scope">
            <el-select v-model="scope.row.status" placeholder="Select" @change="updateOrderStatus(scope.row)">
              <el-option label="Pending" value="PENDING"></el-option>
              <el-option label="Processing" value="PROCESSING"></el-option>
              <el-option label="Shipped" value="SHIPPED"></el-option>
              <el-option label="Delivered" value="DELIVERED"></el-option>
              <el-option label="Cancelled" value="CANCELLED"></el-option>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from '@/api/axios';

const orders = ref([]);

onMounted(() => {
  fetchOrders();
});

const fetchOrders = async () => {
  try {
    const response = await axios.get('/orders');
    orders.value = response.data;
  } catch (error) {
    ElMessage.error('Failed to fetch orders.');
    console.error('Error fetching orders:', error);
  }
};

const updateOrderStatus = async (order) => {
  try {
    await axios.put(`/orders/status/${order.orderId}`, { status: order.status });
    ElMessage.success(`Order ${order.orderId} status updated to ${order.status}`);
  } catch (error) {
    ElMessage.error(`Failed to update order ${order.orderId} status.`);
    console.error('Error updating order status:', error);
  }
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
