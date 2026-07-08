package com.example.floranest.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiChatRequest {

    private String message;
    private List<ChatMessageDto> history;

}
