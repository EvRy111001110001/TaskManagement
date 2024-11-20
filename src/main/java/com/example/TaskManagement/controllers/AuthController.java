package com.example.TaskManagement.controllers;

import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.model.JwtAuthenticationResponse;
import com.example.TaskManagement.model.SignInRequest;
import com.example.TaskManagement.model.SignUpRequest;

import com.example.TaskManagement.services.AuthorizationService;
import com.example.TaskManagement.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling authentication and authorization processes.
 * This class provides endpoints for user registration and user login,
 * using JWT (JSON Web Tokens) for authentication.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "authentication and authorization")
public class AuthController {
    private final AuthorizationService authenticationService;
    private final UserService userService;

    /**
     * Handles user registration by accepting a {@link SignUpRequest} and
     * responding with a message and a JWT token for the newly registered user.
     *
     * @param request the user sign-up request containing the necessary user details
     * @return a {@link ResponseEntity} containing a message and the JWT token in a map
     */
    @Operation(summary = "user registration")
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody @Valid SignUpRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signUp(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", jwtResponse.getToken());

        return ResponseEntity.ok(response);
    }

    /**
     * Handles user login by accepting a {@link SignInRequest}, validating the user,
     * and responding with a personalized greeting and a JWT token for the logged-in user.
     *
     * @param request the user sign-in request containing the user's credentials
     * @return a {@link ResponseEntity} containing a greeting message and the JWT token in a map
     */
    @Operation(summary = "user authorization")
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody @Valid SignInRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signIn(request);
        User user = userService.getByEmail(request.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello " + user.getUsername() + " !");
        response.put("token", jwtResponse.getToken());

        return ResponseEntity.ok(response);
    }
}
