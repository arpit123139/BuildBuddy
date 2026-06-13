package com.example.Lovable.service;

import com.example.Lovable.dto.auth.UserProfileResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
   UserProfileResponse getProfile(Long userId);
}
