package com.example.cloudproject.orders.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedidos")  // ← cambiar "orders" por "pedidos"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id")   // ← agregar
    private Long userId;

    @Column(name = "total")        // ← agregar
    private Double total;

    @Column(name = "estado")       // ← agregar
    private String status;         // ← cambiar de OrderStatus a String

    @Column(name = "fecha")        // ← agregar
    private String createdAt;      // ← cambiar de LocalDateTime a String

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}