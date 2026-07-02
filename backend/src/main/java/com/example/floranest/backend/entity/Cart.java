package com.example.floranest.backend.entity;

import lombok.Data;

@Data
public class Cart {

    private Integer cartId;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

}