package com.example.TaskManagement.model;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
