package com.devtrack.controller;

import com.devtrack.dto.AnalyticsDTO;
import com.devtrack.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Analytics Controller
 * Provides derived analytics and statistics
 * Requires JWT authentication
 */
@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * GET /api/analytics - Get complete analytics for authenticated user
     */
    @GetMapping
    public ResponseEntity<AnalyticsDTO> getAnalytics(Authentication authentication) {
        String userEmail = authentication.getName();
        AnalyticsDTO analytics = analyticsService.getAnalytics(userEmail);
        return ResponseEntity.ok(analytics);
    }
}
