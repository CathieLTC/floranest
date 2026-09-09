package com.example.floranest.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {
    private Integer reviewId;
    private Integer userId;
    private String reviewType;
    private String targetName;
    private String authorName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
