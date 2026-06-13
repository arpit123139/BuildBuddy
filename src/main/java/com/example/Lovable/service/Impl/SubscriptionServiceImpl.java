package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.subscription.CheckoutRequest;
import com.example.Lovable.dto.subscription.CheckoutResponse;
import com.example.Lovable.dto.subscription.PortalResponse;
import com.example.Lovable.dto.subscription.SubscriptionResponse;
import com.example.Lovable.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
