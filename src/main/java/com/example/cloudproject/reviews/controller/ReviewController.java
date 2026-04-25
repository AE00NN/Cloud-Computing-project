package com.example.cloudproject.reviews.controller;

import com.example.cloudproject.reviews.domain.Review;
import com.example.cloudproject.reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(review));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getByBook(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getByUser(userId));
    }
}