package com.devtrackpro.controller;

import com.devtrackpro.model.FocusSession;
import com.devtrackpro.service.FocusSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FocusSessionController {

    private final FocusSessionService service;

    @PostMapping
    public FocusSession create(@RequestBody FocusSession session) {
        return service.create(session);
    }

    @GetMapping
    public List<FocusSession> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}