package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.entity.User;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Get all users (admin)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (updatedUser.getFullName() != null) existingUser.setFullName(updatedUser.getFullName());
            if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getPhoneNo() != null) existingUser.setPhoneNo(updatedUser.getPhoneNo());
            if (updatedUser.getAddress() != null) existingUser.setAddress(updatedUser.getAddress());
            userRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete user (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Change user role (admin)
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> changeRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            User user = userOptional.get();
            user.setRole(com.jayakulan.urbannest.entity.Role.valueOf(body.get("role").toUpperCase()));
            return ResponseEntity.ok(userRepository.save(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
