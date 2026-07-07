package com.example.floranest.backend.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {

    private Integer orderItemId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private String productName;
    private String imageUrl;
}