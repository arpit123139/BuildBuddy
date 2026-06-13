package com.example.Lovable.dto.subscription;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


public class SubscriptionResponse {

    PlanResponse plan;
    String status;
    LocalDateTime periodEnd;
    Long tokenUsedThisCycle;
}
