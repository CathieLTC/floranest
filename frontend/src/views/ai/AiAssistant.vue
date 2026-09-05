<template>
  <div class="ai-page">

    <!-- Page header -->
    <div class="page-header">
      <el-tag type="success" effect="plain" round>✨ Powered by AI</el-tag>
      <h1>FloraNest AI Assistant</h1>
      <p>Chat with our gardening expert, get personalised plant recommendations,
         search using natural language, or detect plant diseases.</p>
    </div>

    <!-- Four AI features in tabs -->
    <div class="content">
      <el-tabs v-model="activeTab" stretch>

        <!-- Tab 1: Gardening Chat -->
        <el-tab-pane name="chat">
          <template #label>
            <span class="tab-label">💬 Gardening Chat</span>
          </template>
          <div class="tab-intro">
            <h2>Ask the Gardening Assistant</h2>
            <p>Get instant advice on watering, light, pests, fertilising and more.</p>
          </div>
          <GardeningChat />
        </el-tab-pane>

        <!-- Tab 2: Plant Recommendations -->
        <el-tab-pane name="recommend">
          <template #label>
            <span class="tab-label">🌱 Recommendations</span>
          </template>
          <div class="tab-intro">
            <h2>Find Your Perfect Plant</h2>
            <p>Answer a few quick questions and our AI will suggest the best plants for your space and lifestyle.</p>
          </div>
          <PlantRecommendation />
        </el-tab-pane>

        <!-- Tab 3: Smart Search -->
        <el-tab-pane name="search">
          <template #label>
            <span class="tab-label">🔍 Smart Search</span>
          </template>
          <div class="tab-intro">
            <h2>Search in Natural Language</h2>
            <p>Instead of typing plant names, describe what you want — our AI finds the best matches.</p>
          </div>
          <SmartSearch />
        </el-tab-pane>

        <!-- Tab 4: Disease Detection -->
        <el-tab-pane name="disease">
          <template #label>
            <span class="tab-label">🩺 Disease Detection</span>
          </template>
          <div class="tab-intro">
            <h2>Plant Disease Detection</h2>
            <p>Upload a picture of your plant and let FloraNest AI help identify possible diseases.</p>
          </div>

          <!-- Upload + Controls -->
          <div class="disease-section">
            <div class="upload-box">
              <el-upload
                drag
                action="#"
                :auto-upload="false"
                :show-file-list="true"
                :limit="1"
                :on-change="handleFileChange"
              >
                <el-icon class="upload-icon"><UploadFilled /></el-icon>
                <div class="el-upload__text">
                  Drop image here or <em>Click to Upload</em>
                </div>
              </el-upload>
            </div>

            <div class="controls">
              <el-select v-model="plant" placeholder="Select Plant (optional)" class="select" clearable>
                <el-option label="Tomato" value="Tomato"/>
                <el-option label="Rose" value="Rose"/>
                <el-option label="Snake Plant" value="Snake Plant"/>
                <el-option label="Aloe Vera" value="Aloe Vera"/>
                <el-option label="Monstera" value="Monstera"/>
              </el-select>

              <el-button type="success" size="large" :loading="loading" @click="analyzePlant">
                Analyze Plant
              </el-button>
            </div>
          </div>

          <!-- Result -->
          <section class="result" v-if="showResult">
            <h2>Diagnosis Result</h2>
            <div class="result-card" :class="disease.healthy ? 'healthy' : 'diseased'">
              <div class="status-banner">
                <span class="status-icon">{{ disease.healthy ? '✅' : '⚠️' }}</span>
                <h3>{{ disease.name }}</h3>
              </div>

              <div class="meta-row">
                <p><strong>Confidence:</strong> {{ disease.confidence }}</p>
                <p v-if="!disease.healthy"><strong>Cause:</strong> {{ disease.cause }}</p>
              </div>

              <div v-if="disease.healthy" class="healthy-message">
                <p>🎉 Your plant looks healthy! Keep up the great care.</p>
              </div>

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
        </el-tab-pane>

      </el-tabs>
    </div>

  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { UploadFilled } from "@element-plus/icons-vue";

import GardeningChat       from "@/components/ai/GardeningChat.vue";
import PlantRecommendation from "@/components/ai/PlantRecommendation.vue";
import SmartSearch         from "@/components/ai/SmartSearch.vue";
import { analyzePlantImage } from "@/api/ai";

const activeTab = ref("chat");

/* ── Disease Detection state & logic ── */
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

  // Free vision models sometimes truncate or ramble around the JSON object.
  // Retrying a few times turns those transient failures into a success.
  const MAX_ATTEMPTS = 3;

  try {
    for (let attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
      try {
        const response = await analyzePlantImage(imageFile.value);
        const result   = extractAiJson(response);

        let confidence = result.confidence;
        if (typeof confidence === "number") {
          confidence = Math.round(confidence * 100) + "%";
        } else if (
          typeof confidence === "string" &&
          confidence.trim() &&
          !confidence.includes("%")
        ) {
          const numeric = Number(confidence);
          if (!Number.isNaN(numeric)) {
            confidence = Math.round(numeric * 100) + "%";
          }
        }

        const plantName = result.plantName || "Plant";
        disease.value = {
          name:       result.healthy
                        ? `${plantName} (Healthy)`
                        : `${plantName} — ${result.disease || "Unknown issue"}`,
          confidence: confidence || "n/a",
          cause:      result.cause      || "n/a",
          healthy:    Boolean(result.healthy),
          symptoms:   result.symptoms   || [],
          treatment:  result.treatment  || [],
          prevention: result.prevention || []
        };

        showResult.value = true;
        ElMessage.success("Plant analysed successfully!");
        return;
      } catch (error) {
        const parseFailed = error?.message
          && error.message.includes("did not contain a valid JSON result");
        if (attempt < MAX_ATTEMPTS && parseFailed) {
          ElMessage.warning(
            `The AI response was incomplete. Retrying (attempt ${attempt} of ${MAX_ATTEMPTS})…`
          );
          await new Promise(r => setTimeout(r, 1800));
          continue;
        }
        throw error;
      }
    }
  } catch (error) {
    console.error(error);
    const detail = error?.message ? ` ${error.message}` : "";
    ElMessage.error(`Failed to analyse plant. Please try again.${detail}`);
  } finally {
    loading.value = false;
  }
}

