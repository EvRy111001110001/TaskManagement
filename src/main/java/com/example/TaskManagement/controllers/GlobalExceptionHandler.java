package com.example.TaskManagement.controllers;

import com.example.TaskManagement.exception.CommentNotFoundException;
import com.example.TaskManagement.exception.DuplicateException;
import com.example.TaskManagement.exception.TaskNotFoundException;
import com.example.TaskManagement.model.CommentResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling specific exceptions across all controllers in the application.
 * This class provides centralized handling of exceptions, ensuring appropriate HTTP status codes and error messages are returned.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link TaskNotFoundException} and returns a response with status NOT_FOUND (404).
     *
     * @param ex the exception to be handled
     * @return a {@link ResponseEntity} containing the exception message with a NOT_FOUND status
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles {@link UsernameNotFoundException} and returns a response with status NOT_FOUND (404).
     *
     * @param ex the exception to be handled
     * @return a {@link ResponseEntity} containing the exception message with a NOT_FOUND status
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles {@link DuplicateException} and returns a response with status CONFLICT (409).
     *
     * @param ex the exception to be handled
     * @return a {@link ResponseEntity} containing the exception message with a CONFLICT status
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles {@link CommentNotFoundException} and returns a response with status NOT_FOUND (404).
     *
     * @param ex the exception to be handled
     * @return a {@link ResponseEntity} containing the exception message with a NOT_FOUND status
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles validation exceptions (e.g., {@link MethodArgumentNotValidException}) and returns a response with status BAD_REQUEST (400).
     * This method maps field errors into a more readable format.
     *
     * @param ex the exception to be handled
     * @return a {@link Map} containing the field validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Map<String, String>> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        errors.put("errors", fieldErrors);
        return errors;
    }
}
