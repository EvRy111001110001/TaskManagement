package com.example.TaskManagement.securityRedis;

import com.example.TaskManagement.entity.RedisTask;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.repositories.UserRepository;
import com.example.TaskManagement.repositories.RedisTaskRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskSecurityService {

    @Autowired
    private RedisTaskRoleRepository redisTaskRoleRepository;
    @Autowired
    private UserRepository userRepository;

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
