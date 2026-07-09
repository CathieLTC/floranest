package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
    Order getOrderById(Integer id);
    List<Order> getOrderByUserId(Integer userId);
    void addOrder(Order order);
    void updateOrder(Order order);
    void deleteOrder(Integer id);
    Order checkout(Integer userId, String address);
    void cancelOrder(Integer orderId);
    void updateStatus(
            Integer orderId,
            Order.OrderStatus status
    );
}