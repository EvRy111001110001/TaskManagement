package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.Role;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public void create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateException("A user with this name already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateException("A user with this email already exists");
        }
        //setUser(user);
        save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

//    public void setAuthor(User user) {
//        user.setRole(Role.ROLE_AUTHOR);
//    }
//
//    public void setExecutor(User user) {
//        user.setRole(Role.ROLE_EXECUTOR);
//    }
//
//    public void setUser(User user) {
//        user.setRole(Role.ROLE_USER);
//    }
}