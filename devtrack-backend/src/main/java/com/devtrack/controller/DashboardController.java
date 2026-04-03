package com.devtrack.controller;

import com.devtrack.dto.DashboardStatsDTO;
import com.devtrack.service.SessionService;
import com.devtrack.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SessionService sessionService;

    /**
     * GET /api/dashboard/stats - Get dashboard statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        long totalTasks = taskService.getTotalTasksCount();
        long completedTasks = taskService.getCompletedTasksCount();
        double totalCodingHours = sessionService.getTotalCodingHours();
        long totalSessions = sessionService.getTotalSessionsCount();

        DashboardStatsDTO stats = new DashboardStatsDTO(
            totalTasks,
            completedTasks,
            totalCodingHours,
            totalSessions
        );

        return ResponseEntity.ok(stats);
    }
}
