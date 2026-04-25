package com.example.cloudproject.catalog.controller;

import com.example.cloudproject.catalog.domain.Editorial;
import com.example.cloudproject.catalog.service.EditorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editorials")
@RequiredArgsConstructor
public class EditorialController {

    private final EditorialService editorialService;

    @PostMapping
    public ResponseEntity<Editorial> create(@RequestBody @Valid Editorial editorial) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(editorialService.create(editorial));
    }

    @GetMapping
    public ResponseEntity<List<Editorial>> getAll() {
        return ResponseEntity.ok(editorialService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editorial> getById(@PathVariable Long id) {
        return ResponseEntity.ok(editorialService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Editorial> patch(
            @PathVariable Long id,
            @RequestBody @Valid Editorial request
    ) {
        return ResponseEntity.ok(editorialService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        editorialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}