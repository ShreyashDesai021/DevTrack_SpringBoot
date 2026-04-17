package com.devtrackpro.service;

import com.devtrackpro.dto.DashboardResponseDTO;
import com.devtrackpro.repository.FocusSessionRepository;
import com.devtrackpro.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private FocusSessionRepository focusSessionRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void shouldReturnDashboardStats() {
        when(taskRepository.count()).thenReturn(5L);
        when(taskRepository.countByStatus("DONE")).thenReturn(2L);
        when(focusSessionRepository.count()).thenReturn(3L);
        when(focusSessionRepository.getTotalFocusMinutes()).thenReturn(420);

        DashboardResponseDTO dto = analyticsService.getDashboardStats();

        assertEquals(5, dto.getTotalTasks());
        assertEquals(2, dto.getCompletedTasks());
        assertEquals(3, dto.getTotalFocusSessions());
        assertEquals(420, dto.getTotalFocusMinutes());
    }
}