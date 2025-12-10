package com.example.UserService.controller;

import com.example.UserService.dto.UserDTO;
import com.example.UserService.entity.User;
import com.example.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Service", description = "Operations related to Users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a single user by their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @Parameter(description = "ID of the user to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Operation(summary = "Create a new user", description = "Create a new user with a valid name and email")
    @PostMapping
    public ResponseEntity<User> create(
            @Parameter(description = "User object to be created")
            @Valid @RequestBody User user) {
        User saved = service.createUser(user);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update user", description = "Update user details by ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {

        UserDTO updatedUser = service.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID of the user to delete", example = "1")
            @PathVariable Long id) {

        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
