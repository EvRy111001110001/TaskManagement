package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.Role;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import com.example.TaskManagement.repositories.UserTaskRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TaskSecurityService {

    @Autowired
    private UserTaskRoleRepository userTaskRoleRepository;
    private UserRepository userRepository;

    public boolean hasRoleInTask(Long taskId, Role roleName, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userTaskRoleRepository.existsByUserIdAndTaskIdAndRole(user.getId(), taskId, roleName);
    }
}
