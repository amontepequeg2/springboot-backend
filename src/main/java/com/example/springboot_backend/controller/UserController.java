package com.example.springboot_backend.controller;

import com.example.springboot_backend.model.User;
import com.example.springboot_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        existingUser.setRole(userDetails.getRole());

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userRepository.delete(existingUser);
        return ResponseEntity.noContent().build();
    }
}
