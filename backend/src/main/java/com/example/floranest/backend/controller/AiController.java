package com.example.floranest.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
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

    @PostMapping("/analyzePlant")
    public ResponseEntity<?> analyzePlant(
            @RequestParam("image") MultipartFile image
    ) {

        try {

            String base64Image =
                    Base64.getEncoder().encodeToString(image.getBytes());

            String prompt = """
                        You are an expert botanist.
                        
                        Analyze the uploaded plant image.
                        
                        Identify:
                        
                        1. Plant species
                        2. Whether it is healthy
                        3. Disease name (if present)
                        4. Confidence percentage
                        5. Cause
                        6. Symptoms
                        7. Treatment
                        8. Prevention
                        
                        Respond ONLY in valid JSON.
                        
                        Do not use markdown.
                        
                        Example:
                        
                        {
                          "plantName":"",
                          "healthy":true,
                          "disease":"",
                          "confidence":"95%",
                          "cause":"",
                          "symptoms":[],
                          "treatment":[],
                          "prevention":[]
                        }
                        """;

            Map<String,Object> imageUrl = new HashMap<>();
            imageUrl.put(
                    "url",
                    "data:image/jpeg;base64," + base64Image
            );

            Map<String,Object> imageContent = new HashMap<>();
            imageContent.put("type","image_url");
            imageContent.put("image_url",imageUrl);

            Map<String,Object> textContent = new HashMap<>();
            textContent.put("type","text");
            textContent.put("text",prompt);

            Map<String,Object> userMessage = new HashMap<>();
            userMessage.put("role","user");
            userMessage.put(
                    "content",
                    List.of(
                            textContent,
                            imageContent
                    )
            );

            Map<String,Object> requestBody = new HashMap<>();

            requestBody.put("model",model);
            requestBody.put("messages",List.of(userMessage));
            requestBody.put("temperature",0.2);
            requestBody.put("max_tokens",1000);

            return ResponseEntity.ok(callAiApi(requestBody));

        }

        catch (HttpClientErrorException e) {

            return buildErrorResponse(e);

        }

        catch(IOException e){

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );

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
