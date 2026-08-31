package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Order;
import com.example.floranest.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // All orders (admin use)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Order history for a specific user — used by MyOrders / OrderHistory pages
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Integer userId) {
        return orderService.getOrderByUserId(userId);
    }

    @PutMapping("/cancel/{orderId}")
    public void cancelOrder(
            @PathVariable Integer orderId){

        orderService.cancelOrder(orderId);

    }

    @PutMapping("/status/{orderId}")
    public void updateStatus(
            @PathVariable Integer orderId,
            @RequestBody Map<String,String> body){

        Order.OrderStatus status =
                Order.OrderStatus.valueOf(body.get("status"));

        orderService.updateStatus(orderId, status);

    }

    // Checkout — receives address in request body alongside userId in path
    @PostMapping("/checkout/{userId}")
    public Order checkout(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> body) {

        String address = body.getOrDefault("address", "");
        return orderService.checkout(userId, address);
    }
}