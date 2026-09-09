package com.example.floranest.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BASE_BACKOFF_MS = 2_000L;

    @Value("${ai.api.key:NOT_FOUND}")
    private String apiKey;

    @Value("${ai.api.url:}")
    private String apiUrl;

    @Value("${ai.model:}")
    private String model;

    @Value("${ai.vision.model:}")
    private String visionModel;

    @Value("${ai.vision.model.fallback:}")
    private String visionModelFallback;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Proxy endpoint — receives messages from Vue frontend,
     * forwards to AI provider, returns the response.
     * Keeps the API key hidden on the server side.
     */
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, Object> body) {
        try {
            if (requiresVision(body)) {
                // Try the pinned vision model first, then any configured fallbacks,
                // because OpenRouter's free catalogue rotates and models can vanish.
                List<String> candidates = visionCandidates();
                body.put("model", candidates.isEmpty() ? model : candidates.get(0));
                return ResponseEntity.ok(callAiApi(body, candidates));
            }
            // Ordinary text chat keeps the default text model.
            body.put("model", model);
            return ResponseEntity.ok(callAiApi(body, List.of(model)));
        } catch (HttpClientErrorException e) {
            return buildErrorResponse(e);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", Map.of("message", e.getMessage())));
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Ordered vision-model candidates: the pinned model first, followed by any
     * configured fallbacks. Keeping alternatives lets the request survive a
     * model being pulled from the free catalogue.
     */
    private List<String> visionCandidates() {
        List<String> candidates = new ArrayList<>();
        if (visionModel != null && !visionModel.isBlank()) {
            candidates.add(visionModel.trim());
        }
        if (visionModelFallback != null && !visionModelFallback.isBlank()) {
            for (String value : visionModelFallback.split(",")) {
                String trimmed = value.trim();
                if (!trimmed.isEmpty() && !candidates.contains(trimmed)) {
                    candidates.add(trimmed);
                }
            }
        }
        return candidates;
    }

    /** True when any message in the payload has image content (multimodal input). */
    private boolean requiresVision(Map<String, Object> body) {
        Object messages = body.get("messages");
        if (!(messages instanceof List<?> messageList)) {
            return false;
        }
        for (Object message : messageList) {
            if (!(message instanceof Map<?, ?> map)) {
                continue;
            }
            Object content = map.get("content");
            if (content instanceof List<?> parts) {
                for (Object part : parts) {
                    if (part instanceof Map<?, ?> partMap
                            && "image_url".equals(partMap.get("type"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** True when the upstream error means the requested model no longer exists. */
    private boolean isModelNotFound(HttpClientErrorException e) {
        int status = e.getStatusCode().value();
        if (status == 404) return true;
        if (status != 400) return false;
        String errorBody = e.getResponseBodyAsString();
        if (errorBody == null) return false;
        String lower = errorBody.toLowerCase();
        return lower.contains("not found")
                || lower.contains("does not support")
                || lower.contains("does not exist")
                || lower.contains("invalid");
    }

    /**
     * Forwards a request to the AI provider.
     * Transient failures (429 rate limit, 5xx) are retried with backoff,
     * honouring the upstream Retry-After header when provided. If the selected
     * model disappears from the catalogue (404/400), we fall back to the next
     * configured candidate before giving up.
     */
    private Map<String, Object> callAiApi(Map<String, Object> body, List<String> modelCandidates) {
        List<String> candidates = (modelCandidates == null || modelCandidates.isEmpty())
                ? List.of(String.valueOf(body.get("model")))
                : modelCandidates;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.set("HTTP-Referer", "https://floranest.com");
        headers.set("X-Title", "FloraNest");

        int modelIndex = 0;
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                body.put("model", candidates.get(modelIndex));
                HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
                ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<Map<String, Object>>() {});
                Map<String, Object> responseBody = response.getBody();
                if (responseBody == null) {
                    throw new IllegalStateException(
                            "AI provider returned an empty response body"
                    );
                }
                return responseBody;
            } catch (HttpClientErrorException.TooManyRequests e) {
                if (attempt >= MAX_ATTEMPTS) {
                    throw e;
                }
                sleepSafely(retryAfterMillis(e));
            } catch (HttpServerErrorException e) {
                if (attempt >= MAX_ATTEMPTS) {
                    throw e;
                }
                // Exponential backoff: 2s, 4s
                sleepSafely(BASE_BACKOFF_MS * (1L << (attempt - 1)));
            } catch (HttpClientErrorException e) {
                // Model may have been removed from the free catalogue — next candidate.
                if (isModelNotFound(e) && modelIndex < candidates.size() - 1) {
                    modelIndex++;
                    attempt = 0;
                    continue;
                }
                throw e;
            }
        }
    }

    /**
     * Maps an upstream 4xx error to a JSON error response
     * that keeps the same HTTP status (429 stays 429).
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpClientErrorException e) {
        String friendly;
        if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            String body = e.getResponseBodyAsString();
            if (body != null && body.toLowerCase().contains("free-models-per-day")) {
                friendly = "The free AI daily limit (50 requests) has been reached. "
                        + "It resets at 8:00 AM. For unlimited access, add credits to your OpenRouter account.";
            } else if (body != null && body.toLowerCase().contains("high demand")) {
                friendly = "The AI service is experiencing high demand right now. Please try again in a few minutes.";
            } else {
                friendly = "The AI service is rate limited. Please wait a minute and try again.";
            }
        } else {
            friendly = "AI request was rejected: " + e.getStatusCode().value();
        }
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Map.<String, Object>of("error", Map.of("message", friendly)));
    }

    /** Reads the Retry-After header when present; falls back to a default delay. */
    private long retryAfterMillis(HttpClientErrorException e) {
        HttpHeaders responseHeaders = e.getResponseHeaders();
        List<String> values = responseHeaders == null
                ? List.of()
                : responseHeaders.getValuesAsList(HttpHeaders.RETRY_AFTER);
        if (!values.isEmpty()) {
            try {
                return Long.parseLong(values.get(0)) * 1000L;
            } catch (NumberFormatException ignored) {
                // fall through to default
            }
        }
        return BASE_BACKOFF_MS;
    }

    private void sleepSafely(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
