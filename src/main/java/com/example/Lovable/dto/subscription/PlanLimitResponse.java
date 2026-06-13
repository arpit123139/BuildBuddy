package com.example.Lovable.dto.subscription;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanLimitResponse {

    String planName;
    int maxTokensPerDay;
    int maxProjects;
    boolean unlimitedAI;
}
