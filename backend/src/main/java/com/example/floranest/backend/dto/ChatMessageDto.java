package com.example.floranest.backend.dto;

import lombok.Data;

@Data
public class ChatMessageDto {

    private String role;
    private String content;
    private String timestamp;

}
