package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getAllCartItems();

    Cart getCartItemById(Integer id);

    void addCartItem(Cart cart);

    void updateCartItem(Cart cart);

    void deleteCartItem(Integer id);

}