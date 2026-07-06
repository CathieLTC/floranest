<template>
  <div class="detail-wrapper" v-if="product">

    <!-- PRODUCT CARD -->
    <div class="detail-card">

      <!-- IMAGE -->
      <div class="image-section">
        <img :src="product.imageUrl">      
      </div>

      <!-- INFO -->
      <div class="info-section">

        <h1>{{ product.productName }}</h1>
        <p class="desc">
          {{ product.description }}
        </p>
        <p class="price">{{ product.price }}$</p>
        <p class="category">
          Category: <b>{{ product.categoryName }}</b>
        </p>
        <p>Difficulty: {{ product.difficulty }}</p>
        <p>Sunlight: {{ product.sunlight }}</p>
        <p>Watering: {{ product.watering }}</p>
        <p>Temperature: {{ product.temperature }}</p>
        
        <el-button type="success" @click="cartStore.addToCart(product)">Add to Cart</el-button>

        <router-link to="/shop">
          <el-button class="btn secondary">
            Back to Shop
          </el-button>
        </router-link>

      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, onMounted } from "vue";
  import { useRoute } from "vue-router";
  import api from "@/api/axios";
  import { ElMessage } from "element-plus";
  import { useCartStore } from "@/stores/cart";

  const cartStore = useCartStore();
  const route = useRoute();
  const product = ref({});

  const loadProduct = async () => {

    try {

      const response = await api.get(
        `/products/${route.params.id}`
      );

      product.value = response.data;

    } catch (error) {
      console.error(error);
    }

  };

  onMounted(loadProduct);

</script>

<style scoped>

/* WRAPPER */
.detail-wrapper {
  padding: 50px;
  background: #f4fff6;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* CARD */
.detail-card {
  display: flex;
  gap: 40px;
  background: white;
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  max-width: 900px;
}

/* IMAGE */
.image-section img {
  width: 350px;
  height: 350px;
  object-fit: cover;
  border-radius: 12px;
}

/* INFO */
.info-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.info-section h1 {
  color: #2E7D32;
  margin-bottom: 10px;
}

.price {
  font-size: 22px;
  font-weight: bold;
  color: #2E7D32;
}

.desc {
  margin: 15px 0;
  color: #666;
  line-height: 1.5;
}

.category {
  margin-bottom: 20px;
  color: #444;
}

/* BUTTONS */
.btn {
  margin-bottom: 10px;
  width: 200px;
}

.secondary {
  background: #eee;
}

</style>