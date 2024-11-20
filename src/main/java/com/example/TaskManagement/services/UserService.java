package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.exception.DuplicateException;
import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling user-related operations, including user registration,
 * retrieval, and loading user details for authentication.
 * Implements {@link UserDetailsService} to provide custom user loading functionality for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Saves a user entity in the repository.
     *
     * @param user The user entity to be saved.
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Creates a new user by first checking if the username or email already exists in the repository.
     * If the username or email already exists, a {@link DuplicateException} is thrown.
     *
     * @param user The user entity to be created.
     * @throws DuplicateException if the username or email already exists.
     */
    public void create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateException("A user with this name already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateException("A user with this email already exists");
        }
        save(user);
    }

    /**
     * Loads a user by their email address. This method is used by Spring Security for authentication.
     * If no user is found with the given email, a {@link UsernameNotFoundException} is thrown.
     *
     * @param email The email of the user to be loaded.
     * @return A {@link UserDetails} object containing user information for authentication.
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email of the user to retrieve.
     * @return The {@link User} entity corresponding to the given email.
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Provides a {@link UserDetailsService} instance that uses the email to retrieve a user.
     *
     * @return A {@link UserDetailsService} instance.
     */
    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }
}