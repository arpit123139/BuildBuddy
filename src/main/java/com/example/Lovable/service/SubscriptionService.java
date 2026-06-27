package com.example.Lovable.service;

import com.example.Lovable.dto.subscription.*;
import com.example.Lovable.enums.SubscriptionStatus;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

public interface SubscriptionService {
     SubscriptionResponse getCurrentSubscription();

    void createSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void cancelSubscription(String subscriptId);

    void updateSubscription(String subscriptId, SubscriptionStatus status, Instant periodStart, Instant periodEnd,
                            Boolean cancelAtPeriodEnd, Long planId);

    void renewSubscriptionPeriod(String subscriptionId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subscriptionId);

    boolean canCreateNewProject();

     PlanResponse getAllActivePlans();
}
