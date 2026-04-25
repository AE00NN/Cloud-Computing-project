package com.example.cloudproject.user.service;

import com.example.cloudproject.user.domain.User;
import com.example.cloudproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no existe");
        }
        userRepository.deleteById(id);
    }
}