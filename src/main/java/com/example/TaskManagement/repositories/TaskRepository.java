package com.example.TaskManagement.repositories;

import com.example.TaskManagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT t.* " +
            "FROM tasks t " +
            "WHERE t.author_id = :id", nativeQuery = true)
    Page<Task> findAllWithAuthor(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT t.* " +
            "FROM tasks t " +
            "WHERE t.executor_id =  :id", nativeQuery = true)
    Page<Task> findAllWithExecutor(@Param("id") Long id, Pageable pageable);

}
