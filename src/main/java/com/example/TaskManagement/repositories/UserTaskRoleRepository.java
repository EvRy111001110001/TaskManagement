package com.example.TaskManagement.repositories;

import com.example.TaskManagement.entity.Role;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.UserTaskRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTaskRoleRepository extends JpaRepository<UserTaskRole, Long> {
    boolean existsByUserIdAndTaskIdAndRole(Long userId, Long taskId, Role role);
    Optional<UserTaskRole> findByTaskAndRole(Task task, Role role);
}
