package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Category;
import com.example.floranest.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public String addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return "Category added successfully.";
    }

    @PutMapping("/{id}")
    public String updateCategory(@PathVariable Integer id,
                                 @RequestBody Category category) {
        category.setCategoryId(id);
        categoryService.updateCategory(category);
        return "Category updated successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "Category deleted successfully.";
    }

}