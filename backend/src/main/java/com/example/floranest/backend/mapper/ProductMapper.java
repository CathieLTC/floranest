package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> findAll();
    Product findById(Integer id);
    List<Product> findByCategory(Integer categoryId);
    List<Product> search(String keyword);
    void insert(Product product);
    void update(Product product);
    void delete(Integer id);
}