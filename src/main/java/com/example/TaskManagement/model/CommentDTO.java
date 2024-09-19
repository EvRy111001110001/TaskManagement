package com.example.TaskManagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String task;
    private String text;
    private String authorName;
}
