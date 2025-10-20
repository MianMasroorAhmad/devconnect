package com.devconnect.devconnect.service;

import com.devconnect.devconnect.dto.AuthResponse;
import com.devconnect.devconnect.dto.LoginRequest;
import com.devconnect.devconnect.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
}
