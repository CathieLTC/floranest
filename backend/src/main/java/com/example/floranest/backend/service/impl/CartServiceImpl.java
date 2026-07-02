package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.Cart;
import com.example.floranest.backend.mapper.CartMapper;
import com.example.floranest.backend.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    public CartServiceImpl(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public List<Cart> getAllCartItems() {
        return cartMapper.findAll();
    }

    @Override
    public Cart getCartItemById(Integer id) {
        return cartMapper.findById(id);
    }

    @Override
    public void addCartItem(Cart cart) {
        cartMapper.insert(cart);
    }

    @Override
    public void updateCartItem(Cart cart) {
        cartMapper.update(cart);
    }

    @Override
    public void deleteCartItem(Integer id) {
        cartMapper.delete(id);
    }

}