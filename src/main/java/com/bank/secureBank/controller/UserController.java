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

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return userService.findByUsername(username)
            .map(user -> {
                user.setPassword(null);
                return ResponseEntity.ok(user);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}