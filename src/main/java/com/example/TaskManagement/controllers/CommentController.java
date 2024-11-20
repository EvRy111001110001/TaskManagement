package com.example.TaskManagement.controllers;

import com.example.TaskManagement.model.CommentRequestDTO;
import com.example.TaskManagement.model.CommentResponseDTO;
import com.example.TaskManagement.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller for managing comments associated with tasks.
 * This class provides endpoints for retrieving, creating, updating, and deleting comments.
 */
@RestController
@RequestMapping("/api/{taskId}/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Managing comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * Retrieves a comment by its ID.
     *
     * @param commentId the ID of the comment to be retrieved
     * @return a {@link ResponseEntity} containing the {@link CommentResponseDTO} for the requested comment
     */
    @Operation(summary = "get comment by ID")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long commentId) {
        log.info("Fetching comment with id {}", commentId);
        CommentResponseDTO commentDto = commentService.getById(commentId);
        return ResponseEntity.ok(commentDto);
    }

    /**
     * Retrieves all comments for a specific task.
     *
     * @param taskId the ID of the task whose comments are to be fetched
     * @return a {@link ResponseEntity} containing a collection of {@link CommentResponseDTO} for all comments
     */
    @GetMapping
    public ResponseEntity<Collection<CommentResponseDTO>> getAllComments(@PathVariable Long taskId) {
        log.info("Fetching all comments");
        Collection<CommentResponseDTO> commentsDto = commentService.listComment(taskId);
        return ResponseEntity.ok(commentsDto);
    }

    /**
     * Creates a new comment for a task.
     *
     * @param taskId the ID of the task for which the comment is being created
     * @param commentRequestDTO the data transfer object containing the comment details
     * @return a {@link ResponseEntity} with status CREATED (201) if the comment is successfully created
     */
    @Operation(summary = "create new comment")
    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable Long taskId, @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("Creating comment by author");
        commentService.create(commentRequestDTO, taskId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates an existing comment.
     *
     * @param taskId the ID of the task associated with the comment
     * @param commentId the ID of the comment to be updated
     * @param commentDto the data transfer object containing the updated comment details
     * @return a {@link ResponseEntity} with status OK (200) if the comment is successfully updated
     */
    @Operation(summary = "change comment by ID")
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long taskId, @PathVariable Long commentId,
                                              @Valid @RequestBody CommentRequestDTO commentDto) {
        log.info("Updating comment with id");
        commentService.update(commentDto, taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId the ID of the comment to be deleted
     * @return a {@link ResponseEntity} with status NO_CONTENT (204) if the comment is successfully deleted
     */
    @Operation(summary = "delete comment by ID")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        log.info("Deleting comment with id {}", commentId);
        commentService.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
