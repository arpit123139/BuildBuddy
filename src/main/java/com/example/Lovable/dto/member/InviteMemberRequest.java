package com.example.Lovable.dto.member;

import com.example.Lovable.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteMemberRequest {

    @Email
    @NotBlank
    String username;

    @NotNull
    ProjectRole role;
}
