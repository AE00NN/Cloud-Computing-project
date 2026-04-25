package com.example.cloudproject.aggregator.controller;

import com.example.cloudproject.aggregator.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getLibrary(@PathVariable Long userId) {
        return ResponseEntity.ok(libraryService.getUserLibrary(userId));
    }
}
