/**
 * FloraNest AI Service
 * All AI calls go through our Spring Boot backend at /ai/chat.
 * The backend holds the API key — it never touches the frontend.
 */
import api from "@/api/axios";

const RATE_LIMIT_MSG = "The AI service is rate limited. Please wait about a minute, then try again.";
const EMPTY_RESPONSE_MSG = "The AI service returned an empty response. Please try again.";

const SYSTEM_PROMPT = `You are FloraNest AI, a friendly and knowledgeable gardening
assistant for an online plant shop. You help users with:
- Plant care advice (watering, light, soil, fertilizing, repotting)
- Diagnosing plant problems (yellow leaves, pests, root rot)
- Recommending plants based on lifestyle and space
- Seasonal gardening tips

Keep responses concise (3-5 sentences), warm, and practical.
If a question is unrelated to plants or gardening, politely redirect
the user back to plant topics.`;

// ─── Shared helpers ───────────────────────────────────────────────────────────

// Space out consecutive AI calls to stay under the provider's rate limit.
let lastCallAt = 0;
const MIN_INTERVAL_MS = 1000;

async function waitForRateSlot() {
    const wait = MIN_INTERVAL_MS - (Date.now() - lastCallAt);
    if (wait > 0) await new Promise(r => setTimeout(r, wait));
    lastCallAt = Date.now();
}

/** Normalises an error from the backend proxy into a friendly message. */
function extractErrorMessage(e, fallback) {
    if (e.response?.status === 429) return RATE_LIMIT_MSG;
    return e.response?.data?.error?.message || e.message || fallback;
}

/** Posts a chat payload to the backend proxy and returns the parsed JSON body. */
async function postChat(payload) {
    await waitForRateSlot();
    try {
        const response = await api.post("/ai/chat", payload);
        return response.data;
    } catch (e) {
        throw new Error(
            extractErrorMessage(e, `Backend error ${e.response?.status || "unknown"}`),
            { cause: e }
        );
    }
}

/** Core caller — returns the assistant text from the provider response. */
async function callAI(messages, maxTokens = 500) {
    const data = await postChat({
        messages,
        max_tokens: maxTokens,
        temperature: 0.7
    });
    const content = data.choices?.[0]?.message?.content;
    if (typeof content !== "string" || !content.trim()) {
        throw new Error(EMPTY_RESPONSE_MSG);
    }
    return content;
}

/** Loads the full product catalogue from the backend (empty on failure). */
async function fetchProducts() {
    try {
        const response = await api.get("/products");
        return response.data;
    } catch (e) {
        console.error("Failed to fetch products:", e);
        return [];
    }
}

// ─── FEATURE 1: Gardening Chat ────────────────────────────────────────────────
export async function sendChatMessage(message, history = []) {
    const messages = [
        { role: "system", content: SYSTEM_PROMPT },
        ...history.slice(1).map(m => ({
            role:    m.role === "assistant" ? "assistant" : "user",
            content: m.content
        })),
        { role: "user", content: message }
    ];

    const content = await callAI(messages, 500);
    return {
        role:      "assistant",
        content:   content,
        timestamp: new Date().toISOString()
    };
}

// ─── FEATURE 2: Smart Search ──────────────────────────────────────────────────
export async function searchPlants(query, filters = {}) {
    try {
        const messages = [
            {
                role:    "system",
                content: `You are a plant search keyword extractor.
Extract 2-3 relevant keywords from the user's plant description.
Respond with ONLY a comma-separated list of keywords, nothing else.
Example: low light, shade tolerant, indoor`
            },
            { role: "user", content: query }
        ];

        const keywordText = await callAI(messages, 50);
        const keywords    = keywordText.split(",").map(k => k.trim().toLowerCase());
        const products    = await fetchProducts();

        return products
            .filter(p => {
                const nameMatch = keywords.some(kw =>
                    p.productName?.toLowerCase().includes(kw) ||
                    p.description?.toLowerCase().includes(kw)
                );
                const lightMatch = !filters.light ||
                    p.sunlight?.toLowerCase().includes(filters.light.toLowerCase());
                return nameMatch && lightMatch;
            })
            .map(p => ({
                id:          p.productId,
                name:        p.productName,
                description: p.description || "A beautiful plant for your space.",
                imageUrl:    p.imageUrl    || "https://images.unsplash.com/photo-1446071103084-c257b5f70672?w=400",
                careLevel:   p.difficulty  || "Easy",
                light:       p.sunlight    || "Varies",
                category:    "Indoor",
                matchScore:  null,
                reason:      null
            }));

    } catch (e) {
        console.error("Smart search error:", e);
        throw e;
    }
}

