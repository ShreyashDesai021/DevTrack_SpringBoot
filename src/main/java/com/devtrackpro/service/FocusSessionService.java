package com.devtrackpro.service;

import com.devtrackpro.model.FocusSession;
import com.devtrackpro.repository.FocusSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FocusSessionService {

    private final FocusSessionRepository repository;

    public FocusSession create(FocusSession session) {
        return repository.save(session);
    }

    public List<FocusSession> getAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}