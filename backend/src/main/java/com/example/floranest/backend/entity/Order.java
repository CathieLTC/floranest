package com.example.floranest.backend.entity;

import lombok.Data;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {

    public enum OrderStatus {

        PENDING,
        PAID,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED

    }

    private OrderStatus orderStatus;
    private Integer orderId;
    private Integer userId;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    private String orderNumber;
    private String address;
    private List<OrderItem> items;
}