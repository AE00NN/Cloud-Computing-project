package com.example.cloudproject.catalog.service;

import com.example.cloudproject.catalog.domain.Book;
import com.example.cloudproject.catalog.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAll(Long authorId, Long genreId, Double minPrice, Double maxPrice) {
        List<Book> books = bookRepository.findAll();

        if (authorId != null) {
            books = books.stream()
                    .filter(b -> authorId.equals(b.getAuthorId()))
                    .collect(Collectors.toList());
        }
        if (genreId != null) {
            books = books.stream()
                    .filter(b -> genreId.equals(b.getGenreId()))
                    .collect(Collectors.toList());
        }
        if (minPrice != null) {
            books = books.stream()
                    .filter(b -> b.getPrice() >= minPrice)
                    .collect(Collectors.toList());
        }
        if (maxPrice != null) {
            books = books.stream()
                    .filter(b -> b.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        return books;
    }

    public List<Book> search(String query) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle() != null &&
                        b.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado"));
    }

    public Book patch(Long id, Book request) {
        Book book = getById(id);
        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getStock() != null) book.setStock(request.getStock());
        if (request.getAuthorId() != null) book.setAuthorId(request.getAuthorId());
        if (request.getGenreId() != null) book.setGenreId(request.getGenreId());
        if (request.getEditorialId() != null) book.setEditorialId(request.getEditorialId());
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Libro no existe");
        }
        bookRepository.deleteById(id);
    }
}