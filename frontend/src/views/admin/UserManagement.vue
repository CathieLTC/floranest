<template>
  <div class="user-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>User Management</span>
        </div>
      </template>

      <el-table :data="users" style="width: 100%">
        <el-table-column prop="userId" label="ID" width="80"></el-table-column>
        <el-table-column prop="fullName" label="Full Name"></el-table-column>
        <el-table-column prop="email" label="Email"></el-table-column>
        <el-table-column prop="role" label="Role" width="150">
          <template #default="scope">
            <el-select v-model="scope.row.role" placeholder="Select Role" @change="updateUserRole(scope.row)">
              <el-option label="User" value="USER"></el-option>
              <el-option label="Admin" value="ADMIN"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="120">
          <template #default="scope">
            <el-button size="small" type="danger" @click="deleteUser(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from '@/api/axios';

const users = ref([]);

onMounted(() => {
  fetchUsers();
});

const fetchUsers = async () => {
  try {
    const response = await axios.get('/users'); // Assuming /users endpoint for all users
    users.value = response.data;
  } catch (error) {
    ElMessage.error('Failed to fetch users.');
    console.error('Error fetching users:', error);
  }
};

const updateUserRole = async (user) => {
  try {
    // Assuming a PUT /users/{id}/role endpoint or similar
    await axios.put(`/users/${user.userId}`, { ...user, role: user.role }); // Sending entire user object for update
    ElMessage.success(`User ${user.fullName} role updated to ${user.role}`);
  } catch (error) {
    ElMessage.error(`Failed to update user ${user.fullName} role.`);
    console.error('Error updating user role:', error);
  }
};

const deleteUser = (user) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete user ${user.fullName}?`,
    'Warning',
    {
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(`/users/${user.userId}`);
      ElMessage.success('User deleted successfully!');
      fetchUsers();
    } catch (error) {
      ElMessage.error('Failed to delete user.');
      console.error('Error deleting user:', error);
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
