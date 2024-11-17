package com.example.TaskManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TaskRequestDTO {
    @JsonIgnore
    private Long id;
    private String authorName;

    @NotBlank(message = "Task title cannot be blank")
    private String title;

    @NotBlank(message = "Task text cannot be blank")
    private String text;
    private String executorName;
}
