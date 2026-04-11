package com.devtrack.service;

import com.devtrack.dto.SessionDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.CodingSession;
import com.devtrack.model.User;
import com.devtrack.repository.SessionRepository;
import com.devtrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all sessions for a user
     */
    public List<SessionDTO> getAllSessions(String userEmail) {
        User user = getUserByEmail(userEmail);
        return sessionRepository.findByUserOrderBySessionDateDesc(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get session by ID (user-scoped)
     */
    public SessionDTO getSessionById(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        CodingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));
        
        // Verify session belongs to user
        if (!session.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Session", id);
        }
        
        return convertToDTO(session);
    }

    /**
     * Create new coding session
     */
    public SessionDTO createSession(SessionDTO sessionDTO, String userEmail) {
        User user = getUserByEmail(userEmail);
        
        // Validation
        if (sessionDTO.getProjectName() == null || sessionDTO.getProjectName().trim().isEmpty()) {
            throw new ValidationException("Project name cannot be empty");
        }
        
        if (sessionDTO.getDurationMinutes() == null || sessionDTO.getDurationMinutes() <= 0) {
            throw new ValidationException("Duration must be greater than 0");
        }
        
        if (sessionDTO.getSessionDate() == null) {
            throw new ValidationException("Session date is required");
        }
        
        CodingSession session = new CodingSession();
        session.setProjectName(sessionDTO.getProjectName());
        session.setSummary(sessionDTO.getSummary());
        session.setDurationMinutes(sessionDTO.getDurationMinutes());
        session.setSessionDate(sessionDTO.getSessionDate());
        session.setWorkType(sessionDTO.getWorkType());
        session.setOutcome(sessionDTO.getOutcome());
        session.setDifficulty(sessionDTO.getDifficulty());
        session.setTags(sessionDTO.getTags());
        session.setUser(user);
        
        CodingSession savedSession = sessionRepository.save(session);
        return convertToDTO(savedSession);
    }

    /**
     * Delete session
     */
    public void deleteSession(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        CodingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));
        
        // Verify session belongs to user
        if (!session.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Session", id);
        }
        
        sessionRepository.delete(session);
    }

    /**
     * Get total coding hours for user
     */
    public double getTotalCodingHours(String userEmail) {
        User user = getUserByEmail(userEmail);
        Long totalMinutes = sessionRepository.sumTotalDurationMinutesByUser(user);
        return totalMinutes / 60.0; // Convert minutes to hours
    }

    /**
     * Get total sessions count for user
     */
    public long getTotalSessionsCount(String userEmail) {
        User user = getUserByEmail(userEmail);
        return sessionRepository.countByUser(user);
    }

    /**
     * Convert Entity to DTO
     */
    private SessionDTO convertToDTO(CodingSession session) {
        return new SessionDTO(
            session.getId(),
            session.getProjectName(),
            session.getSummary(),
            session.getDurationMinutes(),
            session.getSessionDate(),
            session.getWorkType(),
            session.getOutcome(),
            session.getDifficulty(),
            session.getTags(),
            session.getCreatedAt(),
            session.getUser().getId()
        );
    }

    /**
     * Helper to get user by email
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
