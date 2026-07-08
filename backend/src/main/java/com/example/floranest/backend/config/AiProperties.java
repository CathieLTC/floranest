package com.example.floranest.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "floranest.ai")
public class AiProperties {

    /** Enable LLM-powered chatbot. When false or key missing, chat returns a setup hint. */
    private boolean enabled = true;

    /** API key — set via floranest.ai.api-key or FLORANEST_AI_API_KEY env var. */
    private String apiKey = "";

    /** OpenAI-compatible base URL (OpenAI, Groq, OpenRouter, Ollama, etc.). */
    private String baseUrl = "https://api.openai.com/v1";

    /** Model name, e.g. gpt-4o-mini, llama-3.3-70b-versatile (Groq). */
    private String model = "gpt-4o-mini";

    private int maxTokens = 800;

    private double temperature = 0.7;

    /** Max prior messages sent as conversation context. */
    private int maxHistoryMessages = 12;

    /** When true and no API key is set, try local Ollama at localhost:11434. */
    private boolean autoOllamaFallback = true;

    /** Provider: openai (OpenAI-compatible), gemini (native Google API), auto (detect from key). */
    private String provider = "auto";

}
