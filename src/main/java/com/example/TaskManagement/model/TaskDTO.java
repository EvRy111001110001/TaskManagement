package com.example.TaskManagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String text;
    private String status;
    private String priority;
    private String authorName;
    private String executorName;
}
