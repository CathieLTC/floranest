<template>
  <div class="care-page">

    <!-- Hero -->
    <section class="hero">
      <h1>Plant Care Guide</h1>
      <p>
        Learn how to keep your plants healthy with proper watering,
        sunlight, fertilizer, and disease prevention.
      </p>
    </section>

    <!-- Plant Care Cards -->
    <GardeningTips />
    <section class="tools">

      <h2>🛠️ Gardening Tools</h2>
      <p class="tools-subtitle">The essentials every plant parent should keep on hand</p>

      <div class="tools-grid">
        <div class="tool-card" v-for="tool in tools" :key="tool.title">
          <div class="tool-icon">{{ tool.icon }}</div>
          <h3>{{ tool.title }}</h3>
          <p>{{ tool.text }}</p>
        </div>
      </div>

    </section>

    <!-- Quick Tips -->
    <section class="tips">

      <h2>🌼 Quick Care Tips</h2>

      <ul>
        <li>✔ Avoid overwatering.</li>
        <li>✔ Use pots with drainage holes.</li>
        <li>✔ Rotate plants weekly.</li>
        <li>✔ Remove yellow leaves.</li>
        <li>✔ Keep leaves clean.</li>

      </ul>

    </section>

    <!-- Disease Detection -->

    <section class="disease">

      <h2>🩺 Plant Disease Detection</h2>

      <p>
        Upload a picture of your plant and let FloraNest help identify
        possible diseases.
      </p>

      <div class="upload-box">

        <el-upload
          drag
          action="#"
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          :on-change="handleFileChange"
      >

          <el-icon class="upload-icon">
            <UploadFilled />
          </el-icon>

          <div class="el-upload__text">
            Drop image here or <em>Click to Upload</em>
          </div>

        </el-upload>

      </div>

      <div class="controls">

        <el-select
          v-model="plant"
          placeholder="Select Plant"
          class="select"
        >
          <el-option label="Tomato" value="Tomato"/>
          <el-option label="Rose" value="Rose"/>
          <el-option label="Snake Plant" value="Snake Plant"/>
          <el-option label="Aloe Vera" value="Aloe Vera"/>
          <el-option label="Monstera" value="Monstera"/>
        </el-select>

        <el-button
            type="success"
            size="large"
            :loading="loading"
            @click="analyzePlant"
        >
            Analyze Plant
        </el-button>

      </div>

    </section>

    <!-- Result -->

   <!-- Result -->
    <section class="result" v-if="showResult">

      <h2>Diagnosis Result</h2>

      <div class="result-card" :class="disease.healthy ? 'healthy' : 'diseased'">

        <!-- Status banner -->
        <div class="status-banner">
          <span class="status-icon">{{ disease.healthy ? '✅' : '⚠️' }}</span>
          <h3>{{ disease.name }}</h3>
        </div>

        <div class="meta-row">
          <p><strong>Confidence:</strong> {{ disease.confidence }}</p>
          <p v-if="!disease.healthy"><strong>Cause:</strong> {{ disease.cause }}</p>
        </div>

        <!-- Healthy message -->
        <div v-if="disease.healthy" class="healthy-message">
          <p>🎉 Your plant looks healthy! Keep up the great care.</p>
        </div>

        <!-- Disease details — only shown when not healthy -->
        <template v-else>
          <div class="detail-block">
            <h4>🔍 Symptoms</h4>
            <ul>
              <li v-for="item in disease.symptoms" :key="item">{{ item }}</li>
            </ul>
          </div>

          <div class="detail-block">
            <h4>💊 Treatment</h4>
            <ul>
              <li v-for="item in disease.treatment" :key="item">{{ item }}</li>
            </ul>
          </div>
        </template>

        <!-- Prevention always shown -->
        <div class="detail-block">
          <h4>🛡️ Prevention Tips</h4>
          <ul v-if="disease.prevention.length">
            <li v-for="item in disease.prevention" :key="item">{{ item }}</li>
          </ul>
          <ul v-else>
            <li>Water at the base, not on the leaves.</li>
            <li>Ensure good drainage to prevent root rot.</li>
            <li>Inspect regularly for early signs of pests.</li>
            <li>Keep leaves clean to maximise light absorption.</li>
          </ul>
        </div>

      </div>

    </section>

    <!-- AI -->

    <section class="ai-help">

      <h2>🤖 Need More Help?</h2>

      <p>
        Still unsure about your plant?
        Visit our AI Assistant for personalized advice.
      </p>

      <router-link to="/ai">

        <el-button
          type="success"
          size="large"
        >
          Ask FloraNest AI
        </el-button>

      </router-link>

    </section>

  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { UploadFilled } from "@element-plus/icons-vue";
