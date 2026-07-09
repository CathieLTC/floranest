<template>
  <div class="care-page">

    <!-- Hero -->
    <section class="hero">
      <h1>🌿 Plant Care Guide</h1>
      <p>
        Learn how to keep your plants healthy with proper watering,
        sunlight, fertilizer, and disease prevention.
      </p>
    </section>

    <!-- Plant Care Cards -->
    <section class="care-grid">

      <div class="care-card">
        <div class="icon">💧</div>
        <h2>Watering</h2>
        <p>
          Water your plants only when the top layer of soil feels dry.
          Overwatering can cause root rot.
        </p>
      </div>

      <div class="care-card">
        <div class="icon">☀️</div>
        <h2>Sunlight</h2>
        <p>
          Most indoor plants need bright indirect sunlight.
          Keep them away from harsh afternoon sun.
        </p>
      </div>

      <div class="care-card">
        <div class="icon">🌱</div>
        <h2>Fertilizer</h2>
        <p>
          Feed your plants every 4–6 weeks using a balanced fertilizer.
        </p>
      </div>

      <div class="care-card">
        <div class="icon">🌡️</div>
        <h2>Temperature</h2>
        <p>
          Keep plants between 18°C and 27°C for healthy growth.
        </p>
      </div>

      <div class="care-card">
        <div class="icon">🪴</div>
        <h2>Repotting</h2>
        <p>
          Repot every 1–2 years to encourage healthy root development.
        </p>
      </div>

      <div class="care-card">
        <div class="icon">🐛</div>
        <h2>Pest Control</h2>
        <p>
          Inspect leaves regularly and remove pests before they spread.
        </p>
      </div>

    </section>

    <GardeningTips />

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
          @click="analyzePlant"
        >
          Analyze Plant
        </el-button>

      </div>

    </section>

    <!-- Result -->

    <section
      class="result"
      v-if="showResult"
    >

      <h2>Diagnosis Result</h2>

      <div class="result-card">

        <h3>{{ disease.name }}</h3>

        <p>
          <strong>Confidence:</strong>
          {{ disease.confidence }}
        </p>

        <p>
          <strong>Cause:</strong>
          {{ disease.cause }}
        </p>

        <h4>Symptoms</h4>

        <ul>
          <li
            v-for="item in disease.symptoms"
            :key="item"
          >
            {{ item }}
          </li>
        </ul>

        <h4>Treatment</h4>

        <ul>
          <li
            v-for="item in disease.treatment"
            :key="item"
          >
            {{ item }}
          </li>
        </ul>

        <h4>Prevention</h4>

        <ul>
          <li
            v-for="item in disease.prevention"
            :key="item"
          >
            {{ item }}
          </li>
        </ul>

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
import { UploadFilled } from "@element-plus/icons-vue";
import GardeningTips  from '@/components/home/GardeningTips.vue';

const plant = ref("");

const showResult = ref(false);

const disease = ref({});

function analyzePlant(){

    disease.value = {

        name:"Leaf Spot",

        confidence:"96%",

        cause:"Leaf Spot is a fungal disease caused by excessive moisture and poor air circulation.",

        symptoms:[
            "Brown circular spots",
            "Yellow leaf edges",
            "Dry patches",
            "Leaf drop"
        ],

        treatment:[
            "Remove infected leaves",
            "Apply fungicide",
            "Avoid overwatering",
            "Improve air circulation"
        ],

        prevention:[
            "Water the soil instead of leaves",
            "Keep plants well spaced",
            "Use clean gardening tools",
            "Inspect plants weekly"
        ]

    }

    showResult.value = true;

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
</style>