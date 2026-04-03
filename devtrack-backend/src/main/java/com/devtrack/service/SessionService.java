package com.devtrack.service;

import com.devtrack.dto.SessionDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.CodingSession;
import com.devtrack.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * Get all sessions
     */
    public List<SessionDTO> getAllSessions() {
        return sessionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get session by ID
     */
    public SessionDTO getSessionById(Long id) {
        CodingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));
        return convertToDTO(session);
    }

    /**
     * Create new coding session
     */
    public SessionDTO createSession(SessionDTO sessionDTO) {
        // Validation
        if (sessionDTO.getProjectName() == null || sessionDTO.getProjectName().trim().isEmpty()) {
            throw new ValidationException("Project name cannot be empty");
        }
        
        if (sessionDTO.getStartTime() == null) {
            throw new ValidationException("Start time is required");
        }
        
        if (sessionDTO.getEndTime() == null) {
            throw new ValidationException("End time is required");
        }
        
        // Validate end time is after start time
        if (sessionDTO.getEndTime().isBefore(sessionDTO.getStartTime())) {
            throw new ValidationException("End time must be after start time");
        }
        
        CodingSession session = new CodingSession();
        session.setProjectName(sessionDTO.getProjectName());
        session.setStartTime(sessionDTO.getStartTime());
        session.setEndTime(sessionDTO.getEndTime());
        // Duration is calculated automatically in @PrePersist
        
        CodingSession savedSession = sessionRepository.save(session);
        return convertToDTO(savedSession);
    }

    /**
     * Delete session
     */
    public void deleteSession(Long id) {
        CodingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id));
        sessionRepository.delete(session);
    }

    /**
     * Get total coding hours
     */
    public double getTotalCodingHours() {
        Long totalMinutes = sessionRepository.sumTotalDurationMinutes();
        return totalMinutes / 60.0; // Convert minutes to hours
    }

    /**
     * Get total sessions count
     */
    public long getTotalSessionsCount() {
        return sessionRepository.count();
    }

    /**
     * Convert Entity to DTO
     */
    private SessionDTO convertToDTO(CodingSession session) {
        return new SessionDTO(
            session.getId(),
            session.getProjectName(),
            session.getStartTime(),
            session.getEndTime(),
            session.getDurationMinutes(),
            session.getCreatedAt()
        );
    }
}
