package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.subscription.PlanLimitResponse;
import com.example.Lovable.dto.subscription.UsageTodayResponse;
import com.example.Lovable.service.UsageService;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public @Nullable UsageTodayResponse getTodayUsage(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
