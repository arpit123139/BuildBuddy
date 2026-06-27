package com.example.Lovable.dto.subscription;

import com.example.Lovable.enums.SubscriptionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SubscriptionResponse {

    PlanResponse plan;
    SubscriptionStatus status;
    Instant currentPeriodEnd;
    Long tokenUsedThisCycle;
}
