package com.example.UserService.service;

import com.example.UserService.dto.UserDTO;
import com.example.UserService.entity.User;
import com.example.UserService.exception.UserNotFoundException;
import com.example.UserService.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return new UserDTO(u.getId(), u.getName(), u.getEmail());
    }

    @Transactional
    public User createUser(User user) {
        // optional: check duplicate email
        repo.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new RuntimeException("Email already in use: " + user.getEmail());
        });
        return repo.save(user);
    }
}
