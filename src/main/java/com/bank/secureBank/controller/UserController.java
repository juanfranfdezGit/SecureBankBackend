package com.bank.secureBank.controller;

import com.bank.secureBank.model.User;
import com.bank.secureBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import com.bank.secureBank.utils.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        System.out.println("Login request for user: " + loginRequest.getUsername());
        var userOptional = userService.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            System.out.println("User not found");
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        User user = userOptional.get();
        System.out.println("User found: " + user.getUsername());
        System.out.println("Encoded password: " + user.getPassword());

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            System.out.println("Token generated");
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            System.out.println("Password mismatch");
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User signupRequest) {
        try {
            User registeredUser = userService.register(signupRequest);
            return ResponseEntity.status(201).body(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint accesible");
    }
}