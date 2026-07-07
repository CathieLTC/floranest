package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getCart(Integer userId);
    void addToCart(Cart cart);
    void removeItem(Integer cartId);
    void clearCart(Integer userId);
    void increaseQuantity(Integer cartId);
    void decreaseQuantity(Integer cartId);
    List<Cart> getCartByUserId(Integer userId);
}