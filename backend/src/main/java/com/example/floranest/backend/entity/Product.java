package com.example.floranest.backend.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {

    private Integer productId;

    private Integer categoryId;

    private String productName;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private String status;

    private String sunlight;

    private String watering;

    private String difficulty;

    private String temperature;

    private String videoUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}