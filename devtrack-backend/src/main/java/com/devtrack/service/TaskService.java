package com.devtrack.service;

import com.devtrack.dto.TaskDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.Task;
import com.devtrack.model.User;
import com.devtrack.repository.TaskRepository;
import com.devtrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all tasks for a user
     */
    public List<TaskDTO> getAllTasks(String userEmail) {
        User user = getUserByEmail(userEmail);
        return taskRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get task by ID (user-scoped)
     */
    public TaskDTO getTaskById(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        
        // Verify task belongs to user
        if (!task.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task", id);
        }
        
        return convertToDTO(task);
    }

    /**
     * Create new task
     */
    public TaskDTO createTask(TaskDTO taskDTO, String userEmail) {
        User user = getUserByEmail(userEmail);
        
        // Validation
        if (taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            throw new ValidationException("Task title cannot be empty");
        }
        
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : "PENDING");
        task.setPriority(taskDTO.getPriority() != null ? taskDTO.getPriority() : "MEDIUM");
        task.setCategory(taskDTO.getCategory());
        task.setEstimatedMinutes(taskDTO.getEstimatedMinutes());
        task.setActualMinutes(taskDTO.getActualMinutes());
        task.setUser(user);
        
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    /**
     * Update existing task
     */
    public TaskDTO updateTask(Long id, TaskDTO taskDTO, String userEmail) {
        User user = getUserByEmail(userEmail);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        
        // Verify task belongs to user
        if (!task.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task", id);
        }
        
        // Update fields
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
            // If marking as DONE, set completedAt
            if (taskDTO.getStatus().equals("DONE") && task.getCompletedAt() == null) {
                task.setCompletedAt(LocalDateTime.now());
            }
        }
        if (taskDTO.getPriority() != null) {
            task.setPriority(taskDTO.getPriority());
        }
        if (taskDTO.getCategory() != null) {
            task.setCategory(taskDTO.getCategory());
        }
        if (taskDTO.getEstimatedMinutes() != null) {
            task.setEstimatedMinutes(taskDTO.getEstimatedMinutes());
        }
        if (taskDTO.getActualMinutes() != null) {
            task.setActualMinutes(taskDTO.getActualMinutes());
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    /**
     * Delete task
     */
    public void deleteTask(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        
        // Verify task belongs to user
        if (!task.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Task", id);
        }
        
        taskRepository.delete(task);
    }

    /**
     * Get completed tasks count for user
     */
    public long getCompletedTasksCount(String userEmail) {
        User user = getUserByEmail(userEmail);
        return taskRepository.countByUserAndStatus(user, "DONE");
    }

    /**
     * Get total tasks count for user
     */
    public long getTotalTasksCount(String userEmail) {
        User user = getUserByEmail(userEmail);
        return taskRepository.countByUser(user);
    }

    /**
     * Convert Entity to DTO
     */
    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getStatus(),
            task.getPriority(),
            task.getCategory(),
            task.getEstimatedMinutes(),
            task.getActualMinutes(),
            task.getCompletedAt(),
            task.getCreatedAt(),
            task.getUser().getId()
        );
    }

    /**
     * Helper to get user by email
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
