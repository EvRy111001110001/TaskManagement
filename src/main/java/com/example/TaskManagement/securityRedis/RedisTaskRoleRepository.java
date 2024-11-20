package com.example.TaskManagement.securityRedis;

import com.example.TaskManagement.entity.RedisTask;
import com.example.TaskManagement.exception.TaskNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link RedisTask} entities in a Redis database.
 * Extends the Spring Data {@link CrudRepository} to provide basic CRUD operations
 * and includes custom methods for managing tasks and their roles.
 */
public interface RedisTaskRoleRepository extends CrudRepository<RedisTask, Long> {

    /**
     * Adds a new task to the Redis store with the specified task ID, author ID, and executor ID.
     * If the executor ID is {@code null}, it is explicitly set to {@code null}.
     *
     * @param taskId     The unique identifier of the task.
     * @param authorId   The unique identifier of the author of the task.
     * @param executorId The unique identifier of the executor of the task (may be {@code null}).
     */
    default void addTask(Long taskId, Long authorId, Long executorId) {
        RedisTask task = new RedisTask(taskId, authorId, executorId);
        if (executorId == null) {
            task.setExecutorId(null);
        }
        save(task);
    }

    /**
     * Updates the executor of an existing task in the Redis store.
     * If the task does not exist, a {@link TaskNotFoundException} is thrown.
     *
     * @param taskId        The unique identifier of the task to update.
     * @param newExecutorId The new executor ID to be assigned to the task.
     * @throws TaskNotFoundException if no task is found with the specified task ID.
     */
    default void updateExecutor(Long taskId, Long newExecutorId) {
        Optional<RedisTask> optionalTask = findById(taskId);
        if (optionalTask.isPresent()) {
            RedisTask task = optionalTask.get();
            task.setExecutorId(newExecutorId);
            save(task);
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }
}
