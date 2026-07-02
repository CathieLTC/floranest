package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Integer id);

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Integer id);

}