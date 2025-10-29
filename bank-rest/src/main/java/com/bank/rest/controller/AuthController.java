package com.bank.rest.controller;

import com.bank.rest.dto.AuthRequest;
import com.bank.rest.dto.AuthResponse;
import com.bank.rest.dto.RegisterRequest;
import com.bank.rest.entity.User;
import com.bank.rest.domainRoles.UserRole;
import com.bank.rest.repositories.UserRepo;
import com.bank.rest.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            System.out.println("Received register request: " + request);

            if (userRepo.findByUsername(request.username()).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            User user = new User();
            user.setUsername(request.username());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(UserRole.USER);
            userRepo.save(user);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        var user = userRepo.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

