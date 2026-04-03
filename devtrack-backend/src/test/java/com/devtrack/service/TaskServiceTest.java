package com.devtrack.service;

import com.devtrack.dto.TaskDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.Task;
import com.devtrack.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit tests for TaskService
 * Tests business logic and repository interactions
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task("Build API", "Create REST endpoints", "PENDING");
        testTask.setId(1L);
        testTask.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Test getAllTasks - Should return list of TaskDTOs
     */
    @Test
    void shouldGetAllTasks() {
        List<Task> tasks = Arrays.asList(testTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDTO> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Build API", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    /**
     * Test getTaskById - Should return TaskDTO when task exists
     */
    @Test
    void shouldGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Build API", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    /**
     * Test getTaskById - Should throw ResourceNotFoundException when task doesn't exist
     */
    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });
    }

    /**
     * Test createTask - Should save and return TaskDTO
     */
    @Test
    void shouldCreateTask() {
        TaskDTO newTaskDTO = new TaskDTO(null, "New Task", "Description", "PENDING", null);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDTO result = taskService.createTask(newTaskDTO);

        assertNotNull(result);
        assertEquals("Build API", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Test createTask - Should throw ValidationException when title is empty
     */
    @Test
    void shouldThrowValidationExceptionWhenTitleEmpty() {
        TaskDTO invalidTask = new TaskDTO(null, "", "Description", "PENDING", null);

        assertThrows(ValidationException.class, () -> {
            taskService.createTask(invalidTask);
        });
    }

    /**
     * Test deleteTask - Should delete task when it exists
     */
    @Test
    void shouldDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(testTask);
    }

    /**
     * Test updateTaskStatus - Should update status to DONE
     */
    @Test
    void shouldUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDTO result = taskService.updateTaskStatus(1L, "DONE");

        assertEquals("DONE", result.getStatus());
        verify(taskRepository, times(1)).save(testTask);
    }

    /**
     * Test updateTaskStatus - Should throw ValidationException for invalid status
     */
    @Test
    void shouldThrowExceptionForInvalidStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        assertThrows(ValidationException.class, () -> {
            taskService.updateTaskStatus(1L, "INVALID");
        });
    }

    /**
     * Test getCompletedTasksCount - Should return count of DONE tasks
     */
    @Test
    void shouldGetCompletedTasksCount() {
        when(taskRepository.countByStatus("DONE")).thenReturn(5L);

        long count = taskService.getCompletedTasksCount();

        assertEquals(5L, count);
        verify(taskRepository, times(1)).countByStatus("DONE");
    }

    /**
     * Test getTotalTasksCount - Should return total count
     */
    @Test
    void shouldGetTotalTasksCount() {
        when(taskRepository.count()).thenReturn(10L);

        long count = taskService.getTotalTasksCount();

        assertEquals(10L, count);
        verify(taskRepository, times(1)).count();
    }
}
