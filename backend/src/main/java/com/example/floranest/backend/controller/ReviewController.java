package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Review;
import com.example.floranest.backend.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /** Shared feed — any visitor can read what customers have written. */
    @GetMapping
    public List<Review> getReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping
    public Map<String, Object> addReview(@RequestBody Review review) {

        Map<String, Object> response = new HashMap<>();

        if (reviewService.addReview(review)) {
            response.put("success", true);
            response.put("message", "Review submitted. Thank you for sharing!");
            response.put("review", review);
        } else {
            response.put("success", false);
            response.put("message", "Please fill in the target and your name.");
        }

        return response;
    }

    /** Used by the Admin dashboard to remove a customer review. */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteReview(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        if (reviewService.deleteReview(id)) {
            response.put("success", true);
            response.put("message", "Review deleted.");
        } else {
            response.put("success", false);
            response.put("message", "Review not found.");
        }

        return response;
    }
}
