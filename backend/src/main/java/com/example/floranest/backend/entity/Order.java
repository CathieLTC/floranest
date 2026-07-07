package com.example.floranest.backend.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {

    private Integer orderId;
    private Integer userId;
    private BigDecimal totalAmount;
    private String orderStatus;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    private String orderNumber;
    private String address;
}