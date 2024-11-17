package com.example.TaskManagement.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task with ID " + taskId + " not found");
    }
}