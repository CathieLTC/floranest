package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Cart;
import com.example.floranest.backend.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findAll();
    Order findById(Integer orderId);
    List<Order> findByUserId(Integer userId);   // for order history page
    int insert(Order order);
    void update(Order order);
    void delete(Integer id);
    List<Cart> getCartItems(Integer userId);
    void clearCart(Integer userId);

}