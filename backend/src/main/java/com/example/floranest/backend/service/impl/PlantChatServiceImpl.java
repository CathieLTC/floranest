package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.config.AiProperties;
import com.example.floranest.backend.dto.AiChatRequest;
import com.example.floranest.backend.dto.ChatMessageDto;
import com.example.floranest.backend.entity.Category;
import com.example.floranest.backend.entity.Product;
import com.example.floranest.backend.service.CategoryService;
import com.example.floranest.backend.service.PlantChatService;
import com.example.floranest.backend.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlantChatServiceImpl implements PlantChatService {

    private static final Logger log = LoggerFactory.getLogger(PlantChatServiceImpl.class);

    private static final String SYSTEM_PROMPT_TEMPLATE = """
            You are FloraNest AI, a friendly and knowledgeable gardening assistant for the FloraNest plant shop.

            Your role:
            - Answer ANY question about plants, gardening, indoor/outdoor care, pests, soil, watering, light, repotting, propagation, seasons, and plant troubleshooting.
            - Give practical, accurate, beginner-friendly advice unless the user is clearly advanced.
            - Be conversational and warm, like a helpful expert — not robotic.
            - If asked about non-plant topics, politely redirect to gardening and plants.
            - When relevant, suggest plants from the FloraNest catalog below.
            - Keep answers concise (2–4 short paragraphs max) unless the user asks for detail.
            - Use bullet points when listing steps or options.

            FloraNest catalog (available products):
            %s

            Always prioritize plant health and realistic care advice for home gardeners.
            """;

    private final AiProperties aiProperties;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    private volatile Boolean ollamaAvailable;

    public PlantChatServiceImpl(
            AiProperties aiProperties,
            ProductService productService,
            CategoryService categoryService,
            ObjectMapper objectMapper) {
        this.aiProperties = aiProperties;
        this.productService = productService;
        this.categoryService = categoryService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateReply(AiChatRequest request) {
        if (!aiProperties.isEnabled()) {
            throw new IllegalStateException("AI chatbot is disabled. Set floranest.ai.enabled=true in application.properties.");
        }

        ResolvedAiConfig config = resolveConfig();

        String userMessage = request.getMessage() != null ? request.getMessage().trim() : "";
        if (!StringUtils.hasText(userMessage)) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        if (useGeminiNative(config)) {
            return callGeminiNative(config, request, userMessage);
        }

        return callOpenAiCompatible(config, request, userMessage);
    }

    private String callOpenAiCompatible(ResolvedAiConfig config, AiChatRequest request, String userMessage) {
        List<Map<String, String>> messages = buildOpenAiMessages(request, userMessage);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", config.model());
        body.put("messages", messages);
        body.put("temperature", aiProperties.getTemperature());
        body.put("max_tokens", aiProperties.getMaxTokens());

        RestClient client = RestClient.builder().baseUrl(config.baseUrl()).build();

        try {
            String responseJson = client.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + config.apiKey())
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return extractOpenAiContent(responseJson);
        } catch (RestClientException ex) {
            String detail = ex.getMessage() != null ? ex.getMessage() : ex.toString();
            if (detail.contains("403") || detail.contains("Forbidden")) {
                throw new IllegalStateException(
                        "The AI provider rejected the API key (403 Forbidden). Check floranest.ai.api-key and restart the backend.",
                        ex);
            }
            throw new IllegalStateException("AI service request failed: " + detail, ex);
        }
    }

    private String callGeminiNative(ResolvedAiConfig config, AiChatRequest request, String userMessage) {
        String systemPrompt = String.format(SYSTEM_PROMPT_TEMPLATE, buildCatalogContext());
        List<Map<String, Object>> contents = buildGeminiContents(request, userMessage);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("systemInstruction", Map.of("parts", List.of(Map.of("text", systemPrompt))));
        body.put("contents", contents);
        body.put("generationConfig", Map.of(
                "temperature", aiProperties.getTemperature(),
                "maxOutputTokens", aiProperties.getMaxTokens()
        ));

        String model = config.model().startsWith("models/") ? config.model() : "models/" + config.model();
        String uri = "/" + model + ":generateContent";

        RestClient client = RestClient.builder().baseUrl(config.baseUrl()).build();

        try {
            String responseJson = client.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("x-goog-api-key", config.apiKey())
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return extractGeminiContent(responseJson);
        } catch (RestClientException ex) {
            String detail = ex.getMessage() != null ? ex.getMessage() : ex.toString();
            throw new IllegalStateException("Gemini API request failed: " + detail, ex);
        }
    }

    private boolean useGeminiNative(ResolvedAiConfig config) {
        String provider = aiProperties.getProvider() != null ? aiProperties.getProvider().trim().toLowerCase() : "auto";
        if ("gemini".equals(provider)) {
            return true;
        }
        if ("openai".equals(provider)) {
            return false;
        }
        return config.apiKey().startsWith("AQ.");
    }

    private ResolvedAiConfig resolveConfig() {
        if (isValidApiKey(aiProperties.getApiKey())) {
            return new ResolvedAiConfig(
                    aiProperties.getApiKey().trim(),
                    normalizeBaseUrl(aiProperties.getBaseUrl()),
                    aiProperties.getModel(),
                    "api-key"
            );
        }

        if (aiProperties.isAutoOllamaFallback() && checkOllamaAvailable()) {
            log.info("No AI API key configured — using local Ollama fallback (llama3.2)");
            return new ResolvedAiConfig(
                    "ollama",
                    "http://localhost:11434/v1",
                    "llama3.2",
                    "ollama"
            );
        }

        throw new IllegalStateException(buildSetupMessage());
    }

    private String buildSetupMessage() {
        return """
                AI API key is not configured. Choose ONE option:

                1) FREE Groq key (recommended):
                   - Sign up at https://console.groq.com and create an API key
                   - Open backend/src/main/resources/application-local.properties
                   - Replace PASTE_YOUR_KEY_HERE with your key (starts with gsk_)
                   - Restart the backend

                2) Environment variable:
                   - PowerShell: $env:FLORANEST_AI_API_KEY="gsk_your_key"
                   - Then restart the backend

                3) Local Ollama (no cloud key):
                   - Install from https://ollama.com
                   - Run: ollama pull llama3.2
                   - Keep Ollama running, then restart the backend
                """;
    }

    private boolean isValidApiKey(String key) {
        if (!StringUtils.hasText(key)) {
            return false;
        }
        String trimmed = key.trim();
        return !trimmed.equals("PASTE_YOUR_KEY_HERE")
                && !trimmed.startsWith("PASTE_")
                && !trimmed.equals("your-api-key-here");
    }

    private boolean checkOllamaAvailable() {
        if (ollamaAvailable != null) {
            return ollamaAvailable;
        }
        synchronized (this) {
            if (ollamaAvailable != null) {
                return ollamaAvailable;
            }
            try {
                RestClient.create()
                        .get()
                        .uri("http://localhost:11434/api/tags")
                        .retrieve()
                        .toBodilessEntity();
                ollamaAvailable = true;
            } catch (Exception ex) {
                ollamaAvailable = false;
            }
            return ollamaAvailable;
        }
    }

    private List<Map<String, String>> buildOpenAiMessages(AiChatRequest request, String userMessage) {
        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> system = new LinkedHashMap<>();
        system.put("role", "system");
        system.put("content", String.format(SYSTEM_PROMPT_TEMPLATE, buildCatalogContext()));
        messages.add(system);

        if (request.getHistory() != null) {
            List<ChatMessageDto> history = request.getHistory();
            int start = Math.max(0, history.size() - aiProperties.getMaxHistoryMessages());

            for (int i = start; i < history.size(); i++) {
                ChatMessageDto msg = history.get(i);
                if (msg == null || !StringUtils.hasText(msg.getContent()) || !StringUtils.hasText(msg.getRole())) {
                    continue;
                }
                if (!"user".equals(msg.getRole()) && !"assistant".equals(msg.getRole())) {
                    continue;
                }

                Map<String, String> entry = new LinkedHashMap<>();
                entry.put("role", msg.getRole());
                entry.put("content", msg.getContent().trim());
                messages.add(entry);
            }
        }

        boolean lastIsCurrentUser = !messages.isEmpty()
                && "user".equals(messages.get(messages.size() - 1).get("role"))
                && userMessage.equals(messages.get(messages.size() - 1).get("content"));

        if (!lastIsCurrentUser) {
            Map<String, String> user = new LinkedHashMap<>();
            user.put("role", "user");
            user.put("content", userMessage);
            messages.add(user);
        }

        return messages;
    }

    private List<Map<String, Object>> buildGeminiContents(AiChatRequest request, String userMessage) {
        List<Map<String, Object>> contents = new ArrayList<>();

        if (request.getHistory() != null) {
            List<ChatMessageDto> history = request.getHistory();
            int start = Math.max(0, history.size() - aiProperties.getMaxHistoryMessages());

            for (int i = start; i < history.size(); i++) {
                ChatMessageDto msg = history.get(i);
                if (msg == null || !StringUtils.hasText(msg.getContent()) || !StringUtils.hasText(msg.getRole())) {
                    continue;
                }

                String role = "assistant".equals(msg.getRole()) ? "model" : "user";
                if (!"user".equals(role) && !"model".equals(role)) {
                    continue;
                }

                contents.add(Map.of(
                        "role", role,
                        "parts", List.of(Map.of("text", msg.getContent().trim()))
                ));
            }
        }

        boolean lastIsCurrentUser = false;
        if (!contents.isEmpty() && "user".equals(contents.get(contents.size() - 1).get("role"))) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> parts =
                    (List<Map<String, String>>) contents.get(contents.size() - 1).get("parts");
            if (!parts.isEmpty() && userMessage.equals(parts.get(0).get("text"))) {
                lastIsCurrentUser = true;
            }
        }

        if (!lastIsCurrentUser) {
            contents.add(Map.of(
                    "role", "user",
                    "parts", List.of(Map.of("text", userMessage))
            ));
        }

        return contents;
    }

    private String buildCatalogContext() {
        Map<Integer, String> categoryNames = categoryService.getAllCategories().stream()
                .collect(Collectors.toMap(
                        Category::getCategoryId,
                        Category::getCategoryName,
                        (a, b) -> a
                ));

        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return "(No products loaded yet — give general plant care advice.)";
        }

        return products.stream()
                .limit(30)
                .map(p -> {
                    String category = p.getCategoryId() != null
                            ? categoryNames.getOrDefault(p.getCategoryId(), "General")
                            : "General";
                    return String.format(
                            "- %s (%s): %s | Light: %s | Water: %s | Difficulty: %s | Price: %s",
                            nullSafe(p.getProductName()),
                            category,
                            truncate(nullSafe(p.getDescription()), 120),
                            nullSafe(p.getSunlight()),
                            nullSafe(p.getWatering()),
                            nullSafe(p.getDifficulty()),
                            p.getPrice() != null ? "$" + p.getPrice() : "N/A"
                    );
                })
                .collect(Collectors.joining("\n"));
    }

    private String extractOpenAiContent(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || !StringUtils.hasText(content.asText())) {
                JsonNode error = root.path("error").path("message");
                if (!error.isMissingNode()) {
                    throw new IllegalStateException("AI API error: " + error.asText());
                }
                throw new IllegalStateException("AI returned an empty response.");
            }
            return content.asText().trim();
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse AI response.", ex);
        }
    }

    private String extractGeminiContent(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode error = root.path("error").path("message");
            if (!error.isMissingNode()) {
                throw new IllegalStateException("Gemini API error: " + error.asText());
            }

            JsonNode text = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (text.isMissingNode() || !StringUtils.hasText(text.asText())) {
                throw new IllegalStateException("Gemini returned an empty response.");
            }
            return text.asText().trim();
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse Gemini response.", ex);
        }
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (!StringUtils.hasText(baseUrl)) {
            return "https://generativelanguage.googleapis.com/v1beta";
        }
        String normalized = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        if (normalized.contains("generativelanguage.googleapis.com") && normalized.contains("/openai")) {
            return "https://generativelanguage.googleapis.com/v1beta";
        }
        return normalized;
    }

    private String nullSafe(String value) {
        return value != null ? value : "N/A";
    }

    private String truncate(String text, int maxLen) {
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen - 3) + "...";
    }

    private record ResolvedAiConfig(String apiKey, String baseUrl, String model, String source) {}
}
