package com.devtrack.controller;

import com.devtrack.dto.SessionDTO;
import com.devtrack.repository.UserRepository;
import com.devtrack.security.JwtUtil;
import com.devtrack.service.SessionService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@Import(TestSecurityConfig.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private JwtUtil jwtUtil;                  // ← ADDED

    @MockBean
    private UserRepository userRepository;    // ← ADDED

    @Autowired
    private ObjectMapper objectMapper;

    private SessionDTO testSession;

    @BeforeEach
    void setUp() {
        testSession = new SessionDTO(1L, "DevTrack Backend",
                "Implemented JWT authentication", 180, LocalDate.now(),
                "Feature", "Completed", "Hard", "Spring Security,JWT,BCrypt",
                LocalDateTime.now(), 1L);
    }                                          // ← FIXED: closing brace added here

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldGetAllSessions() throws Exception {
        List<SessionDTO> sessions = Arrays.asList(testSession);
        when(sessionService.getAllSessions("ann@example.com")).thenReturn(sessions);

        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectName").value("DevTrack Backend"))
                .andExpect(jsonPath("$[0].workType").value("Feature"))
                .andExpect(jsonPath("$[0].durationMinutes").value(180))
                .andExpect(jsonPath("$[0].tags").value("Spring Security,JWT,BCrypt"));
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldGetSessionById() throws Exception {
        when(sessionService.getSessionById(1L, "ann@example.com")).thenReturn(testSession);

        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.projectName").value("DevTrack Backend"))
                .andExpect(jsonPath("$.difficulty").value("Hard"));
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldCreateSession() throws Exception {
        SessionDTO newSession = new SessionDTO();
        newSession.setProjectName("New Project");
        newSession.setSummary("Built feature X");
        newSession.setDurationMinutes(90);
        newSession.setSessionDate(LocalDate.now());
        newSession.setWorkType("Feature");
        newSession.setOutcome("In Progress");

        when(sessionService.createSession(any(SessionDTO.class), eq("ann@example.com")))
                .thenReturn(testSession);

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSession)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "ann@example.com")
    void shouldDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isNoContent());
    }
}