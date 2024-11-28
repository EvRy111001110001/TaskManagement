package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.Comment;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.exception.CommentNotFoundException;
import com.example.TaskManagement.exception.TaskNotFoundException;
import com.example.TaskManagement.mappers.CommentMapper;
import com.example.TaskManagement.model.CommentRequestDTO;
import com.example.TaskManagement.model.CommentResponseDTO;
import com.example.TaskManagement.repositories.CommentRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing comments within the system.
 * Provides functionality for creating, retrieving, updating, and deleting comments,
 * as well as converting between DTOs and entities.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Retrieves a comment by its ID and converts it to a response DTO.
     *
     * @param id The ID of the comment.
     * @return A {@link CommentResponseDTO} representing the comment.
     * @throws CommentNotFoundException if no comment with the given ID exists.
     */
    public CommentResponseDTO getById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return commentMapper.toDTO(comment);
    }

    /**
     * Retrieves all comments associated with a specific task.
     *
     * @param taskId The ID of the task.
     * @return A list of {@link CommentResponseDTO} objects for the task.
     * @throws TaskNotFoundException if no task with the given ID exists.
     */
    public List<CommentResponseDTO> listComment(Long taskId) {
        log.info("Get all comments task " + taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        return commentRepository.findAll()
                .stream().filter(comment -> comment.getTask().equals(task))
                .map(commentMapper::toDTO)
                .toList();
    }

    /**
     * Creates a new comment for a specific task based on the provided DTO.
     *
     * @param commentRequestDTO The data transfer object containing comment details.
     * @param taskId            The ID of the task the comment is associated with.
     * @throws UsernameNotFoundException if the author of the comment does not exist.
     */
    public void create(CommentRequestDTO commentRequestDTO, Long taskId) {
        log.info("Create comment by author");
        User user = userRepository.findByUsername(commentRequestDTO.getAuthorName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Comment comment = commentMapper.toEntity(commentRequestDTO,task);
        commentRepository.save(comment);
    }

    /**
     * Updates an existing comment for a specific task based on the provided DTO.
     *
     * @param commentRequestDTO The data transfer object containing updated comment details.
     * @param taskId     The ID of the task the comment is associated with.
     */
    public void update(CommentRequestDTO commentRequestDTO, Long taskId) {
        log.info("Update comment");
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Comment comment = commentMapper.toEntity(commentRequestDTO,task);
        commentRepository.save(comment);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to delete.
     */
    public void deleteById(Long id) {
        log.info("Delete comment by id " + id);
        commentRepository.deleteById(id);
    }

}