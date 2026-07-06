// import api from "./axios";

/**
 * AI service layer — swap mock implementations for real API calls when backend is ready.
 *
 * Example integration:
 *   return (await api.post("/api/ai/search", { query, filters })).data;
 */

const MOCK_PLANTS = [
  {
    id: 1,
    name: "Monstera Deliciosa",
    imageUrl: "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=400",
    description: "A striking tropical plant with iconic split leaves, perfect for bright indoor spaces.",
    careLevel: "Easy",
    light: "Bright indirect",
    category: "Indoor",
    matchScore: 95,
  },
  {
    id: 2,
    name: "Snake Plant",
    imageUrl: "https://images.unsplash.com/photo-1593482892228-4c0f9632e057?w=400",
    description: "Nearly indestructible and excellent at purifying indoor air with minimal care.",
    careLevel: "Easy",
    light: "Low to bright",
    category: "Indoor",
    matchScore: 92,
  },
  {
    id: 3,
    name: "Lavender",
    imageUrl: "https://images.unsplash.com/photo-1499002238440-d264edd596ec?w=400",
    description: "Fragrant outdoor herb that thrives in sunny gardens and attracts pollinators.",
    careLevel: "Moderate",
    light: "Full sun",
    category: "Outdoor",
    matchScore: 88,
  },
  {
    id: 4,
    name: "Peace Lily",
    imageUrl: "https://images.unsplash.com/photo-1593691509543-c55fb32d8caa?w=400",
    description: "Elegant white blooms and lush foliage; ideal for low-light corners.",
    careLevel: "Easy",
    light: "Low to medium",
    category: "Indoor",
    matchScore: 90,
  },
  {
    id: 5,
    name: "Rosemary",
    imageUrl: "https://images.unsplash.com/photo-1628556270448-4d4e845c1a0e?w=400",
    description: "Aromatic culinary herb that loves sunny balconies and herb gardens.",
    careLevel: "Moderate",
    light: "Full sun",
    category: "Outdoor",
    matchScore: 85,
  },
  {
    id: 6,
    name: "Pothos",
    imageUrl: "https://images.unsplash.com/photo-1614594895304-fe7116ac3cc8?w=400",
    description: "Fast-growing trailing vine, perfect for shelves and hanging baskets.",
    careLevel: "Easy",
    light: "Low to bright",
    category: "Indoor",
    matchScore: 94,
  },
];

const MOCK_CHAT_RESPONSES = [
  "Water most houseplants when the top inch of soil feels dry. Overwatering is a common cause of yellow leaves.",
  "For healthy growth, rotate your plant every few weeks so all sides receive equal light.",
  "Yellow leaves can mean overwatering, underwatering, or too much direct sun — check soil moisture first.",
  "Most tropical plants prefer humidity above 50%. A pebble tray or room humidifier can help indoors.",
  "Repot in spring when roots circle the pot. Use fresh potting mix and a container one size larger.",
];

function delay(ms = 600) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function filterPlants(plants, query, filters = {}) {
  let results = [...plants];

  if (query?.trim()) {
    const term = query.trim().toLowerCase();
    results = results.filter(
      (p) =>
        p.name.toLowerCase().includes(term) ||
        p.description.toLowerCase().includes(term) ||
        p.category.toLowerCase().includes(term),
    );
  }

  if (filters.category) {
    results = results.filter((p) => p.category === filters.category);
  }

  if (filters.careLevel) {
    results = results.filter((p) => p.careLevel === filters.careLevel);
  }

  if (filters.light) {
    results = results.filter((p) =>
      p.light.toLowerCase().includes(filters.light.toLowerCase()),
    );
  }

  return results;
}

export async function searchPlants(query, filters = {}) {
  // return (await api.post("/api/ai/search", { query, filters })).data;
  await delay();
  return filterPlants(MOCK_PLANTS, query, filters);
}

export async function getPlantRecommendations(preferences = {}) {
  // return (await api.post("/api/ai/recommendations", preferences)).data;
  await delay(800);

  let results = filterPlants(MOCK_PLANTS, "", {
    category: preferences.location === "outdoor" ? "Outdoor" : preferences.location === "indoor" ? "Indoor" : undefined,
    careLevel: preferences.experience === "beginner" ? "Easy" : undefined,
  });

  if (preferences.purpose === "air-purifying") {
    results = results.filter((p) => ["Snake Plant", "Peace Lily", "Pothos"].includes(p.name));
  }

  if (preferences.space === "small") {
    results = results.filter((p) => ["Snake Plant", "Pothos", "Peace Lily"].includes(p.name));
  }

  if (results.length === 0) {
    results = MOCK_PLANTS.slice(0, 3);
  }

  return results
    .map((plant) => ({
      ...plant,
      matchScore: Math.min(99, plant.matchScore + Math.floor(Math.random() * 5)),
      reason: buildRecommendationReason(plant, preferences),
    }))
    .sort((a, b) => b.matchScore - a.matchScore)
    .slice(0, 4);
}

export async function sendChatMessage(message, history = []) {
  // return (await api.post("/api/ai/chat", { message, history })).data;
  await delay(900);

  const lower = message.toLowerCase();
  let reply;

  if (lower.includes("water") || lower.includes("watering")) {
    reply =
      "Check soil moisture before watering. Most houseplants prefer the top 1–2 inches to dry out between waterings. Reduce frequency in winter when growth slows.";
  } else if (lower.includes("sun") || lower.includes("light")) {
    reply =
      "Light needs vary by species. Bright indirect light suits most tropical plants. Direct afternoon sun can scorch leaves — move the plant or use a sheer curtain.";
  } else if (lower.includes("yellow")) {
    reply =
      "Yellow leaves often signal stress: overwatering, underwatering, or nutrient issues. Inspect roots, adjust watering, and ensure drainage holes aren't blocked.";
  } else if (lower.includes("fertiliz") || lower.includes("feed")) {
    reply =
      "Feed during active growth (spring and summer) with a balanced liquid fertilizer diluted to half strength. Skip feeding in fall and winter.";
  } else {
    reply =
      MOCK_CHAT_RESPONSES[Math.floor(Math.random() * MOCK_CHAT_RESPONSES.length)];
  }

  return {
    role: "assistant",
    content: reply,
    timestamp: new Date().toISOString(),
  };
}

function buildRecommendationReason(plant, preferences) {
  const parts = [];

  if (preferences.experience === "beginner" && plant.careLevel === "Easy") {
    parts.push("low-maintenance and beginner-friendly");
  }

  if (preferences.location === "indoor" && plant.category === "Indoor") {
    parts.push("well-suited for indoor environments");
  }

  if (preferences.location === "outdoor" && plant.category === "Outdoor") {
    parts.push("ideal for outdoor gardens");
  }

  if (preferences.space === "small") {
    parts.push("compact enough for small spaces");
  }

  if (parts.length === 0) {
    parts.push("a great match for your preferences");
  }

  return `Recommended because it is ${parts.join(" and ")}.`;
}
