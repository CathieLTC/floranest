package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Integer id);

    void addOrder(Order order);

    void updateOrder(Order order);

    void deleteOrder(Integer id);

}