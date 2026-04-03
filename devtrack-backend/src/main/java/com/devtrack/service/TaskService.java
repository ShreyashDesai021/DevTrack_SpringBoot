package com.devtrack.service;

import com.devtrack.dto.TaskDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.Task;
import com.devtrack.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Get all tasks
     */
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get task by ID
     */
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        return convertToDTO(task);
    }

    /**
     * Create new task
     */
    public TaskDTO createTask(TaskDTO taskDTO) {
        // Validation
        if (taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            throw new ValidationException("Task title cannot be empty");
        }
        
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : "PENDING");
        
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    /**
     * Update existing task
     */
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    /**
     * Delete task
     */
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        taskRepository.delete(task);
    }

    /**
     * Update task status
     */
    public TaskDTO updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        
        // Validate status
        if (!status.equals("PENDING") && !status.equals("DONE")) {
            throw new ValidationException("Status must be either PENDING or DONE");
        }
        
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    /**
     * Get count of completed tasks
     */
    public long getCompletedTasksCount() {
        return taskRepository.countByStatus("DONE");
    }

    /**
     * Get total tasks count
     */
    public long getTotalTasksCount() {
        return taskRepository.count();
    }

    /**
     * Convert Entity to DTO
     */
    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreatedAt()
        );
    }
}
