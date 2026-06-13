package com.example.Lovable.service;

import com.example.Lovable.dto.auth.AuthResponse;
import com.example.Lovable.dto.auth.LoginRequest;
import com.example.Lovable.dto.auth.SignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    
     AuthResponse signup(SignupRequest request);

     AuthResponse login(LoginRequest request);


}
