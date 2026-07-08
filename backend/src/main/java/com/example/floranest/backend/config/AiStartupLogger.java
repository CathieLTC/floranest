package com.example.floranest.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AiStartupLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AiStartupLogger.class);

    private final AiProperties aiProperties;

    public AiStartupLogger(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!aiProperties.isEnabled()) {
            log.warn("FloraNest AI chatbot is DISABLED (floranest.ai.enabled=false)");
            return;
        }

        String key = aiProperties.getApiKey();
        if (StringUtils.hasText(key) && !key.equals("PASTE_YOUR_KEY_HERE") && !key.startsWith("PASTE_")) {
            log.info("FloraNest AI chatbot ready — provider: {} | model: {}", aiProperties.getBaseUrl(), aiProperties.getModel());
            return;
        }

        if (aiProperties.isAutoOllamaFallback()) {
            log.warn("""
                    FloraNest AI: no API key set. Chat will use local Ollama if installed, otherwise configure a key in:
                    backend/src/main/resources/application-local.properties
                    Get a FREE Groq key at https://console.groq.com
                    """);
            return;
        }

        log.warn("FloraNest AI: API key not configured. Gardening Chat will not work until you set floranest.ai.api-key.");
    }
}
