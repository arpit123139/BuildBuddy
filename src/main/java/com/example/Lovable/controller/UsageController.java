package com.example.Lovable.controller;

import com.example.Lovable.dto.subscription.PlanLimitResponse;
import com.example.Lovable.dto.subscription.UsageTodayResponse;
import com.example.Lovable.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage(){
        Long userId=1l;
        return ResponseEntity.ok(usageService.getTodayUsage(userId));
    }

    @GetMapping("/limit")
    public ResponseEntity<PlanLimitResponse> getPlanLimits(){
        Long userId=1l;
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimitsOfUser(userId));
    }
}
