package com.example.TaskManagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Authorization request")
public class SignInRequest {
    @Schema(description = "User email", example = "jondoe@gmail.com")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Password", example = "my_1secret1_password")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
