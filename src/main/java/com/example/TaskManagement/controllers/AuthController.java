package com.example.TaskManagement.controllers;

import com.example.TaskManagement.model.JwtAuthenticationResponse;
import com.example.TaskManagement.model.SignInRequest;
import com.example.TaskManagement.model.SignUpRequest;

import com.example.TaskManagement.services.AuthorizationService;
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

//    @Operation(summary = "user registration")
//    @PostMapping("/sign-up")
//    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {
//        JwtAuthenticationResponse jwtResponse = authenticationService.signUp(request);
//
//        return ResponseEntity.ok("User registered successfully");
//    }
    @Operation(summary = "user registration")
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody @Valid SignUpRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signUp(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", jwtResponse.getToken()); // предполагается, что jwtResponse имеет getToken()

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "user authorization")
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody @Valid SignInRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signIn(request);

        //return ResponseEntity.ok("Hello " + request.getUsername() + " !");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello " + request.getUsername() + " !");
        response.put("token", jwtResponse.getToken()); // предполагается, что jwtResponse имеет getToken()

        return ResponseEntity.ok(response);
    }
}
