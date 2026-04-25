package com.example.cloudproject.catalog.service;

import com.example.cloudproject.catalog.domain.Genre;
import com.example.cloudproject.catalog.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Genre getById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Género no encontrado"));
    }

    public Genre patch(Long id, Genre request) {
        Genre genre = getById(id);

        if (request.getName() != null) {
            genre.setName(request.getName());
        }

        return genreRepository.save(genre);
    }

    public void delete(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Género no existe");
        }
        genreRepository.deleteById(id);
    }
}