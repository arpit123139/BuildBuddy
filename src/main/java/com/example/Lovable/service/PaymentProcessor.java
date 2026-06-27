package com.example.Lovable.service;

import com.example.Lovable.dto.subscription.CheckoutRequest;
import com.example.Lovable.dto.subscription.CheckoutResponse;
import com.example.Lovable.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
