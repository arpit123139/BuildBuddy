package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.subscription.PlanResponse;
import com.example.Lovable.dto.subscription.SubscriptionResponse;
import com.example.Lovable.entity.Plan;
import com.example.Lovable.entity.Subscription;
import com.example.Lovable.entity.User;
import com.example.Lovable.enums.SubscriptionStatus;
import com.example.Lovable.error.ResourceNotFoundException;
import com.example.Lovable.mapper.SubscriptionMapper;
import com.example.Lovable.repository.PlanRepository;
import com.example.Lovable.repository.ProjectMemberRepository;
import com.example.Lovable.repository.SubscriptionRepository;
import com.example.Lovable.repository.UserRepository;
import com.example.Lovable.security.AuthUtil;
import com.example.Lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId= authUtil.getCurrentUserId();

        Subscription subscription= subscriptionRepository.findByUserIdAndStatusIn(userId,
                Set.of(SubscriptionStatus.ACTIVE,
                SubscriptionStatus.PAST_DUE,SubscriptionStatus.TRIALING)).orElse(null);

        return subscriptionMapper.toSubscriptionResponse(subscription);
    }

    @Override
    public void createSubscription(Long userId, Long planId, String subscriptionId, String customerId ) {
        //create Subscription but do not activate
        boolean exsist=subscriptionRepository.existsByStripSubscriptionId(subscriptionId);
        if(exsist) return;

        User user=userRepository.getReferenceById(userId);
        Plan plan= planRepository.getReferenceById(planId);

        Subscription subscription=Subscription.builder()
                .user(user)
                .plan(plan)
                .stripSubscriptionId(subscriptionId)
                .stripCustomerId(customerId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {

        Subscription subscription=
                subscriptionRepository.findByStripSubscriptionId(gatewaySubscriptionId).orElseThrow(()->new ResourceNotFoundException("Subscription",gatewaySubscriptionId));

        if(subscription.getStatus()==SubscriptionStatus.CANCELED){
            log.debug("Subscription is already set to cancel :{}",gatewaySubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription=subscriptionRepository.findByStripSubscriptionId(gatewaySubscriptionId).orElseThrow(()->new ResourceNotFoundException("Subscription",gatewaySubscriptionId));

        boolean subscriptionUpdated=false;
        if(status!=null && status!=subscription.getStatus()){
            subscriptionUpdated=true;
            subscription.setStatus(status);
        }

        if(periodStart!=null && periodStart!=subscription.getCurrentPeriodStart()){
            subscriptionUpdated=true;
            subscription.setCurrentPeriodStart(periodStart);
        }


        if(periodEnd!=null && periodEnd!=subscription.getCurrentPeriodEnd()){
            subscriptionUpdated=true;
            subscription.setCurrentPeriodEnd(periodEnd);
        }


        if(cancelAtPeriodEnd!=null && cancelAtPeriodEnd!=subscription.getCancelAtPeriodEnd()){
            subscriptionUpdated=true;
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
        }


        if(planId!=null && !subscription.getPlan().getId().equals(planId)){
            subscriptionUpdated=true;
            Plan newPlan=planRepository.getReferenceById(planId);
            subscription.setPlan(newPlan);
        }

        if(subscriptionUpdated){
            log.info("Subscription with id {} has been updated",gatewaySubscriptionId);
            subscriptionRepository.save(subscription);
        }


    }

    //When the invoice is paid
    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Subscription subscription=subscriptionRepository.findByStripSubscriptionId(gatewaySubscriptionId).orElseThrow(()->new ResourceNotFoundException("Subscription",gatewaySubscriptionId));

        // Although this will not happen when the invoice.paid event is triggered we will get the period start but if not then maintain the continuity
        Instant newStart=periodStart!=null ? periodStart : subscription.getCurrentPeriodEnd();

        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus()==SubscriptionStatus.PAST_DUE || subscription.getStatus()==SubscriptionStatus.INCOMPLETE)
            subscription.setStatus(SubscriptionStatus.ACTIVE);

        subscriptionRepository.save(subscription);

    }

    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {

        Subscription subscription=
                subscriptionRepository.findByStripSubscriptionId(gatewaySubscriptionId).orElseThrow(()->new ResourceNotFoundException("Subscription",gatewaySubscriptionId));

        if(subscription.getStatus()==SubscriptionStatus.PAST_DUE){
            log.debug("Subscription is already set to past due :{}",gatewaySubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
        //Notify user via email

    }

    @Override
    public boolean canCreateNewProject() {

        Long userId= authUtil.getCurrentUserId();

        int countProjectOwned = projectMemberRepository.countProjectOwnedByUser(userId);

        SubscriptionResponse subscriptionResponse=getCurrentSubscription();

        //User is on Free Plan
        if(subscriptionResponse==null){
            if(countProjectOwned>=1)
                    return false;
            else
                    return true;
        }
        //User is on Pro/Buisness Plan
        return countProjectOwned<subscriptionResponse.getPlan().getMaxProjects();

    }

    @Override
    public PlanResponse getAllActivePlans() {
        SubscriptionResponse subscriptionResponse=getCurrentSubscription();

        if(subscriptionResponse==null)
        {
            //User is on Free Plan he did not subscript to any Plan
            Plan plan = planRepository.findByName("Free Plan");
            return subscriptionMapper.toPlanResponse(plan);
        }

        return subscriptionResponse.getPlan();
    }
}
