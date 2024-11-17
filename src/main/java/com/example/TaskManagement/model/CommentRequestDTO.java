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
public class CommentRequestDTO {
    @JsonIgnore
    private Long id;

    @Schema(description = "Comment to the task")
    @NotBlank(message = "Comment text cannot be blank")
    private String text;

    @Schema(description = "Author name", example = "Jon")
    private String authorName;
}
