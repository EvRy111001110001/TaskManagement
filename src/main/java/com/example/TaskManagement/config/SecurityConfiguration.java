package com.example.TaskManagement.config;

import com.example.TaskManagement.filter.JwtAuthenticationFilter;
import com.example.TaskManagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
////                .cors(cors -> cors.configurationSource(request -> {
////                    var corsConfiguration = new CorsConfiguration();
////                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
////                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////                    corsConfiguration.setAllowedHeaders(List.of("*"));
////                    corsConfiguration.setAllowCredentials(true);
////                    return corsConfiguration;
////                }))
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/tasks/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/tasks/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.PUT, "/tasks/**").hasRole("AUTHOR")
//                        .requestMatchers(HttpMethod.PATCH, "/tasks/**").hasRole("AUTHOR")
//                        .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasRole("AUTHOR")
//                        .requestMatchers(HttpMethod.PATCH, "/tasks/{taskId}/status/*").hasRole("EXECUTOR")
//                        .anyRequest().authenticated())
//                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
   // }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)  // Полностью отключаем CSRF
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/tasks/**").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/tasks/**").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, "/tasks/**").hasRole("AUTHOR")
                    .requestMatchers(HttpMethod.PATCH, "/tasks/**").hasRole("AUTHOR")
                    .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasRole("AUTHOR")
                    .requestMatchers(HttpMethod.PATCH, "/tasks/{taskId}/status/*").hasRole("EXECUTOR")
                    .anyRequest().authenticated())
            .headers(headers -> headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))  // Отключаем защиту от кликов в iframe
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())  // Правильное место для этой строки
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Добавляем фильтр

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}