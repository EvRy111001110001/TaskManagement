package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.*;
import com.example.TaskManagement.exception.TaskNotFoundException;
import com.example.TaskManagement.mappers.TaskMapper;
import com.example.TaskManagement.model.TaskRequestDTO;
import com.example.TaskManagement.model.TaskResponseDTO;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import com.example.TaskManagement.securityRedis.RedisTaskRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class that handles the business logic for managing tasks.
 * It includes operations for creating, updating, retrieving, deleting, and manipulating tasks and their properties
 * such as status, priority, and executor. It interacts with repositories for tasks, users, and Redis for role management.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RedisTaskRoleRepository redisTaskRoleRepository;
    private final TaskMapper taskMapper;

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return A TaskResponseDTO containing task details.
     * @throws TaskNotFoundException If no task with the given ID exists.
     */
    public TaskResponseDTO getById(Long id) {
        log.info("Get by task id " + id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toDTO(task);
    }

    /**
     * Creates a new task from the provided TaskRequestDTO.
     *
     * @param taskRequestDto The data transfer object containing the task details.
     * @throws UsernameNotFoundException If the author or executor user cannot be found.
     */
    public void create(TaskRequestDTO taskRequestDto) {
        log.info("Create task");
        User author = userRepository.findByUsername(taskRequestDto.getAuthorName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Task task = new Task();
                taskMapper.toEntity(task,taskRequestDto,author);

        Task savedTask = taskRepository.save(task);

        Long executorId = null;
        if (taskRequestDto.getExecutorName() != null) {
            executorId = userRepository.findByUsername(taskRequestDto.getExecutorName())
                    .map(User::getId)
                    .orElse(null);
        }

        redisTaskRoleRepository.addTask(savedTask.getId(), author.getId(), executorId);
    }

    /**
     * Updates an existing task with new details from the provided TaskRequestDTO.
     *
     * @param taskRequestDTO The new task details.
     * @param taskId The ID of the task to update.
     * @throws TaskNotFoundException If the task with the given ID cannot be found.
     * @throws UsernameNotFoundException If the author user cannot be found.
     */
    public void update(TaskRequestDTO taskRequestDTO, Long taskId) {
        log.info("Update task");
        User author = userRepository.findByUsername(taskRequestDTO.getAuthorName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        taskMapper.toEntity(task,taskRequestDTO,author);

        taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     */
    public void deleteById(Long id) {
        log.info("Delete by task id " + id);
        taskRepository.deleteById(id);
    }

    /**
     * Retrieves all tasks associated with a specific author, with pagination support.
     *
     * @param username The username of the author whose tasks are to be retrieved.
     * @param page The page number.
     * @param size The number of tasks per page.
     * @return A list of TaskResponseDTO objects containing task details.
     * @throws UsernameNotFoundException If the user with the given username cannot be found.
     */
    public List<TaskResponseDTO> getAllTasksAuthor(String username, int page, int size) {
        log.info("Get all tasks for author");
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Page<Task> taskPage = taskRepository.findAllWithAuthor(user.getId(), pageable);

        return taskPage.getContent()
                .stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all tasks assigned to a specific executor, with pagination support.
     *
     * @param username The username of the executor whose tasks are to be retrieved.
     * @param page The page number.
     * @param size The number of tasks per page.
     * @return A list of TaskResponseDTO objects containing task details.
     * @throws UsernameNotFoundException If the user with the given username cannot be found.
     */
    public List<TaskResponseDTO> getAllTasksExecutor(String username, int page, int size) {
        log.info("Get all tasks for executor");
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Page<Task> taskPage = taskRepository.findAllWithExecutor(user.getId(), pageable);

        return taskPage.getContent()
                .stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Patches the status of a task to "IN_PROCESS".
     *
     * @param id The ID of the task to update.
     * @throws TaskNotFoundException If the task with the given ID cannot be found.
     */
    public void patchStatusTaskInProgress(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(StatusTask.IN_PROCESS);
        taskRepository.save(task);
    }

    /**
     * Patches the status of a task to "COMPLETED".
     *
     * @param id The ID of the task to update.
     * @throws TaskNotFoundException If the task with the given ID cannot be found.
     */
    public void patchStatusTaskCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(StatusTask.COMPLETED);
        taskRepository.save(task);
    }

    /**
     * Patches the priority of a task to "LOW".
     *
     * @param id The ID of the task to update.
     * @throws TaskNotFoundException If the task with the given ID cannot be found.
     */
    public void patchPriorityTaskLow(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setPriority(PriorityTask.LOW);
        taskRepository.save(task);
    }

    /**
     * Patches the priority of a task to "HIGH".
     *
     * @param id The ID of the task to update.
     * @throws TaskNotFoundException If the task with the given ID cannot be found.
     */
    public void patchPriorityTaskHigh(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setPriority(PriorityTask.HIGH);
        taskRepository.save(task);
    }

    /**
     * Updates the executor of a task.
     *
     * @param taskId The ID of the task to update.
     * @param executorName The username of the new executor.
     * @throws TaskNotFoundException If the task with the given ID cannot be found.
     * @throws UsernameNotFoundException If the executor user cannot be found.
     */
    public void updateExecutor(Long taskId, String executorName) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        User executor = userRepository.findByUsername(executorName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        task.setExecutor(executor);
        taskRepository.save(task);
        redisTaskRoleRepository.updateExecutor(taskId, executor.getId());
    }

}