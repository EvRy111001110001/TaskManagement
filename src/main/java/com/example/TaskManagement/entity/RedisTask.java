package com.example.TaskManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@RedisHash("Task")
public class RedisTask implements Serializable {
    private Long id;
    private Long authorId;
    private Long executorId;
}
