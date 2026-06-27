package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.subscription.CheckoutRequest;
import com.example.Lovable.dto.subscription.CheckoutResponse;
import com.example.Lovable.dto.subscription.PortalResponse;
import com.example.Lovable.entity.Plan;
import com.example.Lovable.entity.User;
import com.example.Lovable.enums.SubscriptionStatus;
import com.example.Lovable.error.BadRequestException;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.repository.PlanRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.security.AuthUtil;
import com.example.Lovable.service.PaymentProcessor;
import com.example.Lovable.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcessor implements PaymentProcessor {

    private final PlanRepository planRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Value("${client.url}")
    private String frontEndUrl;


    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request) {

        Plan plan=planRepository.findById(request.getPlanId()).orElseThrow(()->new ResourceNotFoundException("Plan",
                request.getPlanId()));

        Long userId= authUtil.getCurrentUserId();
        User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User",userId));


        var params = SessionCreateParams.builder()
                .addLineItem(
                        // Associate an item with the subscription for us it is the Plan
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripeId())
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        new SessionCreateParams.SubscriptionData.Builder()
                                .setBillingMode(
                                        SessionCreateParams.SubscriptionData.BillingMode.builder()
                                                .setType(
                                                        //The Billing type Flexible mwans suppose the user purchased
                                                        // the plan on 15th then the billing cycle will be from 15th
                                                        // of this month to 15th of next month (30 Day Billing cycle)
                                                        SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE
                                                )
                                                .build()
                                )
                                .build()
                )
                .setSuccessUrl(frontEndUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontEndUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());

        try {

            String stripCustomerId=user.getStripeCustomerId();
            if(stripCustomerId==null || stripCustomerId.isEmpty()){
                params.setCustomerEmail(user.getUsername());
            }
            else
                params.setCustomer(stripCustomerId);

            Session session=Session.create(params.build());   //Making API call to the stripe Backend
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal() {

        Long userId=authUtil.getCurrentUserId();
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User",userId));

        String stripeCustomerId=user.getStripeCustomerId();

        if(stripeCustomerId==null || stripeCustomerId.isEmpty()){
            throw new BadRequestException("User does not have a strip Customer Id , UserId: "+userId);

        }

        try {
            var portalSession= com.stripe.model.billingportal.Session.create(
                    com.stripe.param.billingportal.SessionCreateParams.builder()
                            .setCustomer(stripeCustomerId)
                            .setReturnUrl(frontEndUrl)
                            .build());

            return new PortalResponse(portalSession.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
        log.info("Handling stripe event type "+type);
//        log.info(type);
        switch (type){

            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject,metadata);
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
            case "customer.subscription.deleted" -> handleSubscriptionDeleted((Subscription) stripeObject);
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject);
            case "invoice.payment_failed" -> handleInvoiceFailed((Invoice) stripeObject);  // when the invoice is not
            // paid , mark the subscription has past_due
            default -> log.debug("Ignoring the event: {}",type);
        }
    }

    public void handleCheckoutSessionCompleted(Session session, Map<String, String> metadata){


        if(session==null){
            log.error("Session Object was null");
            return ;
        }
        Long userId=Long.parseLong(metadata.get("user_id"));
        Long planId=Long.parseLong(metadata.get("plan_id"));

        String subscriptionId=session.getSubscription();
        String customerId=session.getCustomer();

        User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User",userId));

        if(user.getStripeCustomerId()==null || user.getStripeCustomerId().isEmpty() || user.getStripeCustomerId().isBlank()){
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }



        subscriptionService.createSubscription(userId,planId,subscriptionId,customerId);  // here we
        // only create
        // the
        // subscription object but did not mark it as active

    }

    public void handleCustomerSubscriptionUpdated(Subscription subscription){

        if(subscription==null){
            log.error("subscription object was null inside handleCustomerSubscriptionUpdated");
            return;
        }
        SubscriptionStatus status = mapStripeStatusToEnum(subscription.getStatus());
        if(status==null){
            log.warn("Unknown Status '{}' for subscription {}",subscription.getStatus(),subscription.getId());
            return;
        }

        // these are the line items for us it is the plan
        SubscriptionItem item=subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice());

        //cancelAtPeriodEnd -- will be set to true when your subscription is going to be ended after current cycle
        // and when it will be finally cacelled we will recieve subscription.deleted event
        subscriptionService.updateSubscription(subscription.getId(),status,periodStart,periodEnd,
                subscription.getCancelAtPeriodEnd(),planId);


    }

    public void handleSubscriptionDeleted(Subscription subscription){

        if(subscription==null){
            log.error("subscription object was null inside handleCustomerSubscriptionDeleted");
            return ;
        }
        subscriptionService.cancelSubscription(subscription.getId());

    }

    // this will be called every month when the subscription is renewed and the payment was successfull
    public void handleInvoicePaid(Invoice invoice){
        // we need a subsctiption object to update it to have the current billing perios
        String subscriptionId = extractSubscriptionId(invoice);
        if(subscriptionId==null) return;

        try {
            Subscription subscription=Subscription.retrieve(subscriptionId);
            var item=subscription.getItems().getData().get(0);
            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

            subscriptionService.renewSubscriptionPeriod(
                    subscriptionId,
                    periodStart,
                    periodEnd
            );

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    public void handleInvoiceFailed(Invoice invoice){

        String subscriptionId = extractSubscriptionId(invoice);
        if(subscriptionId == null) return;

        subscriptionService.markSubscriptionPastDue(subscriptionId);
    }


    //Utility Function

    private SubscriptionStatus mapStripeStatusToEnum(String status) {
        return switch (status) {
            case "active" -> SubscriptionStatus.ACTIVE;
            case "trialing" -> SubscriptionStatus.TRIALING;
            case "past_due", "unpaid", "paused", "incomplete_expired" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unmapped Stripe status: {}", status);
                yield null;
            }
        };
    }

    private Instant toInstant(Long epoch) {
        return epoch != null ? Instant.ofEpochSecond(epoch) : null;
    }

    private Long resolvePlanId(Price price){
        if(price==null || price.getId()==null) return null;

        return planRepository.findByStripeId(price.getId()).map(plan -> plan.getId()).orElseThrow(()->new ResourceNotFoundException("Plan with StripeID ", price.getId()));
    }

    private String extractSubscriptionId(Invoice invoice) {
        var parent = invoice.getParent();
        if (parent == null) return null;

        var subDetails = parent.getSubscriptionDetails();
        if (subDetails == null) return null;

        return subDetails.getSubscription();
    }


}
