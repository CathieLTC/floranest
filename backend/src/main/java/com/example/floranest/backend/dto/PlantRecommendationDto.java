package com.example.floranest.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlantRecommendationDto {

    private Integer id;
    private String name;
    private String imageUrl;
    private String description;
    private String careLevel;
    private String light;
    private String category;
    private Integer matchScore;
    private String reason;
    private BigDecimal price;

}
