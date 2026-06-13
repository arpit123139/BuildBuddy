package com.example.Lovable.service;

import com.example.Lovable.dto.subscription.CheckoutRequest;
import com.example.Lovable.dto.subscription.CheckoutResponse;
import com.example.Lovable.dto.subscription.PortalResponse;
import com.example.Lovable.dto.subscription.SubscriptionResponse;
import org.jspecify.annotations.Nullable;

public interface SubscriptionService {
     SubscriptionResponse getCurrentSubscription(Long userId);

     CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

     PortalResponse openCustomerPortal(Long userId);
}
