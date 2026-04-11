package com.devtrack.service;

import com.devtrack.dto.SessionDTO;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.CodingSession;
import com.devtrack.model.User;
import com.devtrack.repository.SessionRepository;
import com.devtrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit tests for SessionService
 * Tests user scoping and metadata handling
 */
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private User testUser;
    private CodingSession testSession;

    @BeforeEach
    void setUp() {
        testUser = new User("Ann", "ann@example.com", "password");
        testUser.setId(1L);

        testSession = new CodingSession("DevTrack Backend", 180, LocalDate.now());
        testSession.setId(1L);
        testSession.setUser(testUser);
        testSession.setSummary("Implemented JWT authentication");
        testSession.setWorkType("Feature");
        testSession.setOutcome("Completed");
        testSession.setDifficulty("Hard");
        testSession.setTags("Spring Security,JWT,BCrypt");
    }

    /**
     * Test getAllSessions returns only user's sessions
     */
    @Test
    void shouldGetAllSessionsForUser() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(sessionRepository.findByUserOrderBySessionDateDesc(testUser))
                .thenReturn(Arrays.asList(testSession));

        List<SessionDTO> sessions = sessionService.getAllSessions("ann@example.com");

        assertEquals(1, sessions.size());
        assertEquals("DevTrack Backend", sessions.get(0).getProjectName());
        assertEquals("Feature", sessions.get(0).getWorkType());
        assertEquals("Spring Security,JWT,BCrypt", sessions.get(0).getTags());
        verify(sessionRepository, times(1)).findByUserOrderBySessionDateDesc(testUser);
    }

    /**
     * Test createSession validates required fields
     */
    @Test
    void shouldValidateProjectNameRequired() {
        SessionDTO invalidSession = new SessionDTO();
        invalidSession.setProjectName("");
        invalidSession.setDurationMinutes(60);
        invalidSession.setSessionDate(LocalDate.now());

        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(ValidationException.class, () -> {
            sessionService.createSession(invalidSession, "ann@example.com");
        });
    }

    /**
     * Test createSession validates duration is positive
     */
    @Test
    void shouldValidateDurationPositive() {
        SessionDTO invalidSession = new SessionDTO();
        invalidSession.setProjectName("Test Project");
        invalidSession.setDurationMinutes(0);
        invalidSession.setSessionDate(LocalDate.now());

        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(ValidationException.class, () -> {
            sessionService.createSession(invalidSession, "ann@example.com");
        });
    }

    /**
     * Test createSession with complete metadata
     */
    @Test
    void shouldCreateSessionWithMetadata() {
        SessionDTO newSession = new SessionDTO();
        newSession.setProjectName("DevTrack Backend");
        newSession.setSummary("Built analytics engine");
        newSession.setDurationMinutes(120);
        newSession.setSessionDate(LocalDate.now());
        newSession.setWorkType("Feature");
        newSession.setOutcome("Completed");
        newSession.setDifficulty("Medium");
        newSession.setTags("Analytics,SQL,Queries");

        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(sessionRepository.save(any(CodingSession.class))).thenReturn(testSession);

        SessionDTO result = sessionService.createSession(newSession, "ann@example.com");

        assertNotNull(result);
        verify(sessionRepository, times(1)).save(any(CodingSession.class));
    }

    /**
     * Test getTotalCodingHours converts minutes to hours
     */
    @Test
    void shouldConvertMinutesToHours() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(sessionRepository.sumTotalDurationMinutesByUser(testUser)).thenReturn(240L);

        double hours = sessionService.getTotalCodingHours("ann@example.com");

        assertEquals(4.0, hours);
        verify(sessionRepository, times(1)).sumTotalDurationMinutesByUser(testUser);
    }

    /**
     * Test user cannot delete another user's session
     */
    @Test
    void shouldNotAllowDeletingOtherUserSession() {
        User otherUser = new User("Other", "other@example.com", "pass");
        otherUser.setId(2L);

        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        assertThrows(Exception.class, () -> {
            sessionService.deleteSession(1L, "other@example.com");
        });
    }
}
