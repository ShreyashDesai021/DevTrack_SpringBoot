package com.devtrackpro.repository;

import com.devtrackpro.model.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {

    @Query("SELECT COALESCE(SUM(f.durationMinutes),0) FROM FocusSession f")
    Integer getTotalFocusMinutes();
}