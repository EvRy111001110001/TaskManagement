package com.example.TaskManagement.exception;

/**
 * Exception thrown when a duplicate entity or resource is found in the system.
 * This exception is used to indicate that an attempt was made to create or
 * persist an entity or resource that already exists.
 * It extends {@link RuntimeException} to be an unchecked exception.
 */
public class DuplicateException extends RuntimeException {

    /**
     * Constructs a new DuplicateException with the specified detail message.
     * The message provides details about the duplicate entity or resource.
     *
     * @param message The detail message explaining the cause of the exception.
     */
    public DuplicateException(String message) {
        super(message);
    }
}
