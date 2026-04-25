package com.example.cloudproject.catalog.service;

import com.example.cloudproject.catalog.domain.Editorial;
import com.example.cloudproject.catalog.repository.EditorialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorialService {

    private final EditorialRepository editorialRepository;

    public Editorial create(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    public List<Editorial> getAll() {
        return editorialRepository.findAll();
    }

    public Editorial getById(Long id) {
        return editorialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Editorial no encontrada"));
    }

    public Editorial patch(Long id, Editorial request) {
        Editorial editorial = getById(id);

        if (request.getName() != null) {
            editorial.setName(request.getName());
        }

        return editorialRepository.save(editorial);
    }

    public void delete(Long id) {
        if (!editorialRepository.existsById(id)) {
            throw new EntityNotFoundException("Editorial no existe");
        }
        editorialRepository.deleteById(id);
    }
}