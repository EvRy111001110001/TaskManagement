package com.example.TaskManagement.exception;

/**
 * Exception thrown when a comment with the specified ID is not found.
 * This exception is used to indicate that the requested comment does not exist in the system.
 * It extends {@link RuntimeException} to be an unchecked exception.
 */
public class CommentNotFoundException extends RuntimeException {

    /**
     * Constructs a new CommentNotFoundException with a detailed message.
     * The message indicates that the comment with the specified ID was not found.
     *
     * @param commentId The ID of the comment that was not found.
     */
    public CommentNotFoundException(Long commentId) {
        super("Task with ID " + commentId + " not found");
    }
}
