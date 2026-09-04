<template>
  <div class="detail-wrapper" v-if="product">

    <div class="detail-layout">

      <!-- PRODUCT CARD (LEFT) -->
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

      <!-- TUTORIAL VIDEO (RIGHT) -->
      <div class="video-section" v-if="product.videoUrl">
        <h2>How to Care for Your {{ product.productName }}</h2>
        <p class="video-subtitle">
          A quick beginner's guide to keeping this plant happy and healthy.
        </p>
        <div class="video-wrapper">
          <iframe
            v-if="embedUrl"
            :src="embedUrl"
            class="tutorial-video"
            frameborder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen
          ></iframe>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from "vue";
  import { useRoute } from "vue-router";
  import api from "@/api/axios";
  import { useCartStore } from "@/stores/cart";

  const cartStore = useCartStore();
  const route = useRoute();
  const product = ref({});

  // Extract a YouTube embed URL from various YouTube link formats
  const embedUrl = computed(() => {
    const url = product.value.videoUrl;
    if (!url) return "";

    // youtu.be/VIDEO_ID
    const shortMatch = url.match(/youtu\.be\/([a-zA-Z0-9_-]{11})/);
    if (shortMatch) return `https://www.youtube.com/embed/${shortMatch[1]}`;

    // youtube.com/watch?v=VIDEO_ID
    const watchMatch = url.match(/[?&]v=([a-zA-Z0-9_-]{11})/);
    if (watchMatch) return `https://www.youtube.com/embed/${watchMatch[1]}`;

    // youtube.com/embed/VIDEO_ID (already embed format)
    const embedMatch = url.match(/youtube\.com\/embed\/([a-zA-Z0-9_-]{11})/);
    if (embedMatch) return `https://www.youtube.com/embed/${embedMatch[1]}`;

    // Not a YouTube URL — return as-is (could be a direct mp4)
    return url;
  });

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
  align-items: flex-start;
}

/* LAYOUT — card on left, video on right */
.detail-layout {
  display: flex;
  gap: 30px;
  align-items: flex-start;
  max-width: 1200px;
  width: 100%;
}

/* CARD */
.detail-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background: white;
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  flex: 1;
  min-width: 0;
}

/* IMAGE */
.image-section img {
  width: 100%;
  height: auto;
  max-height: 400px;
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

/* TUTORIAL VIDEO */
.video-section {
  background: white;
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  flex: 1;
  min-width: 0;
  position: sticky;
  top: 30px;
}

.video-section h2 {
  color: #2E7D32;
  font-size: 22px;
  margin-bottom: 6px;
}

.video-subtitle {
  color: #888;
  font-size: 14px;
  margin-bottom: 20px;
}

.video-wrapper {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
  aspect-ratio: 16 / 9;
}

.tutorial-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 0;
}

/* RESPONSIVE — stack on smaller screens */
@media (max-width: 900px) {
  .detail-layout {
    flex-direction: column;
  }

  .video-section {
    position: static;
  }
}

</style>
