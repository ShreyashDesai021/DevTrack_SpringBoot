package com.devtrack.repository;

import com.devtrack.model.Task;
import com.devtrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // User-scoped queries
    List<Task> findByUser(User user);
    
    List<Task> findByUserAndStatus(User user, String status);
    
    long countByUserAndStatus(User user, String status);
    
    long countByUser(User user);
    
    // Analytics queries
    @Query("SELECT t.category, COUNT(t) FROM Task t WHERE t.user = :user GROUP BY t.category")
    List<Object[]> countTasksByCategory(@Param("user") User user);
    
    @Query("SELECT AVG(ABS(t.estimatedMinutes - t.actualMinutes)) FROM Task t " +
           "WHERE t.user = :user AND t.estimatedMinutes IS NOT NULL AND t.actualMinutes IS NOT NULL")
    Double getAverageEstimationError(@Param("user") User user);
}
