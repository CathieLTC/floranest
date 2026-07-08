import api from "./axios";

/**
 * FloraNest AI API client — calls backend endpoints at /api/ai/*
 */

export async function searchPlants(query, filters = {}) {
  const { data } = await api.post("/api/ai/search", { query, filters });
  return data;
}

export async function getPlantRecommendations(preferences = {}) {
  const { data } = await api.post("/api/ai/recommendations", preferences);
  return data;
}

export async function sendChatMessage(message, history = []) {
  const { data } = await api.post("/api/ai/chat", { message, history });
  return data;
}
