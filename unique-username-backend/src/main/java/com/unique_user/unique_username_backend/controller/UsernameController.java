package com.unique_user.unique_username_backend.controller;

import com.unique_user.unique_username_backend.model.Username;
import com.unique_user.unique_username_backend.repository.UsernameRepository;
import com.unique_user.unique_username_backend.service.BloomFilterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/create-username")
@CrossOrigin(origins = "http://localhost:3000")
public class UsernameController {

    private final UsernameRepository repository;
    private final BloomFilterService bloomService;

    public UsernameController(UsernameRepository repository, BloomFilterService bloomService) {
        this.repository = repository;
        this.bloomService = bloomService;
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("available", false));
        }

        boolean isAvailable;

        if (!bloomService.mightContain(username)) {
            isAvailable = true; // Definitely not in DB
        } else {
            isAvailable = !repository.existsByUsername(username); // False positive? Double-check DB
        }

        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUsername(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username cannot be empty.");
        }

        if (repository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        repository.save(new Username(username));
        bloomService.addUsername(username);
        return ResponseEntity.ok("Username added successfully.");
    }
}
