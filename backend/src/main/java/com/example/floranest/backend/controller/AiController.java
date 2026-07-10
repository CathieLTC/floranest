package com.example.floranest.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {

    @Value("${ai.api.key:NOT_FOUND}")
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
    public ResponseEntity<Map> chat(@RequestBody Map<String, Object> body) {
        try {
            // Inject the correct model into the request body
            body.put("model", model);

            // Forward to AI API with auth header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    apiUrl, request, Map.class
            );

            return ResponseEntity.ok(response.getBody());

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

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String,Object>> request =
                    new HttpEntity<>(requestBody,headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            apiUrl,
                            request,
                            Map.class
                    );

            return ResponseEntity.ok(response.getBody());

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
}