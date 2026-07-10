/**
 * FloraNest AI Service
 * All AI calls go through our Spring Boot backend at /ai/chat
 * The backend holds the API key — it never touches the frontend.
 */

const BACKEND_URL = "http://localhost:8080/ai/chat";

const SYSTEM_PROMPT = `You are FloraNest AI, a friendly and knowledgeable gardening
assistant for an online plant shop. You help users with:
- Plant care advice (watering, light, soil, fertilizing, repotting)
- Diagnosing plant problems (yellow leaves, pests, root rot)
- Recommending plants based on lifestyle and space
- Seasonal gardening tips

Keep responses concise (3-5 sentences), warm, and practical.
If a question is unrelated to plants or gardening, politely redirect
the user back to plant topics.`;

// ─── Core caller — sends to our backend, not AI directly ─────────────────────
async function callAI(messages, maxTokens = 500) {
    const response = await fetch(BACKEND_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            messages:   messages,
            max_tokens: maxTokens,
            temperature: 0.7
        })
    });

    if (!response.ok) {
        const err = await response.json();
        throw new Error(err.error?.message || `Backend error ${response.status}`);
    }

    const data = await response.json();
    return data.choices[0].message.content;
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

        const res      = await fetch("http://localhost:8080/products");
        const products = await res.json();

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

        // Try to match against real backend products
        let products = [];
        try {
            const res = await fetch("http://localhost:8080/products");
            products  = await res.json();
        } catch { /* backend unavailable, use AI data only */ }

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