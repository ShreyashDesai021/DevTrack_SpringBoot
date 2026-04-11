package com.devtrack.controller;

import com.devtrack.dto.TaskDTO;
import com.devtrack.repository.UserRepository;
import com.devtrack.security.JwtUtil;
import com.devtrack.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(TestSecurityConfig.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;    // ← FIXED: was UserDetailsService

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDTO testTask;

    @BeforeEach
    void setUp() {
        testTask = new TaskDTO(1L, "Build REST API", "PENDING", "HIGH",
                "Backend", 120, null, null, LocalDateTime.now(), 1L);
    }                                          // ← FIXED: closing brace added here

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldGetAllTasks() throws Exception {
        List<TaskDTO> tasks = Arrays.asList(testTask);
        when(taskService.getAllTasks("ann@example.com")).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Build REST API"))
                .andExpect(jsonPath("$[0].category").value("Backend"))
                .andExpect(jsonPath("$[0].estimatedMinutes").value(120));
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(1L, "ann@example.com")).thenReturn(testTask);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Build REST API"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldCreateTask() throws Exception {
        TaskDTO newTask = new TaskDTO();
        newTask.setTitle("New Task");
        newTask.setCategory("Frontend");
        newTask.setEstimatedMinutes(60);

        when(taskService.createTask(any(TaskDTO.class), eq("ann@example.com"))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldUpdateTask() throws Exception {
        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setStatus("DONE");
        updatedTask.setActualMinutes(100);

        when(taskService.updateTask(eq(1L), any(TaskDTO.class), eq("ann@example.com")))
                .thenReturn(testTask);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}