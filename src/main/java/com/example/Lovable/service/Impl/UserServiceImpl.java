package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.auth.UserProfileResponse;
import com.example.Lovable.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
