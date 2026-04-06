package com.devtrackpro.controller;

import com.devtrackpro.dto.DashboardResponseDTO;
import com.devtrackpro.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public DashboardResponseDTO getDashboard() {
        return analyticsService.getDashboardStats();
    }
}