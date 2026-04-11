package com.devtrack.service;

import com.devtrack.dto.TaskDTO;
import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.model.Task;
import com.devtrack.model.User;
import com.devtrack.repository.TaskRepository;
import com.devtrack.repository.UserRepository;
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
 * Tests user scoping and metadata handling
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = new User("Ann", "ann@example.com", "password");
        testUser.setId(1L);

        testTask = new Task("Build REST API", "PENDING", "HIGH");
        testTask.setId(1L);
        testTask.setUser(testUser);
        testTask.setCategory("Backend");
        testTask.setEstimatedMinutes(120);
        testTask.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Test getAllTasks returns only user's tasks
     */
    @Test
    void shouldGetAllTasksForUser() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByUser(testUser)).thenReturn(Arrays.asList(testTask));

        List<TaskDTO> tasks = taskService.getAllTasks("ann@example.com");

        assertEquals(1, tasks.size());
        assertEquals("Build REST API", tasks.get(0).getTitle());
        assertEquals("Backend", tasks.get(0).getCategory());
        verify(taskRepository, times(1)).findByUser(testUser);
    }

    /**
     * Test createTask associates task with user
     */
    @Test
    void shouldCreateTaskForUser() {
        TaskDTO newTaskDTO = new TaskDTO();
        newTaskDTO.setTitle("New Task");
        newTaskDTO.setCategory("Frontend");
        newTaskDTO.setEstimatedMinutes(60);

        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDTO result = taskService.createTask(newTaskDTO, "ann@example.com");

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Test getTaskById validates user ownership
     */
    @Test
    void shouldGetTaskByIdForCorrectUser() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        TaskDTO result = taskService.getTaskById(1L, "ann@example.com");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Build REST API", result.getTitle());
    }

    /**
     * Test user cannot access another user's task
     */
    @Test
    void shouldNotAllowAccessToOtherUserTask() {
        User otherUser = new User("Other", "other@example.com", "pass");
        otherUser.setId(2L);

        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // testTask belongs to testUser (id=1), but otherUser (id=2) is trying to access it
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(1L, "other@example.com");
        });
    }

    /**
     * Test marking task as DONE sets completedAt
     */
    @Test
    void shouldSetCompletedAtWhenMarkingDone() {
        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setStatus("DONE");

        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        taskService.updateTask(1L, updateDTO, "ann@example.com");

        assertEquals("DONE", testTask.getStatus());
        assertNotNull(testTask.getCompletedAt());
        verify(taskRepository, times(1)).save(testTask);
    }

    /**
     * Test getCompletedTasksCount is user-scoped
     */
    @Test
    void shouldGetCompletedTasksCountForUser() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.countByUserAndStatus(testUser, "DONE")).thenReturn(5L);

        long count = taskService.getCompletedTasksCount("ann@example.com");

        assertEquals(5L, count);
        verify(taskRepository, times(1)).countByUserAndStatus(testUser, "DONE");
    }
}
