<template>
  <div class="detail-wrapper" v-if="product">

    <!-- PRODUCT CARD -->
    <div class="detail-card">

      <!-- IMAGE -->
      <div class="image-section">
        <img :src="product.image" />
      </div>

      <!-- INFO -->
      <div class="info-section">

        <h1>{{ product.name }}</h1>

        <p class="price">{{ product.price }}$</p>

        <p class="desc">
          {{ product.description }}
        </p>

        <p class="category">
          Category: <b>{{ product.category }}</b>
        </p>

        <el-button type="success" class="btn">
          Add to Cart
        </el-button>

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
import { ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

/* SAMPLE PRODUCTS (same as shop for now) */
const products = [
  {
    id: 1,
    name: "Monstera Deliciosa",
    price: 25,
    category: "indoor",
    image: "https://images.unsplash.com/photo-1501004318641-b39e6451bec6",
    description: "A beautiful tropical plant perfect for indoor decoration."
  },
  {
    id: 2,
    name: "Snake Plant",
    price: 18,
    category: "indoor",
    image: "https://images.unsplash.com/photo-1593691509543-c55fb32e5e22",
    description: "Very easy to care for and improves air quality."
  },
  {
    id: 3,
    name: "Aloe Vera",
    price: 12,
    category: "succulent",
    image: "https://images.unsplash.com/photo-1596541223130-5d31a73fb6c6",
    description: "Medicinal plant used for skin care and healing."
  }
];

/* GET PRODUCT BY ID */
const product = ref(
  products.find(p => p.id === Number(route.params.id))
);
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