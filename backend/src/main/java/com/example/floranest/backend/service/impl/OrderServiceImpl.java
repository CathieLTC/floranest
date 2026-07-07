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

    public OrderServiceImpl(OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
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
    public List<Order> getOrderByUserId(Integer userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order checkout(Integer userId, String address) {

        // 1. Get all cart items for this user
        List<Cart> cartItems = orderMapper.getCartItems(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        // 2. Calculate total amount
        BigDecimal total = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            BigDecimal subTotal = cart.getPrice()
                    .multiply(BigDecimal.valueOf(cart.getQuantity()));
            total = total.add(subTotal);
        }

        // 3. Build and save the order (including shipping address)
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setOrderStatus("PENDING");
        order.setAddress(address);
        order.setOrderNumber(
                "FN" + LocalDate.now().toString().replace("-", "")
                        + System.currentTimeMillis()
        );

        orderMapper.insert(order);

        // orderId is filled automatically via useGeneratedKeys
        Integer orderId = order.getOrderId();

        // 4. Save each cart item as an order item
        for (Cart cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getPrice());
            orderItemMapper.insert(item);
        }

        // 5. Clear the cart
        orderMapper.clearCart(userId);

        return order;
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

    /**
     * @param userId
     * @return
     */
    @Override
    public Order checkout(Integer userId) {
        return null;
    }
}