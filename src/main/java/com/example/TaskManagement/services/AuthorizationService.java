package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.exception.DuplicateException;
import com.example.TaskManagement.model.JwtAuthenticationResponse;
import com.example.TaskManagement.model.SignInRequest;
import com.example.TaskManagement.model.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

/**
 * Service class responsible for handling user authentication and registration processes.
 * Provides functionality for user sign-up and sign-in, including JWT token generation.
 */
@Service
@RequiredArgsConstructor
public class AuthorizationService {
    /**
     * Service responsible for user management operations.
     */
    private final UserService userService;

    /**
     * Service responsible for generating and validating JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * Encoder for hashing passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The manager used to authenticate user credentials.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user and generates a JWT token for the newly created user.
     *
     * @param request The request containing the user's sign-up information (username, email, password).
     * @return A {@link JwtAuthenticationResponse} containing the generated JWT token.
     * @throws DuplicateException if a user with the provided username or email already exists.
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Authenticates a user based on the provided credentials and generates a JWT token for the authenticated user.
     *
     * @param request The request containing the user's sign-in information (email, password).
     * @return A {@link JwtAuthenticationResponse} containing the generated JWT token.
     * @throws UsernameNotFoundException if the user with the provided email is not found.
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
