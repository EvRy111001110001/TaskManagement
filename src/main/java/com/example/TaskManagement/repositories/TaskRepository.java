package com.example.TaskManagement.repositories;


import com.example.TaskManagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Task} entities.
 * Provides CRUD operations and custom queries for {@link Task} objects.
 * Extends {@link JpaRepository} for basic persistence operations and includes custom query methods.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Retrieves a paginated list of {@link Task} entities where the specified author is the owner.
     *
     * @param id The ID of the author whose tasks are being retrieved.
     * @param pageable Pagination information (e.g., page number and size).
     * @return A {@link Page} of {@link Task} entities for the specified author.
     */
    @Query(value = "SELECT t.* " +
            "FROM tasks t " +
            "WHERE t.author_id = :id", nativeQuery = true)
    Page<Task> findAllWithAuthor(@Param("id") Long id, Pageable pageable);

    /**
     * Retrieves a paginated list of {@link Task} entities where the specified user is the executor.
     *
     * @param id The ID of the executor whose tasks are being retrieved.
     * @param pageable Pagination information (e.g., page number and size).
     * @return A {@link Page} of {@link Task} entities for the specified executor.
     */
    @Query(value = "SELECT t.* " +
            "FROM tasks t " +
            "WHERE t.executor_id =  :id", nativeQuery = true)
    Page<Task> findAllWithExecutor(@Param("id") Long id, Pageable pageable);

    /**
     * Retrieves a list of comments associated with a specific task, along with the author's name.
     *
     * @param id The ID of the task whose comments are being retrieved.
     * @return A list of object arrays where each array contains the comment text and the author's name.
     */
    @Query(value = "SELECT c.text, u.name " +
            "FROM comments c JOIN users u ON " +
            "c.author_id = u.id " +
            "WHERE c.task_id = :id",
            nativeQuery = true)
    List<Object[]> findAllComment(@Param("id") Long id);
}
