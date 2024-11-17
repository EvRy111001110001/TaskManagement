package com.example.TaskManagement;


import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.model.TaskRequestDTO;
import com.example.TaskManagement.repositories.RedisTaskRoleRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import com.example.TaskManagement.services.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskManagementServiceTaskTests {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final RedisTaskRoleRepository redisTaskRoleRepository = Mockito.mock(RedisTaskRoleRepository.class);
    private final TaskService taskService = new TaskService(taskRepository, userRepository, redisTaskRoleRepository);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateTask_Success() {

        TaskRequestDTO taskRequestDto = new TaskRequestDTO();
        taskRequestDto.setAuthorName("authorName");
        taskRequestDto.setExecutorName("executorName");

        User author = new User();
        author.setId(1L);
        author.setUsername("authorName");
        userRepository.save(author);

        User executor = new User();
        executor.setId(2L);
        executor.setUsername("executorName");

        Task savedTask = new Task();
        savedTask.setId(10L);
        when(userRepository.findByUsername("authorName")).thenReturn(Optional.of(author));
        when(userRepository.findByUsername("executorName")).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);


        taskService.create(taskRequestDto);


        verify(userRepository).findByUsername("authorName");
        verify(userRepository).findByUsername("executorName");
        verify(taskRepository).save(any(Task.class));
        verify(redisTaskRoleRepository).addTask(savedTask.getId(), author.getId(), executor.getId());

    }

    @Test
    public void testCreateTask_UserNotFound() {

        TaskRequestDTO taskRequestDto = new TaskRequestDTO();
        taskRequestDto.setAuthorName("authorName");

        when(userRepository.findByUsername("authorName")).thenReturn(Optional.empty());


        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            taskService.create(taskRequestDto);
        });

        assertEquals("User not found", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
        verify(redisTaskRoleRepository, never()).addTask(anyLong(), anyLong(), any());
    }

    @Test
    public void testCreateTask_ExecutorNotFound() {

        TaskRequestDTO taskRequestDto = new TaskRequestDTO();
        taskRequestDto.setAuthorName("authorName");
        taskRequestDto.setExecutorName("nonExistentExecutor");

        User author = new User();
        author.setId(1L);
        author.setUsername("authorName");

        Task savedTask = new Task();
        savedTask.setId(10L);

        when(userRepository.findByUsername("authorName")).thenReturn(Optional.of(author));
        when(userRepository.findByUsername("nonExistentExecutor")).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);


        taskService.create(taskRequestDto);


        verify(userRepository).findByUsername("authorName");
        verify(userRepository).findByUsername("nonExistentExecutor");
        verify(taskRepository).save(any(Task.class));
        verify(redisTaskRoleRepository).addTask(savedTask.getId(), author.getId(), null);
    }
}