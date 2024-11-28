package com.example.TaskManagement.mappers;

import com.example.TaskManagement.entity.Comment;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.model.CommentRequestDTO;
import com.example.TaskManagement.model.CommentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between {@link Comment} entities and {@link CommentResponseDTO} or
 * {@link CommentRequestDTO} objects.
 *
 * This interface is used by MapStruct to automatically generate the implementation for the mapping logic.
 * It provides two main methods:
 * 1. {@link #toDTO(Comment)} - Converts a {@link Comment} entity to a {@link CommentResponseDTO}.
 * 2. {@link #toEntity(CommentRequestDTO, Task)} - Converts a {@link CommentRequestDTO} and a {@link Task}
 *    into a {@link Comment} entity.
 *
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "task", source = "task.title")
    @Mapping(target = "authorName", source = "author.username")
    CommentResponseDTO toDTO(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAtComment", ignore = true)
    @Mapping(target = "updatedAtComment", ignore = true)
    @Mapping(target = "versionComment", ignore = true)
    @Mapping(target = "task", source = "task")
    @Mapping(target = "text", source = "commentRequestDTO.text")
    Comment toEntity(CommentRequestDTO commentRequestDTO, Task task);
}
