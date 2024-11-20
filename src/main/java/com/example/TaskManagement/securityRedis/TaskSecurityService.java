package com.example.TaskManagement.securityRedis;

import com.example.TaskManagement.entity.RedisTask;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class responsible for verifying user roles in relation to tasks.
 * Provides methods to check if a user is the author or executor of a given task.
 */
@Service
public class TaskSecurityService {

    @Autowired
    private RedisTaskRoleRepository redisTaskRoleRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Checks if the user with the given email is the author of the specified task.
     *
     * @param taskId     The unique identifier of the task.
     * @param authorEmail The email address of the user to check.
     * @return {@code true} if the user is the author of the task, {@code false} otherwise.
     * @throws UsernameNotFoundException if the user with the given email is not found.
     */
    public boolean checkAuthorRole(Long taskId, String authorEmail) {
        User user = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();
        Optional<RedisTask> optionalTask = redisTaskRoleRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            RedisTask redisTask = optionalTask.get();
            return redisTask.getAuthorId().equals(userId);
        }
        return false;
    }

    /**
     * Checks if the user with the given email is the executor of the specified task.
     *
     * @param taskId       The unique identifier of the task.
     * @param executorEmail The email address of the user to check.
     * @return {@code true} if the user is the executor of the task, {@code false} otherwise.
     * @throws UsernameNotFoundException if the user with the given email is not found.
     */
    public boolean checkExecutorRole(Long taskId, String executorEmail) {
        User user = userRepository.findByEmail(executorEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();

        Optional<RedisTask> optionalTask = redisTaskRoleRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            RedisTask redisTask = optionalTask.get();
            return redisTask.getExecutorId() != null && redisTask.getExecutorId().equals(userId);
        }
        return false;
    }
}
