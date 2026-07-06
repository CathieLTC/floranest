package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findByUserId(Integer userId);
    Cart findByUserAndProduct(Integer userId, Integer productId);
    int insert(Cart cart);
    int updateQuantity(Cart cart);
    int delete(Integer cartId);
    int clearCart(Integer userId);
    int increaseQuantity(Integer cartId);
    int decreaseQuantity(Integer cartId);

}