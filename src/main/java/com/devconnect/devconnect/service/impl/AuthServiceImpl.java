package com.devconnect.devconnect.service.impl;

import com.devconnect.devconnect.dto.AuthResponse;
import com.devconnect.devconnect.dto.LoginRequest;
import com.devconnect.devconnect.dto.RegisterRequest;
import com.devconnect.devconnect.model.User;
import com.devconnect.devconnect.repository.UserRepository;
import com.devconnect.devconnect.service.AuthService;
import com.devconnect.devconnect.service.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.devconnect.devconnect.security.JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, com.devconnect.devconnect.security.JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("username taken");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("email taken");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(u);
        String token = tokenProvider.createToken(u.getId(), u.getUsername());
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = tokenProvider.createToken(u.getId(), u.getUsername());
        return new AuthResponse(token);
    }
}
