package com.example.TaskManagement.controllers;

import com.example.TaskManagement.model.TaskRequestDTO;
import com.example.TaskManagement.model.TaskResponseDTO;
import com.example.TaskManagement.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Controller for managing tasks in the application. This class handles requests related to tasks,
 * including retrieving, creating, updating, and deleting tasks, as well as modifying task statuses
 * and priorities. It also enforces authorization using security annotations.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Management")
public class TaskController {

    private final TaskService taskService;

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to be retrieved
     * @return a {@link ResponseEntity} containing the task details if found, or a NOT_FOUND status if the task does not exist
     */
    @Operation(summary = "getting task by ID")
    @PreAuthorize("@taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        log.info("Fetching task with id {}", taskId);
        TaskResponseDTO taskResponseDTO = taskService.getById(taskId);
        return ResponseEntity.ok(taskResponseDTO);
    }

    /**
     * Retrieves all tasks assigned to a specific author, with pagination support.
     *
     * @param author the username of the author whose tasks are to be retrieved
     * @param page the page number to retrieve (default is 0)
     * @param size the number of tasks per page (default is 10)
     * @return a {@link ResponseEntity} containing a list of task DTOs
     */
    @Operation(summary = "getting all tasks of the author")
    @GetMapping("/users/{author}/allTasksOfAuthor")
    public ResponseEntity<Collection<TaskResponseDTO>> getAllTasksAuthor(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching tasks for author: " + author + ", page: " + page + ", size: " + size);
        List<TaskResponseDTO> tasksPage = taskService.getAllTasksAuthor(author, page, size);
        return ResponseEntity.ok(tasksPage);
    }

    /**
     * Retrieves all tasks assigned to a specific executor, with pagination support.
     *
     * @param executor the username of the executor whose tasks are to be retrieved
     * @param page the page number to retrieve (default is 0)
     * @param size the number of tasks per page (default is 10)
     * @return a {@link ResponseEntity} containing a list of task DTOs
     */
    @Operation(summary = "getting all tasks of the executor")
    @GetMapping("/users/{executor}/allTasksOfExecutor")
    public ResponseEntity<Collection<TaskResponseDTO>> getAllTasksExecutor(
            @PathVariable String executor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching tasks for author: " + executor + ", page: " + page + ", size: " + size);
        List<TaskResponseDTO> tasksPage = taskService.getAllTasksExecutor(executor, page, size);
        return ResponseEntity.ok(tasksPage);
    }

    /**
     * Creates a new task.
     *
     * @param taskRequestDto the task data to be created
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "create new task")
    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@Valid  @RequestBody TaskRequestDTO taskRequestDto) {
        log.info("Creating task");
        taskService.create(taskRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param taskId the ID of the task to be updated
     * @param taskRequestDto the updated task data
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "update task by ID")
    @PreAuthorize("@taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long taskId,@Valid @RequestBody TaskRequestDTO taskRequestDto) {
        log.info("Updating task");
        taskService.update(taskRequestDto, taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to be deleted
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "delete task by ID")
    @PreAuthorize("@taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        log.info("Deleting task with id {}", taskId);
        taskService.deleteById(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Updates the status of a task to "COMPLETED".
     *
     * @param taskId the ID of the task to update
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "update task status - completed")
    @PreAuthorize("@taskSecurityService.checkExecutorRole(#taskId, authentication.name) " +
            "|| @taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @PatchMapping("/tasks/{taskId}/status/completed")
    public ResponseEntity<Void> updateStatusCompleted(@PathVariable Long taskId) {
        log.info("Updating task status to COMPLETED for task with id {}", taskId);
        taskService.patchStatusTaskCompleted(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the status of a task to "IN_PROCESS".
     *
     * @param taskId the ID of the task to update
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "update task status - in progress")
    @PreAuthorize("@taskSecurityService.checkExecutorRole(#taskId, authentication.name) " +
            "|| @taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @PatchMapping("/tasks/{taskId}/status/in-process")
    public ResponseEntity<Void> updateStatusInProcess(@PathVariable Long taskId) {
        log.info("Updating task status to IN_PROCESS for task with id {}", taskId);
        taskService.patchStatusTaskInProgress(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the priority of a task to "LOW".
     *
     * @param taskId the ID of the task to update
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "update task priority - low")
    @PreAuthorize("@taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @PatchMapping("/tasks/{taskId}/priority/low")
    public ResponseEntity<Void> updatePriorityMedium(@PathVariable Long taskId) {
        log.info("Updating task priority to LOW for task with id {}", taskId);
        taskService.patchPriorityTaskLow(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the priority of a task to "HIGH".
     *
     * @param taskId the ID of the task to update
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "update task priority - high")
    @PreAuthorize("@taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @PatchMapping("/tasks/{taskId}/priority/high")
    public ResponseEntity<Void> updatePriorityHigh(@PathVariable Long taskId) {
        log.info("Updating task priority to HIGH for task with id {}", taskId);
        taskService.patchPriorityTaskHigh(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the executor for a task.
     *
     * @param taskId the ID of the task to update
     * @param executorName the new executor's name
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @Operation(summary = "update task executor")
    @PreAuthorize("@taskSecurityService.checkAuthorRole(#taskId, authentication.name)")
    @PatchMapping("/tasks/{taskId}/{executorName}")
    public ResponseEntity<Void> updateExecutor(@PathVariable Long taskId, @PathVariable String executorName) {
        log.info("Updating task executor to {} for task with id {}", executorName, taskId);
        taskService.updateExecutor(taskId, executorName);
        return ResponseEntity.ok().build();
    }
}