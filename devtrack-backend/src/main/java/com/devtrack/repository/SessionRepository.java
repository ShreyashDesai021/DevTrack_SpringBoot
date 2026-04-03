package com.devtrack.repository;

import com.devtrack.model.CodingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<CodingSession, Long> {
    
    // Custom query to sum all durations (returns total minutes)
    @Query("SELECT COALESCE(SUM(s.durationMinutes), 0) FROM CodingSession s")
    Long sumTotalDurationMinutes();
}
