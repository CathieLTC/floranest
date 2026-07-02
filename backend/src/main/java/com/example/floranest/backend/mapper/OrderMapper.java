package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findAll();

    Order findById(Integer orderId);

    int insert(Order order);

    int update(Order order);

    int delete(Integer orderId);

}