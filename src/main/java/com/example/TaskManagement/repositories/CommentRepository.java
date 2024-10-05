package com.example.TaskManagement.repositories;

import com.example.TaskManagement.entity.Comment;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
