<template>
  <div class="reviews-wrapper">

    <!-- HEADER -->
    <div class="reviews-header">
      <span class="header-eyebrow">Community</span>
      <h1>Plant & Shop Reviews</h1>
      <p>Read what other plant lovers are saying, or share your own experience!</p>
    </div>

    <div class="reviews-content">

      <!-- LEFT: Submit a review -->
      <div class="submit-section">
        <h2>Write a Review</h2>

        <el-form :model="form" label-position="top">
          <el-form-item label="Review Type">
            <el-radio-group v-model="form.type">
              <el-radio-button label="plant">Plant</el-radio-button>
              <el-radio-button label="shop">Shop</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item :label="form.type === 'plant' ? 'Plant Name' : 'Shop Name'">
            <el-input v-model="form.targetName" :placeholder="form.type === 'plant' ? 'e.g. Monstera Deliciosa' : 'e.g. Indoor Oasis'" />
          </el-form-item>

          <el-form-item label="Your Name">
            <el-input v-model="form.authorName" placeholder="Your name" />
          </el-form-item>

          <el-form-item label="Rating">
            <el-rate v-model="form.rating" :max="5" show-text :texts="['Poor','Fair','Good','Very Good','Excellent']" />
          </el-form-item>

          <el-form-item label="Your Review">
            <el-input v-model="form.comment" type="textarea" :rows="4" placeholder="Share your experience..." />
          </el-form-item>

          <el-button type="success" size="large" @click="submitReview" :disabled="!canSubmit">
            Submit Review
          </el-button>
        </el-form>
      </div>

      <!-- RIGHT: Recent reviews -->
      <div class="feed-section">
        <h2>Recent Reviews</h2>

        <div v-if="reviews.length === 0" class="empty">
          <div class="empty-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2L13.5 7.5L19 9L13.5 10.5L12 16L10.5 10.5L5 9L10.5 7.5L12 2Z"/></svg>
          </div>
          <p>No reviews yet. Be the first to share!</p>
        </div>

        <div v-else class="review-list">
          <div class="review-card" v-for="(review, i) in sortedReviews" :key="i">
            <div class="review-top">
              <div class="review-author">
                <div class="avatar">{{ review.authorName.charAt(0).toUpperCase() }}</div>
                <div>
                  <p class="author-name">{{ review.authorName }}</p>
                  <p class="review-date">{{ review.date }}</p>
                </div>
              </div>
              <el-rate :model-value="review.rating" disabled :max="5" size="small" />
            </div>

            <div class="review-target">
              <el-tag :type="review.type === 'plant' ? 'success' : 'warning'" size="small">
                {{ review.type === 'plant' ? 'Plant' : 'Shop' }}
              </el-tag>
              <span class="target-name">{{ review.targetName }}</span>
            </div>

            <p class="review-comment">{{ review.comment }}</p>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { ElMessage } from "element-plus";

const STORAGE_KEY = "floranest_reviews";

const form = ref({
  type: "plant",
  targetName: "",
  authorName: "",
  rating: 5,
  comment: ""
});

const reviews = ref(JSON.parse(localStorage.getItem(STORAGE_KEY)) || []);

const canSubmit = computed(() =>
  form.value.targetName.trim() &&
  form.value.authorName.trim() &&
  form.value.comment.trim()
);

const sortedReviews = computed(() =>
  [...reviews.value].sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
);

const submitReview = () => {
  if (!canSubmit.value) {
    ElMessage.warning("Please fill in all fields.");
    return;
  }

  const now = new Date();
  const newReview = {
    type: form.value.type,
    targetName: form.value.targetName.trim(),
    authorName: form.value.authorName.trim(),
    rating: form.value.rating,
    comment: form.value.comment.trim(),
    date: now.toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" }),
    timestamp: now.toISOString()
  };

  reviews.value.unshift(newReview);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(reviews.value));

  ElMessage.success("Review submitted! Thank you for sharing.");

  form.value.targetName = "";
  form.value.authorName = "";
  form.value.rating = 5;
  form.value.comment = "";
};
</script>

<style scoped>
.reviews-wrapper {
  padding: 44px 24px;
  min-height: 100vh;
}

.reviews-header {
  text-align: center;
  margin-bottom: 40px;
}

.header-eyebrow {
  display: inline-block;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--fn-green-600);
  margin-bottom: 8px;
}

.reviews-header h1 {
  color: var(--fn-ink);
  font-size: clamp(1.8rem, 4vw, 2.5rem);
  margin-bottom: 8px;
  letter-spacing: -0.03em;
}

.reviews-header p {
  color: var(--fn-text-3);
  font-size: 0.95rem;
}

.reviews-content {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

/* Submit / Feed sections */
.submit-section,
.feed-section {
  background: #fff;
  padding: 32px;
  border-radius: var(--fn-radius-md);
  border: 1px solid var(--fn-border);
  box-shadow: var(--fn-shadow);
}

.submit-section h2,
.feed-section h2 {
  color: var(--fn-ink);
  font-size: 1.3rem;
  margin-bottom: 20px;
  letter-spacing: -0.02em;
}

/* Review card */
.review-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-card {
  background: var(--fn-surface-2);
  padding: 20px;
  border-radius: var(--fn-radius);
  border: 1px solid var(--fn-border);
  border-left: 3px solid var(--fn-green-500);
  transition: all var(--fn-t);
}

.review-card:hover {
  border-color: var(--fn-green-200);
  border-left-color: var(--fn-green-500);
  box-shadow: var(--fn-shadow-sm);
}

.review-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.review-author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--fn-green-600), var(--fn-green-700));
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 16px;
}

.author-name {
  font-weight: 600;
  color: var(--fn-ink);
  font-size: 14px;
  margin: 0;
}

.review-date {
  font-size: 12px;
  color: var(--fn-text-3);
  margin: 0;
}

.review-target {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.target-name {
  font-weight: 600;
  color: var(--fn-green-700);
  font-size: 14px;
}

.review-comment {
  color: var(--fn-text-2);
  line-height: 1.6;
  font-size: 14px;
  margin: 0;
}

.empty {
  text-align: center;
  padding: 48px 20px;
  color: var(--fn-text-3);
}

.empty-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  background: var(--fn-green-50);
  border-radius: 50%;
  color: var(--fn-green-400);
}

.empty p {
  font-size: 14px;
}

/* Responsive */
@media (max-width: 900px) {
  .reviews-content {
    grid-template-columns: 1fr;
  }
  .reviews-wrapper {
    padding: 24px 16px;
  }
  .submit-section,
  .feed-section {
    padding: 24px 20px;
  }
}
</style>
