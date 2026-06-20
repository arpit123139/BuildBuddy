package com.example.Lovable.controller;

import com.example.Lovable.dto.auth.AuthResponse;
import com.example.Lovable.dto.auth.LoginRequest;
import com.example.Lovable.dto.auth.SignupRequest;
import com.example.Lovable.dto.auth.UserProfileResponse;
import com.example.Lovable.service.AuthService;
import com.example.Lovable.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup (@RequestBody @Valid  SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody @Valid  LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile (){
        Long userId=1l;
        return ResponseEntity.ok(userService.getProfile(userId));
    }


}
