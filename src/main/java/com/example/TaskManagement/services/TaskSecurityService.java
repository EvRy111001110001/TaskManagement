package com.example.TaskManagement.services;

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

//    public void addTask(Long taskId, Long authorId, Long executorId) {
//        RedisTask task = new RedisTask(taskId, authorId, executorId);
//        redisTaskRoleRepository.save(task);
//    }
//    public void updateExecutor(Long taskId, Long newExecutorId) {
//        Optional<RedisTask> optionalTask = redisTaskRoleRepository.findById(taskId);
//        if (optionalTask.isPresent()) {
//            RedisTask redisTask = optionalTask.get();
//            redisTask.setExecutorId(newExecutorId);
//            redisTaskRoleRepository.save(redisTask);
//        }
//    }

    public boolean checkAuthorRole(Long taskId, String authorName) {
        User user = userRepository.findByUsername(authorName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();
        Optional<RedisTask> optionalTask = redisTaskRoleRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            RedisTask redisTask = optionalTask.get();
            return redisTask.getAuthorId().equals(userId);
        }
        return false;
    }

    public boolean checkExecutorRole(Long taskId, String executorName) {
        User user = userRepository.findByUsername(executorName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();

        Optional<RedisTask> optionalTask = redisTaskRoleRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            RedisTask redisTask = optionalTask.get();
            return redisTask.getExecutorId() != null && redisTask.getExecutorId().equals(userId);
        }
        return false;
    }

//    public boolean hasRoleInTask(Long taskId, String roleName, Long userId) {
//        if ("ROLE_AUTHOR".equals(roleName)) {
//            checkAuthorRole(taskId, userId); // проверяем автора
//            return true;
//        } else if ("ROLE_EXECUTOR".equals(roleName)) {
//            checkExecutorRole(taskId, userId); // проверяем исполнителя
//            return true;
//        }
//        return false;
//    }
}
