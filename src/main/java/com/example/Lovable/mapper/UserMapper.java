package com.example.Lovable.mapper;

import com.example.Lovable.dto.auth.SignupRequest;
import com.example.Lovable.dto.auth.UserProfileResponse;
import com.example.Lovable.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(SignupRequest signupRequest);

    UserProfileResponse toUserProfileResponse(User user);
}
