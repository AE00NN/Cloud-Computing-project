package com.example.cloudproject.aggregator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LibraryService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Object getUserLibrary(Long userId) {

        String orders = "http://orders-service/orders/user/" + userId;
        String reviews = "http://reviews-service/reviews/user/" + userId;

        Object orderData = restTemplate.getForObject(orders, Object.class);
        Object reviewData = restTemplate.getForObject(reviews, Object.class);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orderData);
        result.put("reviews", reviewData);

        return result;
    }
}