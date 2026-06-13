package com.example.Lovable.service.Impl;

import com.example.Lovable.dto.subscription.PlanResponse;
import com.example.Lovable.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
