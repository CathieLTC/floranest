package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    Product findById(Integer productId);

    int insert(Product product);

    int update(Product product);

    int delete(Integer productId);

}