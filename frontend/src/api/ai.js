/**
 * FloraNest AI service — powered by OpenRouter
 * Docs: https://openrouter.ai/docs
 *
 * The free model used here: meta-llama/llama-3.1-8b-instruct:free
 * You can swap it for any model listed at openrouter.ai/models
 */

const OPENROUTER_URL  = "https://openrouter.ai/api/v1/chat/completions";
const OPENROUTER_KEY  = import.meta.env.VITE_OPENROUTER_API_KEY;
const MODEL = "meta-llama/llama-3.3-70b-instruct:free";


// System prompt — tells the AI who it is and what it should focus on
const SYSTEM_PROMPT = `You are FloraNest AI, a friendly and knowledgeable gardening assistant 
for an online plant shop. You help users with:
- Plant care advice (watering, light, soil, fertilizing, repotting)
- Diagnosing plant problems (yellow leaves, pests, root rot)
- Recommending plants based on lifestyle and space
- Seasonal gardening tips

Keep responses concise (3-5 sentences), friendly, and practical. 
If a question is completely unrelated to plants or gardening, 
politely redirect the user back to plant topics.`;

/**
 * Core function — sends a message to OpenRouter and returns the AI reply.
 * Called by GardeningChat.vue.
 *
 * @param {string}  message  - The user's latest message
 * @param {Array}   history  - Full conversation history (role + content objects)
 * @returns {Object}         - Message object { role, content, timestamp }
 */
export async function sendChatMessage(message, history = []) {

    // Build the messages array OpenRouter expects:
    // [system prompt] + [conversation history] + [new user message]
    const messages = [
        { role: "system", content: SYSTEM_PROMPT },
        // Include history but skip the first greeting message (it's from the UI, not a real exchange)
        ...history.slice(1).map(m => ({
            role:    m.role,
            content: m.content
        })),
        { role: "user", content: message }
    ];

    const response = await fetch(OPENROUTER_URL, {
        method:  "POST",
        headers: {
            "Content-Type":  "application/json",
            "Authorization": `Bearer ${OPENROUTER_KEY}`,
            "HTTP-Referer":  "http://localhost:5173",   // your local dev URL
            "X-Title":       "FloraNest"
        },
        body: JSON.stringify({
            model:       MODEL,
            messages:    messages,
            max_tokens:  500,
            temperature: 0.7    // 0 = very factual, 1 = more creative
        })
    });

    if (!response.ok) {
      const error = await response.json();
      console.error("OpenRouter error code:", error.error?.code);
      console.error("OpenRouter error message:", error.error?.message);
      throw new Error(error.error?.message || "OpenRouter request failed");
  
    }

    const data    = await response.json();
    const content = data.choices[0].message.content;

    return {
        role:      "assistant",
        content:   content,
        timestamp: new Date().toISOString()
    };
}


// ─── Smart Search ─────────────────────────────────────────────────────────────
// Uses AI to interpret a natural language search query,
// then filters your real product database.

export async function searchPlants(query, filters = {}) {

    const messages = [
        {
            role: "system",
            content: `You are a plant search assistant. The user will describe what they're 
looking for. Extract 2-3 relevant keywords from their description that could match 
plant names or descriptions in a database. Respond with ONLY a comma-separated list 
of keywords, nothing else. Example: "monstera, tropical, large leaves"`
        },
        { role: "user", content: query }
    ];

    try {
        const response = await fetch(OPENROUTER_URL, {
            method:  "POST",
            headers: {
                "Content-Type":  "application/json",
                "Authorization": `Bearer ${OPENROUTER_KEY}`,
                "HTTP-Referer":  "http://localhost:5173",
                "X-Title":       "FloraNest"
            },
            body: JSON.stringify({ model: MODEL, messages, max_tokens: 50 })
        });

        const data     = await response.json();
        const keywords = data.choices[0].message.content
            .split(",")
            .map(k => k.trim().toLowerCase());

        // Now fetch real products and filter by AI-extracted keywords
        const productsRes = await fetch("http://localhost:8080/products");
        const products    = await productsRes.json();

        return products.filter(p =>
            keywords.some(kw =>
                p.productName?.toLowerCase().includes(kw) ||
                p.description?.toLowerCase().includes(kw)
            )
        );

    } catch (e) {
        console.error("Smart search error:", e);
        return [];
    }
}


// ─── Plant Recommendations ────────────────────────────────────────────────────
// Sends user preferences to the AI and returns structured recommendations.

export async function getPlantRecommendations(preferences = {}) {

    const prefText = Object.entries(preferences)
        .map(([k, v]) => `${k}: ${v}`)
        .join(", ");

    const messages = [
        {
            role: "system",
            content: `You are a plant recommendation assistant. Given user preferences, 
suggest 3 specific plant names that would suit them. Respond with ONLY a JSON array 
of objects with this exact format, no other text:
[{"name":"Plant Name","reason":"One sentence why it fits","careLevel":"Easy/Moderate/Hard"}]`
        },
        { role: "user", content: `My preferences: ${prefText}` }
    ];

    try {
        const response = await fetch(OPENROUTER_URL, {
            method:  "POST",
            headers: {
                "Content-Type":  "application/json",
                "Authorization": `Bearer ${OPENROUTER_KEY}`,
                "HTTP-Referer":  "http://localhost:5173",
                "X-Title":       "FloraNest"
            },
            body: JSON.stringify({ model: MODEL, messages, max_tokens: 300 })
        });

        const data = await response.json();
        const text = data.choices[0].message.content
            .replace(/```json|```/g, "")
            .trim();

        return JSON.parse(text);

    } catch (e) {
        console.error("Recommendation error:", e);
        return [];
    }
}