package com.example.Lovable.mapper;

import com.example.Lovable.dto.subscription.PlanResponse;
import com.example.Lovable.dto.subscription.SubscriptionResponse;
import com.example.Lovable.entity.Plan;
import com.example.Lovable.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {


    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
