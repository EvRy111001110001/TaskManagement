package com.example.TaskManagement.controllers;

import com.example.TaskManagement.model.TaskDTO;
import com.example.TaskManagement.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/users/{userId}/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Management")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "getting task by ID")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username)")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        log.info("Fetching task with id {}", taskId);
        TaskDTO taskDto = taskService.getById(taskId);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "getting all tasks of the author")
    @GetMapping("/{author}")
    public ResponseEntity<Page<TaskDTO>> getAllTasksAuthor(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching tasks for author: " + author + ", page: " + page + ", size: " + size);
        Page<TaskDTO> tasksPage = taskService.getAllTasksAuthor(author, page, size);
        return ResponseEntity.ok(tasksPage);
    }

    @Operation(summary = "getting all tasks of the executor")
    @GetMapping("/{executor}")
    public ResponseEntity<Page<TaskDTO>> getAllTasksExecutor(
            @PathVariable String executor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching tasks for author: " + executor + ", page: " + page + ", size: " + size);
        Page<TaskDTO> tasksPage = taskService.getAllTasksExecutor(executor, page, size);
        return ResponseEntity.ok(tasksPage);
    }

    @Operation(summary = "create new task")
    @PostMapping()
    public ResponseEntity<Void> createTask(
            @PathVariable Long userId,
            @RequestBody TaskDTO taskDto
    ) {

        log.info("Creating task" );
        taskService.create(taskDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "modify task by ID")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username)")
    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDto) {
        log.info("Updating task");
        taskService.patchEntityTask(taskId,taskDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "delete task by ID")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username)")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        log.info("Deleting task with id {}", taskId);
        taskService.deleteById(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "update task status - completed")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username) or @taskSecurityService.isExecutor(#taskId, principal.username)")
    @PatchMapping("/{taskId}/status/completed")
    public ResponseEntity<Void> updateStatusCompleted(@PathVariable Long taskId,@RequestParam String status) {
        log.info("Updating task status to COMPLETED for task with id {}", taskId);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(taskId);
        taskDto.setStatus(status);
        taskService.patchEntityTask(taskId,taskDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "update task status - in progress")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username) or @taskSecurityService.isExecutor(#taskId, principal.username)")
    @PatchMapping("/{taskId}/status/in-process")
    public ResponseEntity<Void> updateStatusInProcess(@PathVariable Long taskId,@RequestParam String status) {
        log.info("Updating task status to IN_PROCESS for task with id {}", taskId);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(taskId);
        taskDto.setStatus(status);
        taskService.patchEntityTask(taskId,taskDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "update task priority - low")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username)")
    @PatchMapping("/{taskId}/priority/low")
    public ResponseEntity<Void> updatePriorityMedium(@PathVariable Long taskId,@RequestParam String priority) {
        log.info("Updating task priority to LOW for task with id {}", taskId);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(taskId);
        taskDto.setPriority(priority);
        taskService.patchEntityTask(taskId,taskDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "update task priority - high")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username)")
    @PatchMapping("/{taskId}/priority/high")
    public ResponseEntity<Void> updatePriorityHigh(@PathVariable Long taskId,@RequestParam String priority) {
        log.info("Updating task priority to HIGH for task with id {}", taskId);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(taskId);
        taskDto.setPriority(priority);
        taskService.patchEntityTask(taskId,taskDto);;
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "update task executor")
    @PreAuthorize("@taskSecurityService.isAuthor(#taskId, principal.username)")
    @PatchMapping("/{taskId}/{executorName}")
    public ResponseEntity<Void> updateExecutor(@PathVariable Long taskId, @RequestParam String executorName) {
        log.info("Updating task executor to {} for task with id {}", executorName, taskId);
        //taskService.patchEntityTask(taskId, executorName);
        return ResponseEntity.ok().build();
    }
}