package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findAll();

    Cart findById(Integer cartId);

    int insert(Cart cart);

    int update(Cart cart);

    int delete(Integer cartId);

}