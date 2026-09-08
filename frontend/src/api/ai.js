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

const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

/** Removes markdown code fences that may wrap a JSON payload. */
function stripFences(text) {
    return String(text || "").replace(/```json/gi, "").replace(/```/g, "").trim();
}

/**
 * Tries to parse a string as JSON, tolerating the trailing commas some
 * free models produce.
 */
function tryParseJSON(str) {
    if (!str) return undefined;
    try { return JSON.parse(str); } catch { /* keep going */ }
    try { return JSON.parse(str.replace(/,\s*([}\]])/g, "$1")); } catch { /* give up */ }
    return undefined;
}

/**
 * Extracts the first complete top-level { ... } object from a reply, scanning
 * bracket-by-bracket while ignoring braces inside strings. Survives prose
 * before and after the object.
 */
function extractJsonObject(text) {
    const source = stripFences(text);
    const start  = source.indexOf("{");
    if (start === -1) return undefined;
    let depth = 0, inString = false, escaped = false;
    for (let i = start; i < source.length; i++) {
        const ch = source[i];
        if (inString) {
            if (escaped) { escaped = false; }
            else if (ch === "\\") { escaped = true; }
            else if (ch === '"') { inString = false; }
            continue;
        }
        if (ch === '"') { inString = true; continue; }
        if (ch === "{") { depth++; continue; }
        if (ch === "}") {
            depth--;
            if (depth === 0) return tryParseJSON(source.slice(start, i + 1));
        }
    }
    return undefined;
}

/**
 * Extracts the first complete top-level [ ... ] array from a reply, mirroring
 * extractJsonObject but for arrays.
 */
