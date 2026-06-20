package com.example.Lovable.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {

     @Email
     @NotBlank
     String username;
     @Size(min = 4 , max = 50)
     String password;
     @Size(min = 1,max = 30)
     String name;
}
