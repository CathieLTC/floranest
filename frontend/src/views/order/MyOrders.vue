<template>
  <div class="orders-page">

    <h1>📦 My Orders</h1>
    <p class="subtitle">Track all your orders in one place</p>

    <div class="table-wrapper">

      <table class="orders-table">

        <thead>
          <tr>
            <th>Order ID</th>
            <th>Date</th>
            <th>Status</th>
            <th>Total</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="orders.length === 0">
            <td colspan="5" style="text-align:center;padding:40px;">
                You haven't placed any orders yet 🌱
            </td>
          </tr>

          <!-- MAIN ROW -->
          <template v-for="order in orders" :key="order.orderId">

            <tr>

              <td>#{{ order.orderNumber }}</td>
              <td>{{ order.orderDate }}</td>

              <td>
                <span
                  class="status"
                  :class="statusClass(order.orderStatus)"
                >
                  {{ order.orderStatus }}
                </span>
              </td>

              <td>$${{ order.totalAmount }}</td>

              <td>
                <el-button
                  size="small"
                  type="success"
                  @click="toggleDetails(order.orderId)"
                >
                  {{ expanded === order.orderId ? "Hide" : "View" }} Details
                </el-button>
                <el-button v-if="order.orderStatus === 'PENDING'" type="danger" size="small" 
                @click="cancelOrder(order.orderId)">Cancel</el-button>
              </td>

            </tr>

            <!-- EXPANDED ROW -->
            <tr v-if="expanded === order.orderId" class="details-row">
              <td colspan="5">

                <div class="details-box">

                  <h3>Order Information</h3>

                  <p><strong>Order Number:</strong> {{ order.orderNumber }}</p>
                  <p><strong>Shipping Address:</strong> {{ order.address }}</p>
                  <p><strong>Status:</strong> {{ order.orderStatus }}</p>

                  <hr>

                  <h4>Purchased Items</h4>

                  <div
                      v-for="item in order.items"
                      :key="item.orderItemId"
                      class="item"
                  >

                    <img
                        :src="item.imageUrl"
                        class="thumb"
                    >

                    <div class="info">

                      <strong>{{ item.productName }}</strong>

                      <div>
                        Quantity: {{ item.quantity }}
                      </div>

                      <div>
                        ${{ item.price }}
                      </div>

                    </div>

                  </div>

                </div>

              </td>
            </tr>
          </template>

        </tbody>

      </table>

    </div>

  </div>
</template>

<script setup>
  import { ref, onMounted } from "vue";
  import api from "@/api/axios";
  import { ElMessage, ElMessageBox } from "element-plus";

  const orders = ref([]);
  const expanded = ref(null);

  // Logged in user
  const user = JSON.parse(localStorage.getItem("user"));

  const loadOrders = async () => {
    try {
      const response = await api.get(`/orders/user/${user.userId}`);
      orders.value = response.data;
    } catch (error) {
      console.error(error);
    }
  };

  onMounted(loadOrders);

  const toggleDetails = (orderId) => {
    expanded.value =
      expanded.value === orderId ? null : orderId;
  };

  const statusClass = (status) => {

    switch (status) {

      case "PENDING":
        return "pending";

      case "PROCESSING":
        return "processing";

      case "SHIPPED":
        return "shipped";

      case "DELIVERED":
        return "delivered";

      case "CANCELLED":
        return "cancelled";

      default:
        return "";

    }

  };
  const cancelOrder = async (orderId) => {

      try{

         await ElMessageBox.confirm(
            "Are you sure you want to cancel this order?",
            "Confirm Cancellation",
            {
              confirmButtonText: "Yes, Cancel",
              cancelButtonText: "No, Keep",
              type: "warning",
            }
          );

          await api.put(`/orders/cancel/${orderId}`);
          loadOrders();

      }catch(error){

          console.error(error);

      }
      ElMessage.success("Order cancelled");

  };
</script>

<style scoped>

.orders-page{
  padding:50px;
  background:#f5fff7;
  min-height:100vh;
}

h1{
  color:#2E7D32;
}

.subtitle{
  color:#666;
  margin-bottom:25px;
}

.table-wrapper{
  background:white;
  padding:20px;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,0.08);
}

.orders-table{
  width:100%;
  border-collapse:collapse;
}

.orders-table th,
.orders-table td{
  padding:15px;
  border-bottom:1px solid #eee;
  text-align:left;
}

.orders-table th{
  background:#f8f8f8;
}

/* STATUS */
.status{
  padding:5px 12px;
  border-radius:20px;
  font-size:12px;
  font-weight:bold;
  color:white;
}

.pending{
    background:#9e9e9e;
}

.delivered{
  background:#2E7D32;
}

.processing{
  background:#fbc02d;
  color:black;
}

.shipped{
  background:#1976d2;
}

.cancelled{
    background:#e53935;
}

/* DETAILS ROW */
.details-row td{
  background:#f9fff9;
}

.details-box{
  padding:15px;
  border-left:4px solid #2E7D32;
}

.details-box h4{
  margin-bottom:10px;
  color:#2E7D32;
}

.item{
  padding:5px 0;
  color:#555;
}

.thumb{
    width:70px;
    height:70px;
    object-fit:cover;
    border-radius:8px;
}

.item{
    display:flex;
    gap:15px;
    align-items:center;
    margin:12px 0;
}

.info{
    display:flex;
    flex-direction:column;
    gap:4px;
}

</style>