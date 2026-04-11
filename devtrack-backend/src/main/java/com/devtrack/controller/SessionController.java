package com.devtrack.controller;

import com.devtrack.dto.SessionDTO;
import com.devtrack.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Session Controller
 * All endpoints require JWT authentication
 */
@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    /**
     * GET /api/sessions - Get all sessions for authenticated user
     */
    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions(Authentication authentication) {
        String userEmail = authentication.getName();
        List<SessionDTO> sessions = sessionService.getAllSessions(userEmail);
        return ResponseEntity.ok(sessions);
    }

    /**
     * GET /api/sessions/{id} - Get session by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        SessionDTO session = sessionService.getSessionById(id, userEmail);
        return ResponseEntity.ok(session);
    }

    /**
     * POST /api/sessions - Create new session
     */
    @PostMapping
    public ResponseEntity<SessionDTO> createSession(
            @Valid @RequestBody SessionDTO sessionDTO,
            Authentication authentication) {
        String userEmail = authentication.getName();
        SessionDTO createdSession = sessionService.createSession(sessionDTO, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    /**
     * DELETE /api/sessions/{id} - Delete session
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        sessionService.deleteSession(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
