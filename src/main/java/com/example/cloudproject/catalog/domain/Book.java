package com.example.cloudproject.catalog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 100)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "editorial_id", nullable = false)
    private Editorial editorial;

    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
    private Double price;

    @Column(nullable = false)
    @PositiveOrZero
    private Integer stock;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}