package com.example.TaskManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank(message = "Comment text cannot be blank")
    private String text;

    private String authorName;
}
