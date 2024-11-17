package com.example.TaskManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TaskResponseDTO {
    @JsonIgnore
    private Long id;
    private String title;
    private String text;
    private String status;
    private String priority;
    private String authorName;
    private String executorName;
    private List<CommentRequestDTO> comments;
}
