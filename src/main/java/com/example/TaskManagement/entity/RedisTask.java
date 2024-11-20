package com.example.TaskManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Represents a task stored in Redis.
 * This class is used to hold task-related information, such as the task ID, author ID, and executor ID,
 * for efficient access and manipulation within Redis storage.
 */
@Getter
@Setter
@AllArgsConstructor
@RedisHash("Task")
public class RedisTask implements Serializable {

    private Long id;
    private Long authorId;
    private Long executorId;
}
