package com.devtrack.dto;

public class DashboardStatsDTO {
    private Long totalTasks;
    private Long completedTasks;
    private Double totalCodingHours;
    private Long totalSessions;

    // Constructors
    public DashboardStatsDTO() {
    }

    public DashboardStatsDTO(Long totalTasks, Long completedTasks, 
                             Double totalCodingHours, Long totalSessions) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.totalCodingHours = totalCodingHours;
        this.totalSessions = totalSessions;
    }

    // Getters and Setters
    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Double getTotalCodingHours() {
        return totalCodingHours;
    }

    public void setTotalCodingHours(Double totalCodingHours) {
        this.totalCodingHours = totalCodingHours;
    }

    public Long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Long totalSessions) {
        this.totalSessions = totalSessions;
    }
}
