package com.devtrackpro.repository;

import com.devtrackpro.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByStatus(String status);
}