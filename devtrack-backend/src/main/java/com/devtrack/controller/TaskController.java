package com.devtrack.controller;

import com.devtrack.dto.TaskDTO;
import com.devtrack.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task Controller
 * All endpoints require JWT authentication
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * GET /api/tasks - Get all tasks for authenticated user
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TaskDTO> tasks = taskService.getAllTasks(userEmail);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/{id} - Get task by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        TaskDTO task = taskService.getTaskById(id, userEmail);
        return ResponseEntity.ok(task);
    }

    /**
     * POST /api/tasks - Create new task
     */
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Valid @RequestBody TaskDTO taskDTO,
            Authentication authentication) {
        String userEmail = authentication.getName();
        TaskDTO createdTask = taskService.createTask(taskDTO, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * PUT /api/tasks/{id} - Update task
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDTO taskDTO,
            Authentication authentication) {
        String userEmail = authentication.getName();
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO, userEmail);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * DELETE /api/tasks/{id} - Delete task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        taskService.deleteTask(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
