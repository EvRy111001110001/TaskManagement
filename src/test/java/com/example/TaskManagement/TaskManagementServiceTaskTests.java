package com.example.TaskManagement;


import com.example.TaskManagement.entity.PriorityTask;
import com.example.TaskManagement.entity.StatusTask;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.exception.TaskNotFoundException;
import com.example.TaskManagement.mappers.TaskMapper;
import com.example.TaskManagement.model.TaskResponseDTO;
import com.example.TaskManagement.securityRedis.RedisTaskRoleRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import com.example.TaskManagement.services.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskManagementServiceTaskTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RedisTaskRoleRepository redisTaskRoleRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskResponseDTO expectedDTO;
    private Task expectedEntity;

    @BeforeEach
    void setUp() {
        TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
        taskService = new TaskService(taskRepository, userRepository, redisTaskRoleRepository, taskMapper);
        expectedDTO = new TaskResponseDTO();
        expectedDTO.setTitle("title");
        expectedDTO.setText("text");
        expectedDTO.setStatus("WAITING");
        expectedDTO.setPriority("MEDIUM");
        expectedDTO.setAuthorName("authorName");
        expectedDTO.setExecutorName("executorName");

        User author = new User();
        author.setId(1L);
        author.setUsername("authorName");

        User executor = new User();
        executor.setId(2L);
        executor.setUsername("executorName");

        expectedEntity = new Task();
        expectedEntity.setId(1L);
        expectedEntity.setTitle("title");
        expectedEntity.setText("text");
        expectedEntity.setStatus(StatusTask.WAITING);
        expectedEntity.setPriority(PriorityTask.MEDIUM);
        expectedEntity.setAuthor(author);
        expectedEntity.setExecutor(executor);
    }

    @Test
    public void testGetTask_Success() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedEntity));

        TaskResponseDTO result = taskService.getById(1L);

        assertEquals(expectedDTO.getTitle(), result.getTitle());
        assertEquals(expectedDTO.getText(), result.getText());
        assertEquals(expectedDTO.getStatus(), result.getStatus());
        assertEquals(expectedDTO.getPriority(), result.getPriority());
        assertEquals(expectedDTO.getAuthorName(), result.getAuthorName());
        assertEquals(expectedDTO.getExecutorName(), result.getExecutorName());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    public void testGetTask_ExecutorNotFound() {
        expectedEntity.setExecutor(null);
        expectedDTO.setExecutorName("You have not assigned a task executor");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedEntity));

        TaskResponseDTO result = taskService.getById(1L);

        assertEquals(expectedDTO.getTitle(), result.getTitle());
        assertEquals(expectedDTO.getText(), result.getText());
        assertEquals(expectedDTO.getStatus(), result.getStatus());
        assertEquals(expectedDTO.getPriority(), result.getPriority());
        assertEquals(expectedDTO.getAuthorName(), result.getAuthorName());
        assertEquals(expectedDTO.getExecutorName(), result.getExecutorName());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any());
    }


    @Test
    public void testTaskNotFound() {

        Long taskId = 2L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getById(taskId);
        });

        assertEquals("Task with ID " + taskId + " not found", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
        verify(redisTaskRoleRepository, never()).addTask(anyLong(), anyLong(), any());
    }
}