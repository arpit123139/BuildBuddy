package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.auth.UserProfileResponse;
import com.example.Lovable.entity.User;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User",username));
        return user;
    }
}
