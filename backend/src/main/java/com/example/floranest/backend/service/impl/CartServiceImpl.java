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
    public List<Cart> getCart(Integer userId) {
        return cartMapper.findByUserId(userId);
    }

    @Override
    public List<Cart> getCartByUserId(Integer userId) {
        return cartMapper.findByUserId(userId);
    }

    @Override
    public void addToCart(Cart cart) {

        Cart existing =
                cartMapper.findByUserAndProduct(
                        cart.getUserId(),
                        cart.getProductId()
                );

        if(existing != null){

            cartMapper.increaseQuantity(existing.getCartId());

        }else{

            cartMapper.insert(cart);

        }

    }

    @Override
    public void increaseQuantity(Integer cartId) {
        cartMapper.increaseQuantity(cartId);
    }

    @Override
    public void decreaseQuantity(Integer cartId) {
        cartMapper.decreaseQuantity(cartId);
    }

    @Override
    public void removeItem(Integer cartId) {
        cartMapper.delete(cartId);
    }

    @Override
    public void clearCart(Integer userId) {
        cartMapper.clearCart(userId);
    }
}