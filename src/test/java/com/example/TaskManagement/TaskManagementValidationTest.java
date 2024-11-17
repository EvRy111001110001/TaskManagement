package com.example.TaskManagement;

import com.example.TaskManagement.controllers.AuthController;
import com.example.TaskManagement.model.JwtAuthenticationResponse;
import com.example.TaskManagement.model.SignUpRequest;
import com.example.TaskManagement.services.AuthorizationService;
import com.example.TaskManagement.services.JwtService;
import com.example.TaskManagement.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class TaskManagementValidationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorizationService authorizationService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;


    @Test
    @WithMockUser
    public void testValidInput() throws Exception {
        JwtAuthenticationResponse mockJwtResponse = new JwtAuthenticationResponse("mocked-token");

        when(authorizationService.signUp(any(SignUpRequest.class))).thenReturn(mockJwtResponse);
        mockMvc.perform(post("/auth/sign-up")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"author@vkdo.ru\", \"password\":\"TEST\", \"username\":\"Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.token").value("mocked-token"));
    }

    @Test
    @WithMockUser
    public void testInvalidInput() throws Exception {
        mockMvc.perform(post("/auth/sign-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid-email\", \"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Invalid email format"))
                .andExpect(jsonPath("$.errors.password").value("Password cannot be blank"));
    }
}
