package com.example.floranest.backend.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Cart {

    private Integer cartId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String productName;
    private String categoryName;
    private BigDecimal price;
    private String imageUrl;

}