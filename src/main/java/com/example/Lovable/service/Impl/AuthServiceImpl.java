package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.auth.AuthResponse;
import com.example.Lovable.dto.auth.LoginRequest;
import com.example.Lovable.dto.auth.SignupRequest;
import com.example.Lovable.entity.User;
import com.example.Lovable.error.BadRequestException;
import com.example.Lovable.mapper.UserMapper;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.security.AuthUtil;
import com.example.Lovable.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
      private final PasswordEncoder passwordEncoder;
      private final AuthUtil authUtil;
      private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {

        userRepository.findByUsername(request.getUsername()).ifPresent((user)-> { throw new BadRequestException("User Already exsist with username "+request.getUsername());});

        User user=userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user=userRepository.save(user);

        String token=authUtil.generateAccessToken(user);

        return new AuthResponse(token,userMapper.toUserProfileResponse(user));

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication=
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));

        User user=(User)authentication.getPrincipal();

        String token=authUtil.generateAccessToken(user);
        return new AuthResponse(token,userMapper.toUserProfileResponse(user));


    }
}
