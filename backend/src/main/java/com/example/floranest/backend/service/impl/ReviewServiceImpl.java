package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.Review;
import com.example.floranest.backend.mapper.ReviewMapper;
import com.example.floranest.backend.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewMapper.findAll();
    }

    @Override
    public boolean addReview(Review review) {

        if (review.getTargetName() == null
                || review.getTargetName().trim().isEmpty()
                || review.getAuthorName() == null
                || review.getAuthorName().trim().isEmpty()) {
            return false;
        }

        String type = review.getReviewType();
        if (!"shop".equalsIgnoreCase(type)) {
            type = "plant";
        }
        review.setReviewType(type.toLowerCase());

        int rating = review.getRating() == null ? 5 : review.getRating();
        review.setRating(Math.max(1, Math.min(5, rating)));

        review.setTargetName(review.getTargetName().trim());
        review.setAuthorName(review.getAuthorName().trim());
        if (review.getComment() == null) {
            review.setComment("");
        }

        return reviewMapper.insert(review) > 0;
    }

    @Override
    public boolean deleteReview(Integer reviewId) {
        return reviewMapper.delete(reviewId) > 0;
    }
}
