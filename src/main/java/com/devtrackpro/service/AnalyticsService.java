package com.devtrackpro.service;

import com.devtrackpro.dto.DashboardResponseDTO;
import com.devtrackpro.repository.FocusSessionRepository;
import com.devtrackpro.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TaskRepository taskRepository;
    private final FocusSessionRepository focusSessionRepository;

    public DashboardResponseDTO getDashboardStats() {
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatus("DONE");
        long totalSessions = focusSessionRepository.count();
        int totalMinutes = focusSessionRepository.getTotalFocusMinutes();

        return new DashboardResponseDTO(
                totalTasks,
                completedTasks,
                totalSessions,
                totalMinutes
        );
    }
}