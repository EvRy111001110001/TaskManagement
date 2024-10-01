package com.example.TaskManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String title;
    private String text;
    private String executorName;
}
