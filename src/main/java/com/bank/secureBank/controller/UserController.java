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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        var userOptional = userService.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                user.setPassword(null); 
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User signupRequest) {
        if (userService.findByUsername(signupRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(409).body("El nombre de usuario ya existe");
        }

        User newUser = userService.save(signupRequest);
        newUser.setPassword(null);

        return ResponseEntity.status(201).body(newUser);
    }
}