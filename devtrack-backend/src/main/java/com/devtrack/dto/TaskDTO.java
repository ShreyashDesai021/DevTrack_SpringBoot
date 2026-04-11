package com.devtrack.dto;

import java.time.LocalDateTime;

public class TaskDTO {
    private Long id;
    
    // Core fields
    private String title;
    private String status;
    private String priority;
    
    // Metadata fields
    private String category;
    private Integer estimatedMinutes;
    private Integer actualMinutes;
    private LocalDateTime completedAt;
    
    private LocalDateTime createdAt;
    private Long userId;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String status, String priority, 
                   String category, Integer estimatedMinutes, Integer actualMinutes,
                   LocalDateTime completedAt, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.estimatedMinutes = estimatedMinutes;
        this.actualMinutes = actualMinutes;
        this.completedAt = completedAt;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public Integer getActualMinutes() {
        return actualMinutes;
    }

    public void setActualMinutes(Integer actualMinutes) {
        this.actualMinutes = actualMinutes;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
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