import GardeningTips from "@/components/home/GardeningTips.vue";
import { analyzePlantImage } from "@/api/ai";

const tools = [
  {
    icon: "✂️",
    title: "Pruning Shears",
    text: "For clean cuts that remove dead or overgrown leaves without crushing the stem."
  },
  {
    icon: "🚿",
    title: "Watering Can",
    text: "A narrow spout gives you control so you water the soil, not the leaves."
  },
  {
    icon: "🧤",
    title: "Gardening Gloves",
    text: "Protect your hands when repotting, pruning, or handling thorny or irritant plants."
  },
  {
    icon: "🪴",
    title: "Pots with Drainage",
    text: "Good drainage holes prevent root rot by letting excess water escape."
  },
  {
    icon: "🌡️",
    title: "Soil Moisture Meter",
    text: "Takes the guesswork out of watering by telling you exactly when soil is dry."
  },
  {
    icon: "🧹",
    title: "Soft Cleaning Brush",
    text: "Keep leaves dust-free so they can absorb light efficiently."
  }
];

const plant = ref("");
const imageFile = ref(null);
const loading = ref(false);
const showResult = ref(false);

const disease = ref({
  name:       "",
  confidence: "",
  cause:      "",
  healthy:    false,
  symptoms:   [],
  treatment:  [],
  prevention: []
});

async function analyzePlant() {
  if (!imageFile.value) {
    ElMessage.warning("Please upload a plant image first.");
    return;
  }

  loading.value    = true;
  showResult.value = false;

  try {
    const response = await analyzePlantImage(imageFile.value);

    // Parse the AI text response
    const aiText = response.choices[0].message.content
      .replace(/```json/g, "")
      .replace(/```/g,     "")
      .trim();

    const result = JSON.parse(aiText);

    // Format confidence as percentage if it comes as decimal
    let confidence = result.confidence;
    if (typeof confidence === "number") {
      confidence = Math.round(confidence * 100) + "%";
    }

    disease.value = {
      name:       result.healthy
                    ? `${result.plantName} (Healthy)`
                    : `${result.plantName} — ${result.disease}`,
      confidence: confidence,
      cause:      result.cause      || "n/a",
      healthy:    result.healthy    || false,
      symptoms:   result.symptoms   || [],
      treatment:  result.treatment  || [],
      prevention: result.prevention || []
    };

    showResult.value = true;
    ElMessage.success("Plant analysed successfully!");

  } catch (error) {
    console.error(error);
    ElMessage.error("Failed to analyse plant. Please try again.");
  } finally {
    loading.value = false;
  }
}

function handleFileChange(file) {
  imageFile.value = file.raw;
}

</script>

<style scoped>
.care-page{
  padding:50px;
  background:#f5fff7;
  min-height:100vh;
}

/* ================= HERO ================= */

.hero{
  text-align:center;
  margin-bottom:50px;
}

.hero h1{
  font-size:42px;
  color:#2E7D32;
  margin-bottom:10px;
}

.hero p{
  font-size:18px;
  color:#666;
  max-width:700px;
  margin:auto;
}

/* ================= CARE GRID ================= */

.care-grid{
  display:grid;
  grid-template-columns:repeat(auto-fit,minmax(250px,1fr));
  gap:25px;
  margin-bottom:50px;
}

.care-card{
  background:white;
  padding:25px;
  border-radius:15px;
  text-align:center;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
  transition:0.3s;
}

.care-card:hover{
  transform:translateY(-8px);
}

.icon{
  font-size:45px;
  margin-bottom:10px;
}

