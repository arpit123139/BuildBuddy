package com.example.Lovable.entity;

import com.example.Lovable.enums.ProjectRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {

    ProjectMemberId projectMemberId;

    Project project;

    User user;

    ProjectRole role;

    User invitedBy;

    LocalDateTime invitedAt;
}
