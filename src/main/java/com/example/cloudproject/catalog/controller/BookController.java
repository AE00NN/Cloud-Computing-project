package com.example.cloudproject.catalog.controller;

import com.example.cloudproject.catalog.domain.Book;
import com.example.cloudproject.catalog.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.create(book));
    }


    @GetMapping
    public ResponseEntity<List<Book>> getAll(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return ResponseEntity.ok(
                bookService.getAll(authorId, genreId, minPrice, maxPrice)
        );
    }


    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(@RequestParam String q) {
        return ResponseEntity.ok(bookService.search(q));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Book> patch(
            @PathVariable Long id,
            @RequestBody Book book
    ) {
        return ResponseEntity.ok(bookService.patch(id, book));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
