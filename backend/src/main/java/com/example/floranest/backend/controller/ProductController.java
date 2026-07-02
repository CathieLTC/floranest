package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Product;
import com.example.floranest.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public String addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return "Product added successfully.";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @RequestBody Product product) {
        product.setProductId(id);
        productService.updateProduct(product);
        return "Product updated successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "Product deleted successfully.";
    }

}