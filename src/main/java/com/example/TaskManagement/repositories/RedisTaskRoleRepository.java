package com.example.TaskManagement.repositories;

import com.example.TaskManagement.entity.RedisTask;
import com.example.TaskManagement.services.TaskNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisTaskRoleRepository extends CrudRepository<RedisTask, Long> {

    default void addTask(Long taskId, Long authorId, Long executorId) {
        RedisTask task = new RedisTask(taskId, authorId, executorId);
        if (executorId == null) {
            task.setExecutorId(null);
        }
        save(task);
    }

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
