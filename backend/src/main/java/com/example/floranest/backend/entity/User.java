package com.example.floranest.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String role;
    private String city;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}