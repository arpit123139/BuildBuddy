package com.example.Lovable.entity;

import com.example.Lovable.enums.SubscriptionStatus;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    private Long id;

    @ManyToOne
    private User user;

    private Plan plan;

    private String stripCustomerId;

    private String stripSubscriptionId;


    private LocalDateTime currentPeriodStart;
    private LocalDateTime currentPeriodEnd;
    private Boolean cancelAtPeriodEnd=false;

    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime updatedAt;

    private SubscriptionStatus status;

}
