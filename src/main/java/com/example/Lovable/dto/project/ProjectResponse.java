package com.example.Lovable.dto.project;

import com.example.Lovable.dto.auth.UserProfileResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectResponse {

    Long id;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserProfileResponse user;
}
