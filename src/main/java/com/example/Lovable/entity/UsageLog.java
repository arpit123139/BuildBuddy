package com.example.Lovable.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsageLog {

    Long id;
    User user;
    Project project;

    String action;

    Integer tokensUsed;

    Integer durationMs;

    String metadata;

    LocalDateTime createdAt;
}
