package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.Order;
import com.example.floranest.backend.mapper.OrderMapper;
import com.example.floranest.backend.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.findAll();
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderMapper.findById(id);
    }

    @Override
    public void addOrder(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public void updateOrder(Order order) {
        orderMapper.update(order);
    }

    @Override
    public void deleteOrder(Integer id) {
        orderMapper.delete(id);
    }

}