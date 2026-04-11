package com.devtrack.repository;

import com.devtrack.model.CodingSession;
import com.devtrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<CodingSession, Long> {
    
    // User-scoped queries
    List<CodingSession> findByUser(User user);
    
    List<CodingSession> findByUserOrderBySessionDateDesc(User user);
    
    long countByUser(User user);
    
    // Analytics queries
    @Query("SELECT COALESCE(SUM(s.durationMinutes), 0) FROM CodingSession s WHERE s.user = :user")
    Long sumTotalDurationMinutesByUser(@Param("user") User user);
    
    @Query("SELECT s.projectName, SUM(s.durationMinutes) FROM CodingSession s " +
           "WHERE s.user = :user GROUP BY s.projectName ORDER BY SUM(s.durationMinutes) DESC")
    List<Object[]> getTimeByProject(@Param("user") User user);
    
    @Query("SELECT s.workType, SUM(s.durationMinutes) FROM CodingSession s " +
           "WHERE s.user = :user AND s.workType IS NOT NULL GROUP BY s.workType")
    List<Object[]> getTimeByWorkType(@Param("user") User user);
    
    @Query("SELECT s FROM CodingSession s WHERE s.user = :user " +
           "AND s.sessionDate >= :startDate ORDER BY s.sessionDate ASC")
    List<CodingSession> findSessionsAfterDate(@Param("user") User user, @Param("startDate") LocalDate startDate);
}
