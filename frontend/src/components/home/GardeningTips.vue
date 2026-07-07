<template>
  <section class="tips">
    <h2>Gardening Tips 🌻</h2>
    <p class="subtitle">Seasonal advice to keep your plants thriving</p>

    <div class="tips-grid">
      <div class="tip-card" v-for="tip in tips" :key="tip.title">
        <div class="tip-icon">{{ tip.icon }}</div>
        <h3>{{ tip.title }}</h3>
        <p>{{ tip.text }}</p>
      </div>
    </div>
  </section>
</template>

<script setup>
const tips = [
  {
    icon: '💧',
    title: 'Water Wisely',
    text: 'Most plants prefer deep, infrequent watering over light daily sprinkles. Check the soil before watering — if it feels moist 2 inches down, wait another day.'
  },
  {
    icon: '☀️',
    title: 'Know Your Light',
    text: 'Match your plant to your window. South-facing windows get the most sun, ideal for succulents and herbs. Low-light plants like pothos thrive anywhere.'
  },
  {
    icon: '🪴',
    title: 'Repot in Spring',
    text: 'Spring is the best time to repot overgrown plants. Look for roots poking out of drainage holes as a sign your plant needs more space.'
  },
  {
    icon: '🌿',
    title: 'Feed in Growing Season',
    text: 'Fertilize every 2–4 weeks during spring and summer. Avoid feeding in autumn and winter when most plants are resting.'
  },
  {
    icon: '✂️',
    title: 'Prune for Health',
    text: 'Remove dead or yellowing leaves promptly. Regular pruning encourages bushier growth and prevents disease from spreading to healthy foliage.'
  },
  {
    icon: '🌡️',
    title: 'Watch the Temperature',
    text: 'Most houseplants prefer 15–24°C. Keep them away from cold drafts, air conditioning vents, and radiators that cause sudden temperature swings.'
  }
];
</script>

<style scoped>
.tips { margin: 50px 0; }
.tips h2 {
  text-align: center;
  color: #2E7D32;
  font-size: 26px;
  margin-bottom: 8px;
}
.subtitle {
  text-align: center;
  color: #888;
  margin-bottom: 30px;
}
.tips-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}
.tip-card {
  background: white;
  padding: 25px;
  border-radius: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.07);
  border-left: 4px solid #2E7D32;
  transition: 0.25s;
}
.tip-card:hover { transform: translateY(-3px); }
.tip-icon { font-size: 30px; margin-bottom: 10px; }
.tip-card h3 { color: #2E7D32; margin-bottom: 8px; }
.tip-card p  { color: #555; font-size: 14px; line-height: 1.6; }
</style>