package com.devtrack.service;

import com.devtrack.dto.SessionDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.CodingSession;
import com.devtrack.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit tests for SessionService
 */
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    private CodingSession testSession;

    @BeforeEach
    void setUp() {
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 4, 1, 12, 0);
        testSession = new CodingSession("DevTrack", startTime, endTime);
        testSession.setId(1L);
        testSession.setDurationMinutes(120); // 2 hours
        testSession.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Test getAllSessions - Should return list of SessionDTOs
     */
    @Test
    void shouldGetAllSessions() {
        List<CodingSession> sessions = Arrays.asList(testSession);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<SessionDTO> result = sessionService.getAllSessions();

        assertEquals(1, result.size());
        assertEquals("DevTrack", result.get(0).getProjectName());
        assertEquals(120, result.get(0).getDurationMinutes());
        verify(sessionRepository, times(1)).findAll();
    }

    /**
     * Test getSessionById - Should return SessionDTO when session exists
     */
    @Test
    void shouldGetSessionById() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        SessionDTO result = sessionService.getSessionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("DevTrack", result.getProjectName());
        verify(sessionRepository, times(1)).findById(1L);
    }

    /**
     * Test getSessionById - Should throw ResourceNotFoundException when session doesn't exist
     */
    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sessionService.getSessionById(999L);
        });
    }

    /**
     * Test createSession - Should save and return SessionDTO
     */
    @Test
    void shouldCreateSession() {
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 4, 1, 12, 0);
        SessionDTO newSessionDTO = new SessionDTO(null, "New Project", startTime, endTime, null, null);
        
        when(sessionRepository.save(any(CodingSession.class))).thenReturn(testSession);

        SessionDTO result = sessionService.createSession(newSessionDTO);

        assertNotNull(result);
        assertEquals("DevTrack", result.getProjectName());
        verify(sessionRepository, times(1)).save(any(CodingSession.class));
    }

    /**
     * Test createSession - Should throw ValidationException when project name is empty
     */
    @Test
    void shouldThrowValidationExceptionWhenProjectNameEmpty() {
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 4, 1, 12, 0);
        SessionDTO invalidSession = new SessionDTO(null, "", startTime, endTime, null, null);

        assertThrows(ValidationException.class, () -> {
            sessionService.createSession(invalidSession);
        });
    }

    /**
     * Test createSession - Should throw ValidationException when end time is before start time
     */
    @Test
    void shouldThrowValidationExceptionWhenEndTimeBeforeStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 1, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 4, 1, 10, 0); // Before start time
        SessionDTO invalidSession = new SessionDTO(null, "Project", startTime, endTime, null, null);

        assertThrows(ValidationException.class, () -> {
            sessionService.createSession(invalidSession);
        });
    }

    /**
     * Test deleteSession - Should delete session when it exists
     */
    @Test
    void shouldDeleteSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        sessionService.deleteSession(1L);

        verify(sessionRepository, times(1)).delete(testSession);
    }

    /**
     * Test getTotalCodingHours - Should calculate hours from minutes
     */
    @Test
    void shouldGetTotalCodingHours() {
        when(sessionRepository.sumTotalDurationMinutes()).thenReturn(240L); // 4 hours

        double hours = sessionService.getTotalCodingHours();

        assertEquals(4.0, hours);
        verify(sessionRepository, times(1)).sumTotalDurationMinutes();
    }

    /**
     * Test getTotalSessionsCount - Should return total count
     */
    @Test
    void shouldGetTotalSessionsCount() {
        when(sessionRepository.count()).thenReturn(5L);

        long count = sessionService.getTotalSessionsCount();

        assertEquals(5L, count);
        verify(sessionRepository, times(1)).count();
    }
}
