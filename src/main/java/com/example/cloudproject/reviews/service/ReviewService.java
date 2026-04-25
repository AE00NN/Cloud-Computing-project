package com.example.cloudproject.reviews.service;

import com.example.cloudproject.reviews.domain.Review;
import com.example.cloudproject.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review create(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public List<Review> getByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}