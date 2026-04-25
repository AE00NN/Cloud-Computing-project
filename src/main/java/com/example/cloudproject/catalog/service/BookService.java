package com.example.cloudproject.catalog.service;


import com.example.cloudproject.catalog.domain.Author;
import com.example.cloudproject.catalog.domain.Book;
import com.example.cloudproject.catalog.domain.Editorial;
import com.example.cloudproject.catalog.domain.Genre;
import com.example.cloudproject.catalog.repository.AuthorRepository;
import com.example.cloudproject.catalog.repository.BookRepository;
import com.example.cloudproject.catalog.repository.EditorialRepository;
import com.example.cloudproject.catalog.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final EditorialRepository editorialRepository;


    public Book create(Book book) {

        if (book.getAuthor() == null) {
            throw new EntityNotFoundException("Author es obligatorio");
        }

        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));

        book.setAuthor(author);

        if (book.getGenre() != null) {
            Genre genre = genreRepository.findById(book.getGenre().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Género no encontrado"));
            book.setGenre(genre);
        }

        if (book.getEditorial() != null) {
            Editorial editorial = editorialRepository.findById(book.getEditorial().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Editorial no encontrada"));
            book.setEditorial(editorial);
        }

        return bookRepository.save(book);
    }


    public List<Book> getAll(Long authorId, Long genreId, Double minPrice, Double maxPrice) {

        Specification<Book> spec = (root, query, cb) -> cb.conjunction();

        if (authorId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.join("author").get("id"), authorId));
        }

        if (genreId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.join("genre").get("id"), genreId));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return bookRepository.findAll(spec);
    }

    public List<Book> search(String query) {
        return bookRepository.findAll((root, q, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + query.toLowerCase() + "%")
        );
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

        if (request.getAuthor() != null) {
            Author author = authorRepository.findById(request.getAuthor().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
            book.setAuthor(author);
        }

        if (request.getGenre() != null) {
            Genre genre = genreRepository.findById(request.getGenre().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Género no encontrado"));
            book.setGenre(genre);
        }

        if (request.getEditorial() != null) {
            Editorial editorial = editorialRepository.findById(request.getEditorial().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Editorial no encontrada"));
            book.setEditorial(editorial);
        }

        return bookRepository.save(book);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Libro no existe");
        }
        bookRepository.deleteById(id);
    }
}
