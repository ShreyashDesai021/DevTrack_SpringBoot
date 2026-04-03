package com.devtrack.controller;

import com.devtrack.dto.TaskDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for TaskController
 * Tests all REST endpoints and exception handling
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDTO testTask;

    @BeforeEach
    void setUp() {
        testTask = new TaskDTO(1L, "Build REST API", "Create Spring Boot backend", 
                              "PENDING", LocalDateTime.now());
    }

    /**
     * Test GET /api/tasks - Should return all tasks
     */
    @Test
    void shouldGetAllTasks() throws Exception {
        List<TaskDTO> tasks = Arrays.asList(testTask);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Build REST API"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    /**
     * Test GET /api/tasks/{id} - Should return task by ID
     */
    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(testTask);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Build REST API"));
    }

    /**
     * Test GET /api/tasks/{id} - Should return 404 when task not found
     */
    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.getTaskById(999L))
                .thenThrow(new ResourceNotFoundException("Task", 999L));

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /**
     * Test POST /api/tasks - Should create task
     */
    @Test
    void shouldCreateTask() throws Exception {
        TaskDTO newTask = new TaskDTO(null, "New Task", "Description", "PENDING", null);
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Build REST API"));
    }

    /**
     * Test PUT /api/tasks/{id} - Should update task
     */
    @Test
    void shouldUpdateTask() throws Exception {
        TaskDTO updatedTask = new TaskDTO(1L, "Updated Task", "Updated description", 
                                         "DONE", LocalDateTime.now());
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    /**
     * Test DELETE /api/tasks/{id} - Should delete task
     */
    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test DELETE /api/tasks/{id} - Should return 404 when deleting non-existent task
     */
    @Test
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        doThrow(new ResourceNotFoundException("Task", 999L))
                .when(taskService).deleteTask(999L);

        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test PATCH /api/tasks/{id}/status - Should update task status
     */
    @Test
    void shouldUpdateTaskStatus() throws Exception {
        TaskDTO completedTask = new TaskDTO(1L, "Build REST API", "Create Spring Boot backend", 
                                           "DONE", LocalDateTime.now());
        when(taskService.updateTaskStatus(1L, "DONE")).thenReturn(completedTask);

        mockMvc.perform(patch("/api/tasks/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }
}
