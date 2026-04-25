package com.example.cloudproject.orders.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "detalle_pedido")  // ← cambiar
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libro_id")   // ← agregar
    private Long bookId;

    @Column(name = "cantidad")   // ← agregar
    private Integer quantity;

    @Column(name = "precio_unitario")  // ← agregar
    private Double price;

    @ManyToOne
    @JoinColumn(name = "pedido_id")  // ← cambiar "order_id" por "pedido_id"
    private Order order;
}