// ─── FEATURE 3: Plant Recommendations ────────────────────────────────────────
export async function getPlantRecommendations(preferences = {}) {
    try {
        const prefText = [
            `Location: ${preferences.location}`,
            `Experience: ${preferences.experience}`,
            `Space: ${preferences.space}`,
            `Light: ${preferences.light}`,
            `Watering: ${preferences.watering}`,
            `Goal: ${preferences.purpose}`
        ].join(", ");

        const messages = [
            {
                role:    "system",
                content: `You are a plant recommendation assistant.
Suggest exactly 3 plants based on user preferences.
Respond with ONLY a valid JSON array, no markdown, no extra text:
[{"name":"Plant Name","reason":"One sentence why","careLevel":"Easy","light":"Low to bright","category":"Indoor"}]`
            },
            { role: "user", content: `Preferences: ${prefText}` }
        ];

        const text        = await callAI(messages, 600);
        const clean       = text.replace(/```json|```/g, "").trim();
        const suggestions = JSON.parse(clean);
        const products    = await fetchProducts();

        return suggestions.map((s, i) => {
            const match = products.find(p =>
                p.productName?.toLowerCase().includes(
                    s.name.toLowerCase().split(" ")[0]
                )
            );
            return {
                id:          match?.productId || `ai-${i}`,
                name:        s.name,
                reason:      s.reason,
                careLevel:   s.careLevel || "Easy",
                light:       s.light     || "Varies",
                category:    s.category  || "Indoor",
                description: s.reason,
                imageUrl:    match?.imageUrl || "https://images.unsplash.com/photo-1463936575829-25148e1db1b8?w=400",
                matchScore:  95 - (i * 5)
            };
        });

    } catch (e) {
        console.error("Recommendation error:", e);
        throw e;
    }
}

// ─── FEATURE 4: Plant Disease Detection ──────────────────────────────────────
// Called by PlantCare.vue
// Accepts: an image File object from the upload component
// Returns: the raw API response (PlantCare.vue handles parsing)
export async function analyzePlantImage(imageFile) {

    // Convert image file to base64
    const base64 = await new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload  = () => resolve(reader.result.split(",")[1]);
        reader.onerror = reject;
        reader.readAsDataURL(imageFile);
    });

    const mimeType = imageFile.type || "image/jpeg";

    // Send to backend proxy — same /ai/chat endpoint
    return postChat({
        messages: [
            {
                role: "user",
                content: [
                    {
                        type: "image_url",
                        image_url: {
                            url: `data:${mimeType};base64,${base64}`
                        }
                    },
                    {
                        type: "text",
                        text: `Analyze this plant image and respond ONLY with a valid JSON object, no markdown, no extra text:
                        {
                        "plantName": "Common plant name",
                        "healthy": true or false,
                        "confidence": "98%",
                        "disease": "Disease name or null if healthy",
                        "cause": "Cause or null if healthy",
                        "symptoms": ["symptom 1", "symptom 2"] or [] if healthy,
                        "treatment": ["step 1", "step 2"] or [] if healthy,
                        "prevention": ["tip 1", "tip 2"]
                        }`
                    }
                ]
            }
        ],
        max_tokens: 800
    });
}
