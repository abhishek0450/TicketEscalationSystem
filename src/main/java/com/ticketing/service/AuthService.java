package com.ticketing.service;

import com.ticketing.dto.request.LoginRequest;
import com.ticketing.dto.request.RegisterRequest;
import com.ticketing.dto.response.AuthResponse;

// TODO: implemented in Phase X
public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
