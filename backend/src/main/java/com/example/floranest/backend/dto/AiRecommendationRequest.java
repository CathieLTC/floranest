package com.example.floranest.backend.dto;

import lombok.Data;

@Data
public class AiRecommendationRequest {

    private String location;
    private String experience;
    private String space;
    private String light;
    private String purpose;

}
