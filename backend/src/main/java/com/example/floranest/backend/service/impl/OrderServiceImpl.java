package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.Cart;
import com.example.floranest.backend.entity.Order;
import com.example.floranest.backend.entity.OrderItem;
import com.example.floranest.backend.mapper.OrderItemMapper;
import com.example.floranest.backend.mapper.OrderMapper;

import com.example.floranest.backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper) {

        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.findAll();
    }

    @Override
    public Order getOrderById(Integer id) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        return order;
    }

    @Override
    @Transactional
    public void addOrder(Order order) {
        // Generate order number if not provided
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            String orderNumber = "FN"
                    + LocalDate.now().toString().replace("-", "")
                    + System.currentTimeMillis();
            order.setOrderNumber(orderNumber);
        }

        // Set default status if not provided
        if (order.getOrderStatus() == null || order.getOrderStatus().isEmpty()) {
            order.setOrderStatus("PENDING");
        }

        orderMapper.insert(order);
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        // Check if order exists
        Order existingOrder = orderMapper.findById(order.getOrderId());
        if (existingOrder == null) {
            throw new RuntimeException("Order not found with id: " + order.getOrderId());
        }

        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id) {
        // Check if order exists
        Order existingOrder = orderMapper.findById(id);
        if (existingOrder == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        // First delete all order items associated with this order
        orderItemMapper.deleteByOrderId(id);

        // Then delete the order
        orderMapper.delete(id);
    }

    @Override
    @Transactional
    public Order checkout(Integer userId) {
        // 1. Get all cart items
        List<Cart> cartItems = orderMapper.getCartItems(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }
        // 2. Calculate total amount
        BigDecimal total = BigDecimal.ZERO;

        for (Cart cart : cartItems) {
            BigDecimal subTotal = cart.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            total = total.add(subTotal);
        }
        // 3. Create order
        Order order = new Order();

        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setOrderStatus("PENDING");

        String orderNumber =
                "FN"
                        + LocalDate.now().toString().replace("-", "")
                        + System.currentTimeMillis();

        order.setOrderNumber(orderNumber);

        orderMapper.insert(order);

        // orderId is automatically filled because of useGeneratedKeys
        Integer orderId = order.getOrderId();

        // 4. Save every cart item
        for (Cart cart : cartItems) {

            OrderItem item = new OrderItem();

            item.setOrderId(orderId);
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getPrice());

            orderItemMapper.insert(item);
        }

        // 5. Clear cart
        orderMapper.clearCart(userId);

        // 6. Return created order
        return order;
    }

}