package com.devtrack.controller;

import com.devtrack.dto.SessionDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.service.SessionService;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for SessionController
 */
@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    private SessionDTO testSession;

    @BeforeEach
    void setUp() {
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 4, 1, 12, 30);
        testSession = new SessionDTO(1L, "DevTrack Backend", startTime, endTime, 150, LocalDateTime.now());
    }

    /**
     * Test GET /api/sessions - Should return all sessions
     */
    @Test
    void shouldGetAllSessions() throws Exception {
        List<SessionDTO> sessions = Arrays.asList(testSession);
        when(sessionService.getAllSessions()).thenReturn(sessions);

        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectName").value("DevTrack Backend"))
                .andExpect(jsonPath("$[0].durationMinutes").value(150));
    }

    /**
     * Test GET /api/sessions/{id} - Should return session by ID
     */
    @Test
    void shouldGetSessionById() throws Exception {
        when(sessionService.getSessionById(1L)).thenReturn(testSession);

        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.projectName").value("DevTrack Backend"));
    }

    /**
     * Test GET /api/sessions/{id} - Should return 404 when session not found
     */
    @Test
    void shouldReturn404WhenSessionNotFound() throws Exception {
        when(sessionService.getSessionById(999L))
                .thenThrow(new ResourceNotFoundException("Session", 999L));

        mockMvc.perform(get("/api/sessions/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /**
     * Test POST /api/sessions - Should create session
     */
    @Test
    void shouldCreateSession() throws Exception {
        when(sessionService.createSession(any(SessionDTO.class))).thenReturn(testSession);

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSession)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.durationMinutes").value(150));
    }

    /**
     * Test DELETE /api/sessions/{id} - Should delete session
     */
    @Test
    void shouldDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test DELETE /api/sessions/{id} - Should return 404 when deleting non-existent session
     */
    @Test
    void shouldReturn404WhenDeletingNonExistentSession() throws Exception {
        doThrow(new ResourceNotFoundException("Session", 999L))
                .when(sessionService).deleteSession(999L);

        mockMvc.perform(delete("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }
}
