package com.example.TaskManagement.repositories;

import com.example.TaskManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides CRUD operations and custom queries for {@link User} objects.
 * Extends {@link JpaRepository} to provide basic persistence operations and includes custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a {@link User} entity by its username.
     *
     * @param username The username of the user.
     * @return An {@link Optional} containing the user if found, or an empty {@link Optional} if no user with the given username exists.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a {@link User} entity by its email.
     *
     * @param email The email of the user.
     * @return An {@link Optional} containing the user if found, or an empty {@link Optional} if no user with the given email exists.
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a {@link User} entity by its ID.
     *
     * @param id The ID of the user.
     * @return An {@link Optional} containing the user if found, or an empty {@link Optional} if no user with the given ID exists.
     */
    Optional<User> findUserById(Long id);

    /**
     * Checks if a user with the specified username exists in the repository.
     *
     * @param username The username to check for existence.
     * @return {@code true} if a user with the given username exists, {@code false} otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with the specified email exists in the repository.
     *
     * @param email The email to check for existence.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    Boolean existsByEmail(String email);
}
