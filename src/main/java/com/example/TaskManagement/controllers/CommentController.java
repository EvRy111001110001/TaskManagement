package com.example.TaskManagement.controllers;


import com.example.TaskManagement.entity.Comment;
import com.example.TaskManagement.model.CommentDTO;
import com.example.TaskManagement.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/users/{userId}/{taskId}/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Managing comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "get comment by ID")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        log.info("Fetching comment with id {}", commentId);
        CommentDTO commentDto = commentService.getById(commentId);
        return ResponseEntity.ok(commentDto);
    }

//    @GetMapping
//    public ResponseEntity<Collection<CommentDTO>> getAllComments() {
//        log.info("Fetching all comments");
//        Collection<CommentDTO> commentsDto = commentService.getAll();
//        return ResponseEntity.ok(commentsDto);
//    }

    @Operation(summary = "create new comment")
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDTO commentDto, @RequestParam Long userId) {
        log.info("Creating comment by author");
        commentService.create(commentDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "change comment by ID")
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentDTO commentDto) {
        log.info("Updating comment with id");
        commentService.update(commentDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "delete comment by ID")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        log.info("Deleting comment with id {}", commentId);
        commentService.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
