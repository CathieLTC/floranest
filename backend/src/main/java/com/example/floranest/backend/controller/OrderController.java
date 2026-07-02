package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Order;
import com.example.floranest.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public String addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return "Order created successfully.";
    }

    @PutMapping("/{id}")
    public String updateOrder(@PathVariable Integer id,
                              @RequestBody Order order) {
        order.setOrderId(id);
        orderService.updateOrder(order);
        return "Order updated successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "Order deleted successfully.";
    }

}