package com.devtrackpro.controller;

import com.devtrackpro.model.Task;
import com.devtrackpro.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void shouldGetAllTasks() throws Exception {
        Task task = Task.builder()
                .id(1L)
                .title("Test Task")
                .priority("HIGH")
                .status("TODO")
                .estimatedHours(5)
                .build();

        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = Task.builder()
                .id(1L)
                .title("Build Testing")
                .priority("HIGH")
                .status("TODO")
                .estimatedHours(5)
                .build();

        when(taskService.createTask(org.mockito.ArgumentMatchers.any(Task.class)))
                .thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "title":"Build Testing",
                              "priority":"HIGH",
                              "status":"TODO",
                              "estimatedHours":5
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Build Testing"));
    }
}