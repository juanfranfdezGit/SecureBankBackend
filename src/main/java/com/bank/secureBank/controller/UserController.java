package com.bank.secureBank.controller;

import com.bank.secureBank.model.User;
import com.bank.secureBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
        public ResponseEntity<User> registerUser(@RequestBody User user) {
            User savedUser = userService.save(user);
            return ResponseEntity.status(201).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        return userService.findByUsername(loginRequest.getUsername())
            .filter(user -> user.getPassword().equals(loginRequest.getPassword()))
            .map(user -> ResponseEntity.ok("Login correcto"))
            .orElse(ResponseEntity.status(401).body("Credenciales inválidas"));
    }
}