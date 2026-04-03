package com.devtrack.repository;

import com.devtrack.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Custom query methods for statistics
    long countByStatus(String status);
}
