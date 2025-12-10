package com.example.UserService.service;

import com.example.UserService.dto.UserDTO;
import com.example.UserService.entity.User;
import com.example.UserService.exception.UserNotFoundException;
import com.example.UserService.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return repo.findAll().stream()
                .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public User createUser(User user) {
        repo.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new RuntimeException("Email already in use: " + user.getEmail());
        });
        return repo.save(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());

        User updated = repo.save(existing);

        return new UserDTO(updated.getId(), updated.getName(), updated.getEmail());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        repo.delete(user);
    }
}
