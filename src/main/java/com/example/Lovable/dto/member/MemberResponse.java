package com.example.Lovable.dto.member;


import com.example.Lovable.enums.ProjectRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberResponse {

    private Long userId;
    private String email;
    LocalDateTime invitedAt;
    ProjectRole projectRole;

}
