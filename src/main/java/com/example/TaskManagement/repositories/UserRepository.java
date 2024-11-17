package com.example.TaskManagement.repositories;

import com.example.TaskManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findUserById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}