package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Order;
import com.example.floranest.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class  OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/checkout/{userId}")
    public Order checkout(@PathVariable Integer userId) {
        return orderService.checkout(userId);
    }

}