package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Cart;
import com.example.floranest.backend.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findAll();
    int insert(Order order);
    List<Cart> getCartItems(Integer userId);
    void clearCart(Integer userId);
    Order findById(Integer orderId);
    void update(Order order);
    void delete(Integer id);
}