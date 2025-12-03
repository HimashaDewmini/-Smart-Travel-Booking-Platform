package com.example.UserService.controller;



import com.example.UserService.dto.UserDTO;
import com.example.UserService.entity.User;
import com.example.UserService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Get user by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    /**
     * Create user.
     * Uses @Valid so validation annotations on User entity are validated.
     */
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User saved = service.createUser(user);
        return ResponseEntity.ok(saved);
    }
}
