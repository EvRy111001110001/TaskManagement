package com.example.TaskManagement.exception;

/**
 * Exception thrown when a task with a specified ID is not found in the system.
 * This exception is used to indicate that a task with the given ID does not exist.
 * It extends {@link RuntimeException} to be an unchecked exception.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructs a new TaskNotFoundException with a detail message indicating
     * that the task with the specified ID was not found.
     *
     * @param taskId The ID of the task that was not found.
     */
    public TaskNotFoundException(Long taskId) {
        super("Task with ID " + taskId + " not found");
    }
}