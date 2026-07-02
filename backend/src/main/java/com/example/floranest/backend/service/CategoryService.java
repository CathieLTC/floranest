package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Category;
import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    void addCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Integer id);

}