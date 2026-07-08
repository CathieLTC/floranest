package com.example.floranest.backend.controller;

import com.example.floranest.backend.dto.*;
import com.example.floranest.backend.service.AiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/search")
    public List<PlantRecommendationDto> search(@RequestBody AiSearchRequest request) {
        return aiService.searchPlants(request);
    }

    @PostMapping("/recommendations")
    public List<PlantRecommendationDto> recommendations(@RequestBody AiRecommendationRequest request) {
        return aiService.getRecommendations(request);
    }

    @PostMapping("/chat")
    public ChatMessageDto chat(@RequestBody AiChatRequest request) {
        return aiService.chat(request);
    }

}
