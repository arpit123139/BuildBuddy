package com.example.Lovable.service;

import com.example.Lovable.dto.subscription.PlanLimitResponse;
import com.example.Lovable.dto.subscription.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageService {
    public @Nullable UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
