<template>
  <div class="home-wrapper">

    <!-- HERO SECTION -->
    <section class="hero">

      <div class="hero-text">
        <h1>Grow Your Perfect Green Space 🌿</h1>
        <p>
          Discover beautiful indoor and outdoor plants for your home,
          garden, and lifestyle.
        </p>

        <div class="hero-buttons">
          <router-link to="/shop">
            <el-button type="success">Shop Now</el-button>
          </router-link>

          <router-link to="/products">
            <el-button>View Products</el-button>
          </router-link>
        </div>
      </div>

      <div class="hero-image">
        <img src="https://images.unsplash.com/photo-1446071103084-c257b5f70672" />
      </div>

    </section>

    <!-- FEATURES -->
    <section class="features">

      <div class="feature-card">
        🌱 Healthy Plants
      </div>

      <div class="feature-card">
        🚚 Fast Delivery
      </div>

      <div class="feature-card">
        💚 Eco Friendly
      </div>

    </section>

    <!-- POPULAR PRODUCTS -->
    <section class="popular">

      <h2>Popular Plants</h2>

      <div class="product-grid">

        <div class="product-card" v-for="(item, i) in products" :key="i">

          <img :src="item.image" />

          <h3>{{ item.name }}</h3>
          <p>{{ item.price }}$</p>

          <router-link to="/shop">
            <el-button type="success" size="small">
              Buy Now
            </el-button>
          </router-link>

        </div>

      </div>

    </section>

  </div>
</template>

<script setup>
import { ref } from "vue";

const products = ref([
  {
    name: "Monstera",
    price: 25,
    image: "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"
  },
  {
    name: "Snake Plant",
    price: 18,
   image: "https://images.unsplash.com/photo-1687552212914-03a30c82053c"
  },
  {
    name: "Aloe Vera",
    price: 12,
    image: "https://images.unsplash.com/photo-1570295835271-04c05b4ed943"
  },
  {
    name: "Monstera",
    price: 25,
    image: "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"
  },
  {
    name: "Snake Plant",
    price: 18,
   image: "https://images.unsplash.com/photo-1687552212914-03a30c82053c"
  },
  {
    name: "Aloe Vera",
    price: 12,
    image: "https://images.unsplash.com/photo-1570295835271-04c05b4ed943"
  },
  {
    name: "Monstera",
    price: 25,
    image: "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"
  },
  {
    name: "Snake Plant",
    price: 18,
   image: "https://images.unsplash.com/photo-1687552212914-03a30c82053c"
  },
  {
    name: "Aloe Vera",
    price: 12,
    image: "https://images.unsplash.com/photo-1570295835271-04c05b4ed943"
  },
  {
    name: "Monstera",
    price: 25,
    image: "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"
  },
  {
    name: "Snake Plant",
    price: 18,
   image: "https://images.unsplash.com/photo-1687552212914-03a30c82053c"
  },
  {
    name: "Aloe Vera",
    price: 12,
    image: "https://images.unsplash.com/photo-1570295835271-04c05b4ed943"
  }
]);
</script>

<style scoped>

/* WRAPPER */
.home-wrapper {
  background: #f4fff6;
  min-height: 100vh;
  padding: 40px 60px;
}

/* HERO */
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 50px;
}

.hero-text {
  max-width: 500px;
}

.hero-text h1 {
  font-size: 38px;
  color: #2E7D32;
}

.hero-text p {
  margin: 15px 0;
  color: #666;
  line-height: 1.5;
}

.hero-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.hero-image img {
  width: 500px;
  border-radius: 15px;
}

/* FEATURES */
.features {
  display: flex;
  justify-content: space-between;
  margin: 40px 0;
}

.feature-card {
  flex: 1;
  margin: 0 10px;
  padding: 20px;
  background: white;
  text-align: center;
  border-radius: 10px;
  font-weight: bold;
  box-shadow: 0 5px 15px rgba(0,0,0,0.05);
}

/* POPULAR */
.popular h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #2E7D32;
}

/* PRODUCT GRID */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  padding: 15px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
}

.product-card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 10px;
}

</style>