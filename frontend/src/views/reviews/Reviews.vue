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
              <el-radio-button value="plant">Plant</el-radio-button>
              <el-radio-button value="shop">Shop</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item :label="form.type === 'plant' ? 'Plant Name' : 'Shop Name'">
            <el-input v-model="form.targetName" :placeholder="form.type === 'plant' ? 'e.g. Monstera Deliciosa' : 'e.g. Indoor Oasis'" />
          </el-form-item>

          <el-form-item label="Your Name">
            <div v-if="loggedInUser" class="account-author">
              <span class="account-avatar">{{ loggedInUser.fullName?.charAt(0).toUpperCase() || 'U' }}</span>
              <span>
                {{ loggedInUser.fullName }}
                <small>Posting under your account</small>
              </span>
            </div>
            <template v-else>
              <el-input v-model="form.authorName" placeholder="Your name" :disabled="posting" />
              <p class="field-hint">
                Tip: <router-link to="/login">Login</router-link> to post under your account.
              </p>
            </template>
          </el-form-item>

          <el-form-item label="Rating">
            <el-rate v-model="form.rating" :max="5" show-text :texts="['Poor','Fair','Good','Very Good','Excellent']" />
          </el-form-item>

          <el-form-item label="Your Review">
            <el-input v-model="form.comment" type="textarea" :rows="4" placeholder="Share your experience..." />
          </el-form-item>

          <el-button type="success" size="large" @click="submitReview" :disabled="!canSubmit" :loading="posting">
            Submit Review
          </el-button>
        </el-form>
      </div>

      <!-- RIGHT: Recent reviews -->
      <div class="feed-section" v-loading="feedLoading">
        <h2>Recent Reviews <span class="feed-count">{{ reviews.length }}</span></h2>

        <div v-if="reviews.length === 0 && !feedLoading" class="empty">
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
                  <p class="review-date">{{ formatDate(review.createdAt) }}</p>
                </div>
              </div>
              <el-rate :model-value="review.rating" disabled :max="5" size="small" />
            </div>

            <div class="review-target">
              <el-tag :type="review.reviewType === 'plant' ? 'success' : 'warning'" size="small">
                {{ review.reviewType === 'plant' ? 'Plant' : 'Shop' }}
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
import { ref, computed, onMounted, watch } from "vue";
import { ElMessage } from "element-plus";
import api from "@/api/axios";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const loggedInUser = computed(() => userStore.user);

const form = ref({
  type: "plant",
  targetName: "",
  authorName: "",
  rating: 5,
  comment: ""
});

const reviews = ref([]);
const feedLoading = ref(false);
const posting = ref(false);

const canSubmit = computed(() => {
  const authorOk = loggedInUser.value
    ? Boolean(loggedInUser.value.fullName && loggedInUser.value.fullName.trim())
    : Boolean(form.value.authorName.trim());
  return (
    Boolean(form.value.targetName.trim()) &&
    authorOk &&
    Boolean(form.value.comment.trim())
  );
});

const sortedReviews = computed(() =>
  [...reviews.value].sort((a, b) =>
    new Date(b.createdAt || 0) - new Date(a.createdAt || 0)
  )
);

const formatDate = (value) => {
  if (!value) return "";
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return String(value).slice(0, 10);
  return d.toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric"
  });
};

const loadReviews = async () => {
  feedLoading.value = true;
  try {
    const { data } = await api.get("/reviews");
    reviews.value = Array.isArray(data) ? data : [];
  } catch (error) {
    console.error(error);
    reviews.value = [];
  } finally {
    feedLoading.value = false;
  }
};

const submitReview = async () => {
  if (!canSubmit.value) {
    ElMessage.warning("Please fill in all fields.");
    return;
  }

  posting.value = true;

  try {
    const user = loggedInUser.value;

    const response = await api.post("/reviews", {
      reviewType: form.value.type,
      targetName: form.value.targetName.trim(),
      authorName: user && user.fullName ? user.fullName.trim() : form.value.authorName.trim(),
      rating: form.value.rating,
      comment: form.value.comment.trim(),
      userId: user ? user.userId : null
    });

    if (response.data.success) {
      ElMessage.success("Review submitted! Thank you for sharing.");
      form.value.targetName = "";
      form.value.rating = 5;
      form.value.comment = "";
      await loadReviews();
    } else {
      ElMessage.error(response.data.message);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error("Unable to connect to the server. Please try again.");
  } finally {
    posting.value = false;
  }
};

watch(loggedInUser, (user) => {
  if (user && user.fullName) {
    form.value.authorName = user.fullName;
  }
}, { immediate: true });

onMounted(loadReviews);
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

.feed-count {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 26px;
  height: 22px;
  padding: 0 8px;
  margin-left: 6px;
  border-radius: 999px;
  background: var(--fn-green-50);
  color: var(--fn-green-700);
  font-size: 12px;
  font-weight: 700;
  vertical-align: middle;
}

/* Account chip inside the "Your Name" field */
.account-author {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 8px 12px;
  background: var(--fn-surface-2);
  border: 1px solid var(--fn-border);
  border-radius: var(--fn-radius);
  color: var(--fn-ink);
  font-weight: 600;
  font-size: 14px;
}

.account-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--fn-green-600), var(--fn-green-700));
  color: #fff;
  font-size: 13px;
  flex-shrink: 0;
}

.account-author small {
  display: block;
  font-size: 12px;
  color: var(--fn-text-3);
  font-weight: 400;
}

.field-hint {
  margin: 6px 0 0;
  font-size: 12px;
  color: var(--fn-text-3);
}

.field-hint a {
  color: var(--fn-green-600);
  font-weight: 600;
  text-decoration: none;
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
