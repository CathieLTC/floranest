package com.example.floranest.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {

    private Integer categoryId;
    private String categoryName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}