/**
 * Extracts the model's text from an OpenAI-style response and parses the
 * JSON object inside it. Tolerates code fences, surrounding prose and
 * models that double-encode the JSON as a quoted string.
 */
function extractAiJson(response) {
  const content = response.choices?.[0]?.message?.content;

  // Some vision models return content as an array of text/image parts.
  const text = Array.isArray(content)
    ? content
        .map(part => (typeof part === "string" ? part : part?.text || ""))
        .filter(Boolean)
        .join("\n")
    : String(content ?? "");

  const stripFences = str =>
    str.replace(/```json/gi, "").replace(/```/g, "").trim();

  const candidates = [stripFences(text)];

  // Some models wrap the whole JSON object in a quoted/escaped string.
  if (candidates[0].startsWith('"')) {
    try {
      const unquoted = JSON.parse(candidates[0]);
      if (typeof unquoted === "string" && unquoted.trim()) {
        candidates.push(stripFences(unquoted));
      }
    } catch { /* not double-encoded — ignore */ }
  }

  /**
   * Scans from the first "{" outward and returns the first substring that
   * parses as JSON. This survives prose wrapped around the object (even when
   * the trailing prose itself contains stray braces) and trailing commas.
   */
  function scanForJson(str) {
    const start = str.indexOf("{");
    if (start === -1) return undefined;
    for (let end = start; end < str.length; end++) {
      if (str[end] !== "}") continue;
      const block = str.slice(start, end + 1);
      try { return JSON.parse(block); } catch { /* keep scanning */ }
      // Tolerate trailing commas — a common LLM mistake.
      try { return JSON.parse(block.replace(/,\s*([}\]])/g, "$1")); } catch { /* keep scanning */ }
    }
    return undefined;
  }

  for (const candidate of candidates) {
    if (!candidate) continue;

    // 1) Direct parse.
    try { return JSON.parse(candidate); } catch { /* try next strategy */ }

    // 2) Scan for the first complete { ... } object.
    const found = scanForJson(candidate);
    if (found) return found;
  }

  // Log the full reply — the error below only shows a preview of it.
  console.warn("[extractAiJson] Could not find JSON in model reply:", text);

  // Surface the model's actual reply so failures are easy to diagnose.
  const snippet = text.trim().slice(0, 200);
  throw new Error(
    "The AI response did not contain a valid JSON result." +
    (snippet ? ` The model replied: "${snippet}"` : " The model returned an empty response.")
  );
}

function handleFileChange(file) {
  imageFile.value = file.raw;
}
</script>

<style scoped>
.ai-page {
  min-height: 100vh;
  background: transparent;
  padding-bottom: 60px;
}

.page-header {
  text-align: center;
  padding: 48px 24px 32px;
}

.page-header .el-tag { margin-bottom: 16px; font-weight: 600; }

.page-header h1 {
  font-size: clamp(1.8rem, 4vw, 2.6rem);
  color: #1B5E20;
  margin: 0 0 12px;
}

.page-header p {
  color: #555;
  font-size: 1.05rem;
  max-width: 580px;
  margin: 0 auto;
  line-height: 1.6;
}

.content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(46,125,50,0.08);
  border: 1px solid #e8f5e9;
}

.tab-label { font-weight: 600; font-size: 14px; }

.tab-intro {
  padding: 20px 0 16px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 24px;
}

.tab-intro h2 { margin: 0 0 6px; color: #2E7D32; font-size: 1.2rem; }
.tab-intro p  { margin: 0; color: #666; font-size: 0.95rem; }

:deep(.el-tabs__item.is-active) { color: #2E7D32; font-weight: 700; }
:deep(.el-tabs__active-bar)     { background: #2E7D32; }
:deep(.el-tabs__content)        { padding: 0 20px 24px; }

/* ── Disease Detection ── */
.disease-section {
  padding: 10px 0;
}

.upload-box { margin-bottom: 20px; }

.upload-icon {
  font-size: 40px;
  color: #2E7D32;
}

.controls {
  display: flex;
  gap: 15px;
  align-items: center;
  flex-wrap: wrap;
}

.select { width: 250px; }

.result {
  margin-top: 40px;
}

.result h2 {
  color: #2E7D32;
  margin-bottom: 15px;
}

.result-card {
  background: #f8faf8;
  padding: 25px;
  border-radius: 15px;
  box-shadow: 0 3px 10px rgba(0,0,0,0.05);
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

@media (max-width: 640px) {
  .page-header { padding: 32px 16px 24px; }
  .content     { padding: 0 12px; border-radius: 12px; }
  .controls    { flex-direction: column; align-items: stretch; }
  .select      { width: 100%; }
}
</style>
