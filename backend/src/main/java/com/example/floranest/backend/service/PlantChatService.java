package com.example.floranest.backend.service;

import com.example.floranest.backend.dto.AiChatRequest;

public interface PlantChatService {

    String generateReply(AiChatRequest request);

}
