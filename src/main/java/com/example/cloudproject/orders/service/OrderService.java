package com.example.cloudproject.orders.service;

import com.example.cloudproject.orders.domain.Order;
import com.example.cloudproject.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order create(Order order) {
        order.setStatus("pendiente");        // ← String en vez de OrderStatus.PENDING
        order.setCreatedAt(LocalDateTime.now().toString()); // ← toString()
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public List<Order> getByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateStatus(Long id, String status) { // ← String en vez de OrderStatus
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}