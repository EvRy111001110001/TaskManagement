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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "authentication and authorization")
public class AuthController {
    private final AuthorizationService authenticationService;
    private final UserService userService;

    @Operation(summary = "user registration")
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody @Valid SignUpRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signUp(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", jwtResponse.getToken());

        return ResponseEntity.ok(response);
    }

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
