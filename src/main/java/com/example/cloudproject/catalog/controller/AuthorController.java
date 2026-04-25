package com.example.cloudproject.catalog.controller;

import com.example.cloudproject.catalog.domain.Author;
import com.example.cloudproject.catalog.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public Author create(@RequestBody Author author) {
        return authorService.create(author);
    }

    @GetMapping
    public List<Author> getAll() {
        return authorService.getAll();
    }

    @GetMapping("/{id}")
    public Author getById(@PathVariable Long id) {
        return authorService.getById(id);
    }

    @PatchMapping("/{id}")
    public Author patch(@PathVariable Long id, @RequestBody Author request) {
        return authorService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}