package com.example.TaskManagement.services;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task with ID " + taskId + " not found");
    }
}