package com.devtrack.dto;

import java.util.Map;

public class AnalyticsDTO {
    
    // Success metrics
    private Double successRate; // Percentage of tasks completed
    private Integer currentStreak; // Days in a row with coding sessions
    private Integer longestStreak;
    
    // Time distribution
    private Map<String, Integer> timeByCategory; // Minutes per category
    private Map<String, Integer> timeByProject; // Minutes per project
    private Map<String, Integer> timeByWorkType; // Minutes per work type
    
    // Focus metrics
    private String topProject; // Most time spent
    private String topCategory; // Most tasks in this category
    
    // Estimation accuracy
    private Double estimationAccuracy; // How accurate are time estimates (%)
    private Integer averageEstimationError; // Average difference in minutes

    public AnalyticsDTO() {
    }

    // Getters and Setters
    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Map<String, Integer> getTimeByCategory() {
        return timeByCategory;
    }

    public void setTimeByCategory(Map<String, Integer> timeByCategory) {
        this.timeByCategory = timeByCategory;
    }

    public Map<String, Integer> getTimeByProject() {
        return timeByProject;
    }

    public void setTimeByProject(Map<String, Integer> timeByProject) {
        this.timeByProject = timeByProject;
    }

    public Map<String, Integer> getTimeByWorkType() {
        return timeByWorkType;
    }

    public void setTimeByWorkType(Map<String, Integer> timeByWorkType) {
        this.timeByWorkType = timeByWorkType;
    }

    public String getTopProject() {
        return topProject;
    }

    public void setTopProject(String topProject) {
        this.topProject = topProject;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public Double getEstimationAccuracy() {
        return estimationAccuracy;
    }

    public void setEstimationAccuracy(Double estimationAccuracy) {
        this.estimationAccuracy = estimationAccuracy;
    }

    public Integer getAverageEstimationError() {
        return averageEstimationError;
    }

    public void setAverageEstimationError(Integer averageEstimationError) {
        this.averageEstimationError = averageEstimationError;
    }
}