.care-card h2{
  color:#2E7D32;
  margin-bottom:10px;
}

/* ================= TIPS ================= */

.tips{
  background:white;
  padding:30px;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
  margin-bottom:60px;
}

.tips h2{
  color:#2E7D32;
  margin-bottom:15px;
}

.tips ul{
  list-style:none;
  padding:0;
}

.tips li{
  padding:10px 0;
  border-bottom:1px solid #eee;
  color:#555;
}

/* ================= GARDENING TOOLS ================= */

.tools{
  background: white;
  padding: 35px;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0,0,0,.08);
  margin-bottom: 60px;
}

.tools h2{
  text-align:center;
  color:#2E7D32;
  font-size:26px;
  margin-bottom:8px;
}

.tools-subtitle{
  text-align:center;
  color:#888;
  margin-bottom:35px;
  font-size:15px;
}

.tools-grid{
  display:grid;
  grid-template-columns:repeat(auto-fit,minmax(260px,1fr));
  gap:25px;
  margin-top:30px;
}

.tool-card{
  background:white;
  padding:25px;
  border-radius:14px;
  box-shadow:0 4px 12px rgba(0,0,0,.07);
  border-left:4px solid #2E7D32;
  transition:.25s;
}

.tool-card:hover{
  transform:translateY(-4px);
  box-shadow:0 8px 20px rgba(46,125,50,.15);
}

.tool-icon{
  font-size:30px;
  margin-bottom:10px;
}

.tool-card h3{
  color:#2E7D32;
  margin-bottom:8px;
}

.tool-card p{
  color:#555;
  font-size:14px;
  line-height:1.6;
}

/* ================= DISEASE SECTION ================= */

.disease{
  margin-top:50px;
  padding:30px;
  background:white;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.disease h2{
  color:#2E7D32;
  margin-bottom:10px;
}

.disease p{
  color:#666;
  margin-bottom:20px;
}

/* Upload Box */

.upload-box{
  margin-bottom:20px;
}

.upload-icon{
  font-size:40px;
  color:#2E7D32;
}

/* Controls */

.controls{
  display:flex;
  gap:15px;
  align-items:center;
  flex-wrap:wrap;
}

.select{
  width:250px;
}

/* ================= RESULT ================= */

.result{
  margin-top:50px;
}

.result h2{
  color:#2E7D32;
  margin-bottom:15px;
}

.result-card{
  background:white;
  padding:25px;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.result-card h3{
  color:#2E7D32;
  margin-bottom:10px;
}

.result-card h4{
  margin-top:15px;
  color:#333;
}

.result-card ul{
  margin-left:20px;
}

.result-card li{
  margin-bottom:5px;
  color:#555;
}

/* ================= AI SECTION ================= */

.ai-help{
  margin-top:60px;
  text-align:center;
  padding:40px;
  background:white;
  border-radius:15px;
  box-shadow:0 5px 15px rgba(0,0,0,.08);
}

.ai-help h2{
  color:#2E7D32;
  margin-bottom:10px;
}

.ai-help p{
  color:#666;
  margin-bottom:20px;
}

/* ================= RESPONSIVE ================= */

@media (max-width: 768px){

  .controls{
    flex-direction:column;
    align-items:stretch;
  }

  .select{
    width:100%;
  }
}

.result-card.healthy  { border-left: 5px solid #2E7D32; }
.result-card.diseased { border-left: 5px solid #f44336; }

.status-banner {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.status-icon { font-size: 28px; }

.status-banner h3 {
  margin: 0;
  color: #2E7D32;
  font-size: 18px;
}

.meta-row {
  display: flex;
  gap: 30px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.healthy-message {
  background: #e8f5e9;
  padding: 16px 20px;
  border-radius: 10px;
  color: #2E7D32;
  font-size: 15px;
  margin-bottom: 16px;
}

.detail-block {
  margin-top: 18px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.detail-block h4 {
  color: #333;
  margin-bottom: 10px;
  font-size: 15px;
}

.detail-block ul {
  margin-left: 20px;
  padding: 0;
}

.detail-block li {
  margin-bottom: 6px;
  color: #555;
  line-height: 1.5;
}
</style>
