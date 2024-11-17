package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
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
        save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

}