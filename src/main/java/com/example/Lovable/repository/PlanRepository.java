package com.example.Lovable.repository;

import com.example.Lovable.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {
    Optional<Plan> findByStripeId(String id);

    Plan findByName(String freePlan);
}
