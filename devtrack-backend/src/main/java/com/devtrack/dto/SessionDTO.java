package com.devtrack.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SessionDTO {
    private Long id;
    
    // Core fields
    private String projectName;
    private String summary;
    private Integer durationMinutes;
    private LocalDate sessionDate;
    
    // Metadata fields
    private String workType;
    private String outcome;
    private String difficulty;
    private String tags;
    
    private LocalDateTime createdAt;
    private Long userId;

    public SessionDTO() {
    }

    public SessionDTO(Long id, String projectName, String summary, Integer durationMinutes,
                      LocalDate sessionDate, String workType, String outcome, String difficulty,
                      String tags, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.projectName = projectName;
        this.summary = summary;
        this.durationMinutes = durationMinutes;
        this.sessionDate = sessionDate;
        this.workType = workType;
        this.outcome = outcome;
        this.difficulty = difficulty;
        this.tags = tags;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
