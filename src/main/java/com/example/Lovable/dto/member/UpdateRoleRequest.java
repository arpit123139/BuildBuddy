package com.example.Lovable.dto.member;

import com.example.Lovable.enums.ProjectRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoleRequest {

    ProjectRole role;
}
