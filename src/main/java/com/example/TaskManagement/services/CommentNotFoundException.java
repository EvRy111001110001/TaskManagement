package com.example.TaskManagement.services;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Task with ID " + commentId + " not found");
    }
}
