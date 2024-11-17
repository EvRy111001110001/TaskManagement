package com.example.TaskManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Task Management Request")
public class TaskRequestDTO {
    @JsonIgnore
    private Long id;

    @Schema(description = "Author name", example = "Jon")
    private String authorName;

    @Schema(description = "Title task")
    @NotBlank(message = "Task title cannot be blank")
    private String title;

    @Schema(description = "Task description")
    @NotBlank(message = "Task text cannot be blank")
    private String text;

    @Schema(description = "Executor name", example = "Jon")
    private String executorName;
}
