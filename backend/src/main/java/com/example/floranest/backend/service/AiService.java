package com.example.floranest.backend.service;

import com.example.floranest.backend.dto.*;

import java.util.List;

public interface AiService {

    List<PlantRecommendationDto> searchPlants(AiSearchRequest request);

    List<PlantRecommendationDto> getRecommendations(AiRecommendationRequest request);

    ChatMessageDto chat(AiChatRequest request);

}
