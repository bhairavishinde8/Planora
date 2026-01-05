package com.planora.backend.controller;

import com.planora.backend.dto.AnalyticsSummary;
import com.planora.backend.dto.WeeklyProgress;
import com.planora.backend.model.User;
import com.planora.backend.repository.UserRepository;
import com.planora.backend.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepo;

    public AnalyticsController(AnalyticsService analyticsService,
                               UserRepository userRepo) {
        this.analyticsService = analyticsService;
        this.userRepo = userRepo;
    }

    @GetMapping("/summary")
    public AnalyticsSummary getSummary(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return analyticsService.getSummary(user);
    }

    @GetMapping("/weekly")
    public List<WeeklyProgress> getWeeklyProgress(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return analyticsService.getWeeklyProgress(user);
    }
}
