package com.example.TaskManagement.mappers;


import com.example.TaskManagement.entity.*;
import com.example.TaskManagement.model.CommentRequestDTO;
import com.example.TaskManagement.model.TaskRequestDTO;
import com.example.TaskManagement.model.TaskResponseDTO;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Mapper interface for converting between {@link Task} entities and {@link TaskResponseDTO} or
 * {@link TaskRequestDTO} objects.
 *
 * This interface is used by MapStruct to automatically generate the implementation for the mapping logic.
 * It provides methods for:
 * 1. Converting a {@link Task} entity to a {@link TaskResponseDTO} object using the {@link #toDTO(Task)} method.
 * 2. Mapping a {@link TaskRequestDTO} to a {@link Task} entity, with the option to ignore certain properties
 *    and set default values, using the {@link #toEntity(Task, TaskRequestDTO, User)} method.
 *
 * The interface includes several default methods to handle specific business logic:
 * - {@link #getExecutorTask(Task)} for retrieving the executor's username or a default message.
 * - {@link #setDefaultStatusTask(Task)} to set the default task status if not provided.
 * - {@link #setDefaultPriorityTask(Task)} to set the default priority if not provided.
 * - {@link #mapComments(Task)} for mapping comments of a task to a list of {@link CommentRequestDTO}.
 *
 */

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "executorName", expression = "java(getExecutorTask(task))")
    @Mapping(target = "authorName", source = "author.username")
    @Mapping(target = "comments", expression = "java(mapComments(task))")
    TaskResponseDTO toDTO(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAtTask", ignore = true)
    @Mapping(target = "updatedAtTask", ignore = true)
    @Mapping(target = "versionTask", ignore = true)
    @Mapping(target = "status", expression = "java(setDefaultStatusTask(task))")
    @Mapping(target = "priority", expression = "java(setDefaultPriorityTask(task))")
    void toEntity(@MappingTarget Task task, TaskRequestDTO taskDTO, User author);

    default String getExecutorTask(Task task) {
        if (task.getExecutor() != null) {
            return task.getExecutor().getUsername();
        } else {
            return "You have not assigned a task executor";
        }
    }

    @Named("setDefaultStatusTask")
    default StatusTask setDefaultStatusTask(Task task) {
        if (task.getStatus() == null) {
            return StatusTask.WAITING;
        }else {
            return task.getStatus();
        }
    }

    @Named("setDefaultPriorityTask")
    default PriorityTask setDefaultPriorityTask(Task task) {
        if (task.getPriority() == null) {
            return PriorityTask.MEDIUM;
        }else {
            return task.getPriority();
        }
    }

    @Named("mapComments")
    default List<CommentRequestDTO> mapComments(Task task) {

            if (task.getComments() == null || task.getComments().isEmpty()) {
                return new ArrayList<>();
            }

        return task.getComments().stream()
                .map(comment -> {
                    CommentRequestDTO dto = new CommentRequestDTO();
                    dto.setText(comment.getText());
                    dto.setAuthorName(comment.getAuthor().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

