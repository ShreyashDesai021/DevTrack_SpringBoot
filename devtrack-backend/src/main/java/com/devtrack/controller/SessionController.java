package com.devtrack.controller;

import com.devtrack.dto.SessionDTO;
import com.devtrack.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    /**
     * GET /api/sessions - Get all sessions
     */
    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        List<SessionDTO> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    /**
     * GET /api/sessions/{id} - Get session by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        SessionDTO session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    /**
     * POST /api/sessions - Create new session
     */
    @PostMapping
    public ResponseEntity<SessionDTO> createSession(@Valid @RequestBody SessionDTO sessionDTO) {
        SessionDTO createdSession = sessionService.createSession(sessionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    /**
     * DELETE /api/sessions/{id} - Delete session
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
