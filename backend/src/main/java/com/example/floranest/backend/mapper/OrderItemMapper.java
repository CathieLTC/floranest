package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper {

    int insert(OrderItem orderItem);
    void deleteByOrderId(Integer orderId);
}