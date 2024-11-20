package com.example.TaskManagement.filter;

import com.example.TaskManagement.services.JwtService;
import com.example.TaskManagement.services.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter is responsible for authenticating the user based on the JWT token provided in the HTTP request's Authorization header.
 * It checks if the request contains a valid Bearer token, extracts the username from the token, and sets the authentication context
 * if the token is valid. The filter ensures that authentication information is available for subsequent requests.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Prefix for Bearer token in the Authorization header.
     */
    public static final String BEARER_PREFIX = "Bearer ";


    /**
     * The name of the Authorization header in the HTTP request.
     */
    public static final String HEADER_NAME = "Authorization";

    private final JwtService jwtService;
    private final UserService userService;

    /**
     * This method is invoked for every incoming HTTP request to check the presence and validity of the JWT token.
     * If a valid token is found, it extracts the username from the token, verifies its validity, and sets the authentication context.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to continue processing the request.
     * @throws ServletException If an error occurs during request filtering.
     * @throws IOException If an I/O error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        var authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            try {
                filterChain.doFilter(request, response);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }


        var jwt = authHeader.substring(BEARER_PREFIX.length());
        var email = jwtService.extractUserName(jwt);
        if (StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService
                    .loadUserByUsername(email);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
