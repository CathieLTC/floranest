package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.Review;
import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews();
    boolean addReview(Review review);
    boolean deleteReview(Integer reviewId);
}
