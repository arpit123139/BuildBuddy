package com.example.Lovable.repository;

import com.example.Lovable.dto.subscription.SubscriptionResponse;
import com.example.Lovable.entity.Subscription;
import com.example.Lovable.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> active);

    boolean existsByStripSubscriptionId(String subscriptionId);

    Optional<Subscription> findByStripSubscriptionId(String gatewaySubscriptionId);
}
