package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.Product;
import com.example.floranest.backend.mapper.ProductMapper;
import com.example.floranest.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    @Override
    public Product getProductById(Integer id) {
        return productMapper.findById(id);
    }

    @Override
    public void addProduct(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
        productMapper.update(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productMapper.delete(id);
    }

}