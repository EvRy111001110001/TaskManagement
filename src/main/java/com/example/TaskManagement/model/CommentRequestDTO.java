package com.example.TaskManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentRequestDTO {
    @JsonIgnore
    private Long id;
    private String text;
    private String authorName;
}
