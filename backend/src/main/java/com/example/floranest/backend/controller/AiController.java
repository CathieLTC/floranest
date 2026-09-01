package com.example.floranest.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BASE_BACKOFF_MS = 2_000L;

    @Value("${ai.api.key:NOT_FOUND}")
    @NonNull
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Proxy endpoint — receives messages from Vue frontend,
     * forwards to AI provider, returns the response.
     * Keeps the API key hidden on the server side.
     */
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, Object> body) {
        try {
            // Inject the correct model into the request body
            body.put("model", model);
            return ResponseEntity.ok(callAiApi(body));
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
     * Forwards a request to the AI provider.
     * Transient failures (429 rate limit, 5xx) are retried with backoff,
     * honouring the upstream Retry-After header when provided.
     */
    private Map<String, Object> callAiApi(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        int attempt = 0;
        while (true) {
            attempt++;
            try {
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
            }
        }
    }

    /**
     * Maps an upstream 4xx error to a JSON error response
     * that keeps the same HTTP status (429 stays 429).
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpClientErrorException e) {
        String friendly = e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS
                ? "AI service is receiving too many requests. Please wait a moment and try again."
                : "AI request was rejected: " + e.getStatusCode().value();
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