function extractJsonArray(text) {
    const source = stripFences(text);
    const start  = source.indexOf("[");
    if (start === -1) return undefined;
    let depth = 0, inString = false, escaped = false;
    for (let i = start; i < source.length; i++) {
        const ch = source[i];
        if (inString) {
            if (escaped) { escaped = false; }
            else if (ch === "\\") { escaped = true; }
            else if (ch === '"') { inString = false; }
            continue;
        }
        if (ch === '"') { inString = true; continue; }
        if (ch === "[") { depth++; continue; }
        if (ch === "]") {
            depth--;
            if (depth === 0) return tryParseJSON(source.slice(start, i + 1));
        }
    }
    return undefined;
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
const SEARCH_STOPWORDS = new Set([
    "and", "the", "for", "with", "that", "this", "under", "over", "in", "on",
    "my", "me", "is", "of", "to", "plant", "plants", "one", "very", "really",
    "want", "need", "looking", "best", "good", "should", "has", "have"
]);

/** Indoor/outdoor classification by category id (see floranest_db.sql). */
const INDOOR_CATEGORIES = new Set([1, 3, 4, 5, 7, 8]);
const isIndoor = categoryId => INDOOR_CATEGORIES.has(Number(categoryId));
const place = p => (isIndoor(p.categoryId) ? "indoor" : "outdoor");

const DEFAULT_PRODUCT_IMAGE = "https://images.unsplash.com/photo-1446071103084-c257b5f70672?w=400";

export async function searchPlants(query, filters = {}) {
    try {
        const products = await fetchProducts();

        // 1) Best-effort AI keyword extraction — never fatal.
        let aiKeywords = [];
        try {
            const messages = [
                {
                    role:    "system",
                    content: `You are a plant search keyword extractor.
Extract 2-3 useful keywords for matching products from the user's description.
Respond with ONLY a comma-separated list of keywords, nothing else.
Example: low light, shade tolerant, indoor`
                },
                { role: "user", content: query }
            ];
            const keywordText = await callAI(messages, 60);
            aiKeywords = keywordText.split(/[,\n]/).map(k => k.trim().toLowerCase()).filter(Boolean);
        } catch (e) {
            console.warn("Smart search: keyword extraction failed, using raw query.", e);
        }

        // 2) Also match on the words the user actually typed, so search works
        //    even when the free keyword model returns nothing useful.
        const rawKeywords = String(query).toLowerCase()
            .split(/[^a-z0-9]+/)
            .map(t => t.trim())
            .filter(t => t.length > 2 && !SEARCH_STOPWORDS.has(t));
        const keywords = [...new Set([...aiKeywords, ...rawKeywords])];

        // 3) A searchable "haystack" covering every useful product field.
        const haystack = p => [
            p.productName, p.description, p.sunlight, p.watering,
            p.difficulty, p.temperature, place(p)
        ].filter(Boolean).join(" ").toLowerCase();

        return products
            .filter(p => {
                const keywordMatch = keywords.length === 0
                    || keywords.some(kw => haystack(p).includes(kw));
                const categoryMatch = !filters.category || place(p) === filters.category;
                const lightMatch = !filters.light
                    || p.sunlight?.toLowerCase().includes(filters.light.toLowerCase())
                    || p.sunlight?.toLowerCase().includes(filters.light.split(" ")[0]);
                return keywordMatch && categoryMatch && lightMatch;
            })
            .map(p => ({
                id:          p.productId,
                name:        p.productName,
                description: p.description || "A beautiful plant for your space.",
                imageUrl:    p.imageUrl    || DEFAULT_PRODUCT_IMAGE,
                careLevel:   p.difficulty  || "Easy",
                light:       p.sunlight    || "Varies",
                category:    isIndoor(p.categoryId) ? "Indoor" : "Outdoor",
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

        const products = await fetchProducts();

        // Free models sometimes truncate or ramble — retry a few times before
        // giving up, and never assume the reply is one clean JSON array.
        const MAX_ATTEMPTS = 3;
        for (let attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            let text;
            try {
                text = await callAI(messages, 1000);
            } catch (e) {
                // Retry transient empty responses; surface rate-limit/provider errors.
                const isTransient = e?.message === EMPTY_RESPONSE_MSG
                    || /empty response/i.test(e?.message || "");
                if (attempt < MAX_ATTEMPTS && isTransient) {
                    await sleep(1500);
                    continue;
                }
                throw e;
            }

            // Prefer a top-level array; also accept { "plants"/"suggestions": [...] }.
            let suggestions = extractJsonArray(text);
            if (!Array.isArray(suggestions)) {
                const object = extractJsonObject(text);
                suggestions = object?.plants || object?.suggestions;
            }

            if (Array.isArray(suggestions) && suggestions.length) {
                return suggestions.map((s, i) => {
                    const match = products.find(p =>
                        p.productName?.toLowerCase().includes(
                            String(s.name || "").toLowerCase().split(" ")[0]
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
                        imageUrl:    match?.imageUrl || DEFAULT_PRODUCT_IMAGE,
                        matchScore:  95 - (i * 5)
                    };
                });
            }

            if (attempt < MAX_ATTEMPTS) await sleep(1500);
        }

        throw new Error("The AI response did not contain a valid recommendation list.");

    } catch (e) {
        console.error("Recommendation error:", e);
        throw e;
    }
}

// ─── FEATURE 4: Plant Disease Detection ──────────────────────────────────────
// Called by AiAssistant.vue
// Accepts: an image File object from the upload component
// Returns: the raw API response (AiAssistant.vue handles parsing)
export async function analyzePlantImage(imageFile) {

    // Convert image file to base64
    const base64 = await new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload  = () => resolve(reader.result.split(",")[1]);
        reader.onerror = reject;
        reader.readAsDataURL(imageFile);
    });

    const mimeType = imageFile.type || "image/jpeg";

    // Send to backend proxy — same /ai/chat endpoint.
    // temperature 0 + a single-line schema + "only JSON" instruction give the
    // vision model the best chance of returning a parseable object.
    const data = await postChat({
        messages: [
            {
                role: "system",
                content: `You are an expert plant pathologist. You inspect plant photos and
report findings as JSON. Always answer in valid JSON only.`
            },
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
                        text: `Analyse the plant in this image and return ONLY a valid JSON object — no markdown, no code fences, no text before or after it. Use exactly this structure:
{"plantName":"common plant name","healthy":true,"confidence":"98%","disease":null,"cause":null,"symptoms":[],"treatment":[],"prevention":["tip 1","tip 2"]}
If the plant is unhealthy, set "healthy" to false and fill in "disease", "cause", "symptoms" and "treatment" with specific, useful values. If it is healthy, keep "disease"/"cause" as null and "symptoms"/"treatment" as empty arrays. Keep the reply short: at most 3 items per list, one concise phrase per item, so the object always finishes well within the response limit.`
                    }
                ]
            }
        ],
        max_tokens: 1200,
        temperature: 0
    });

    // OpenRouter can return a top-level "error" object with a 200 status
    // (e.g. moderation rejections). Surface it instead of letting the caller
    // mistake it for a malformed JSON reply.
    const providerError = data?.error;
    if (providerError) {
        const message = typeof providerError === "string"
            ? providerError
            : providerError.message || "Unknown AI provider error";
        throw new Error(`AI provider error: ${message}`);
    }

    // Same empty-content guard that callAI() applies to the text features.
    const content = data.choices?.[0]?.message?.content;
    const empty = typeof content !== "string"
        ? !Array.isArray(content) || content.length === 0
        : !content.trim();
    if (empty) {
        throw new Error(EMPTY_RESPONSE_MSG);
    }

    return data;
}
