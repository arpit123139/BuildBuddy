package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.auth.AuthResponse;
import com.example.Lovable.dto.auth.LoginRequest;
import com.example.Lovable.dto.auth.SignupRequest;
import com.example.Lovable.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
