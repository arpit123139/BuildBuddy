package com.example.Lovable.dto.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

   private String token;

   private  UserProfileResponse user;

}
