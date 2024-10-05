package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.*;
import com.example.TaskManagement.model.TaskRequestDTO;
import com.example.TaskManagement.model.TaskResponseDTO;
import com.example.TaskManagement.repositories.CommentRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import com.example.TaskManagement.repositories.RedisTaskRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaskService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RedisTaskRoleRepository redisTaskRoleRepository;

    public TaskResponseDTO getById(Long id) {
        log.info("Get by task id " + id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return toDto(task);
    }

    public void create(TaskRequestDTO taskRequestDto) {
        log.info("Create task");
        User author = userRepository.findByUsername(taskRequestDto.getAuthorName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Task task = toEntity(taskRequestDto);
        task.setAuthor(author); //явное указание автора
        setDefaultValue(task);

        Task savedTask = taskRepository.save(task);

        Long executorId = null;
        if (taskRequestDto.getExecutorName() != null) {
            executorId = userRepository.findByUsername(taskRequestDto.getExecutorName())
                    .map(User::getId)
                    .orElse(null);
        }

        redisTaskRoleRepository.addTask(savedTask.getId(), author.getId(), executorId);
    }

    public void update(TaskRequestDTO taskRequestDTO, Long taskId) {
        log.info("Update task");
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        toEntity(taskRequestDTO);

        taskRepository.save(task);
    }

    public void deleteById(Long id) {
        log.info("Delete by task id " + id);
        taskRepository.deleteById(id);
    }

    public void setDefaultValue(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(StatusTask.WAITING);
        }
        if (task.getPriority() == null) {
            task.setPriority(PriorityTask.MEDIUM);
        }
    }

    public void patchStatusTaskInProgress(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(StatusTask.IN_PROCESS);
        taskRepository.save(task);
    }

    public void patchStatusTaskCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(StatusTask.COMPLETED);
        taskRepository.save(task);
    }

    public void patchPriorityTaskLow(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setPriority(PriorityTask.LOW);
        taskRepository.save(task);
    }

    public void patchPriorityTaskHigh(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setPriority(PriorityTask.HIGH);
        taskRepository.save(task);
    }

    public void updateExecutor(Long taskId, String executorName) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        User executor = userRepository.findByUsername(executorName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        task.setExecutor(executor);
        taskRepository.save(task);
        redisTaskRoleRepository.updateExecutor(taskId, executor.getId());
    }

    public TaskResponseDTO toDto(Task task) {
        TaskResponseDTO taskResponseDto = modelMapper.map(task, TaskResponseDTO.class);

        if (task.getAuthor() != null) {
            String authorName = task.getAuthor().getUsername();
            taskResponseDto.setAuthorName(authorName);
        } else {
            taskResponseDto.setAuthorName("Unknown Author");
        }

        if (task.getExecutor() != null) {
            Optional<User> executor = userRepository.findUserById(task.getExecutor().getId());
            if (executor.isPresent()) {
                taskResponseDto.setExecutorName(task.getExecutor().getUsername());
            } else {
                taskResponseDto.setExecutorName("Executor not found");
            }
        } else {
            taskResponseDto.setExecutorName("You have not assigned a task executor");
        }
        return taskResponseDto;
    }

    public Task toEntity(TaskRequestDTO taskRequestDto) {
        return modelMapper.map(taskRequestDto, Task.class);
    }

    public List<TaskResponseDTO> getAllTasksAuthor(String username, int page, int size) {
        log.info("Get all tasks for author");
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Page<Task> taskPage = taskRepository.findAllWithAuthor(user.getId(), pageable);

        return taskPage.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getAllTasksExecutor(String username, int page, int size) {
        log.info("Get all tasks for author");
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Page<Task> taskPage = taskRepository.findAllWithExecutor(user.getId(), pageable);

        return taskPage.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}