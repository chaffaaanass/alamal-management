package com.projects.demo.controllers;

import com.projects.demo.dtos.UserDTO;
import com.projects.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<UserDTO> createUser(@Validated({Default.class}) @RequestBody UserDTO userDTO) {
        UserDTO user = userService.createUser(userDTO);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String username, @Validated({Default.class}) @RequestBody UserDTO userDTO) {
        UserDTO user = userService.updateUser(username, userDTO);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
