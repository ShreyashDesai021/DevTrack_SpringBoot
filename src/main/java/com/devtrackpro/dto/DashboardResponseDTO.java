package com.devtrackpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponseDTO {
    private long totalTasks;
    private long completedTasks;
    private long totalFocusSessions;
    private int totalFocusMinutes;
}