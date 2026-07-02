package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();
    Category findById(Integer categoryId);
    int insert(Category category);
    int update(Category category);
    int delete(Integer categoryId);

}