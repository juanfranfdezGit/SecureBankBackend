package com.bank.secureBank.service;

import com.bank.secureBank.model.User;
import com.bank.secureBank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(User signupRequest) {
        
        Optional<User> existingUser = userRepository.findByUsername(signupRequest.getUsername());
        if(existingUser.isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
        signupRequest.setPassword(hashedPassword);

        User savedUser = userRepository.save(signupRequest);

        savedUser.setPassword(null);
        
        return savedUser;
    }
}