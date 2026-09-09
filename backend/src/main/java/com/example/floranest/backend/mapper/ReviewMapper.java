package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ReviewMapper {

    List<Review> findAll();
    Review findById(Integer reviewId);
    int insert(Review review);
    int delete(Integer reviewId);
}
