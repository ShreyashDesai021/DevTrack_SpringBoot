package com.devtrackpro.controller;

import com.devtrackpro.dto.DashboardResponseDTO;
import com.devtrackpro.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @Test
    void shouldReturnDashboardStats() throws Exception {
        DashboardResponseDTO dto =
                new DashboardResponseDTO(5, 2, 3, 420);

        when(analyticsService.getDashboardStats()).thenReturn(dto);

        mockMvc.perform(get("/api/analytics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(5))
                .andExpect(jsonPath("$.totalFocusMinutes").value(420));
    }
}