package com.example.cloudproject.catalog.service;

import com.example.cloudproject.catalog.domain.Author;
import com.example.cloudproject.catalog.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author create(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
    }

    public Author patch(Long id, Author request) {
        Author author = getById(id);

        if (request.getName() != null) {
            author.setName(request.getName());
        }

        return authorRepository.save(author);
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Autor no existe");
        }
        authorRepository.deleteById(id);
    }
}