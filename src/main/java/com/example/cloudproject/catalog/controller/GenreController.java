package com.example.cloudproject.catalog.controller;

import com.example.cloudproject.catalog.domain.Genre;
import com.example.cloudproject.catalog.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<Genre> create(@RequestBody @Valid Genre genre) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreService.create(genre));
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Genre> patch(
            @PathVariable Long id,
            @RequestBody @Valid Genre request
    ) {
        return ResponseEntity.ok(genreService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}