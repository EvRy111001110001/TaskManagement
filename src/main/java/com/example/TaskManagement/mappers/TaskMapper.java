package com.example.TaskManagement.mappers;


import com.example.TaskManagement.entity.*;
import com.example.TaskManagement.model.TaskRequestDTO;
import com.example.TaskManagement.model.TaskResponseDTO;
import org.mapstruct.*;


/**
 * TaskMapper
 * <p>
 *
 * </p>
 */

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "executorName", expression = "java(getExecutorTask(task))")
    @Mapping(target = "authorName", source = "author.username")
    TaskResponseDTO toDTO(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAtTask", ignore = true)
    @Mapping(target = "updatedAtTask", ignore = true)
    @Mapping(target = "versionTask", ignore = true)
    @Mapping(target = "status", expression = "java(setDefaultStatusTask(task))")
    @Mapping(target = "priority", expression = "java(setDefaultPriorityTask(task))")
    //@Mapping(target = "comments", expression = "java(mapComments(task))")
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

//    @Named("mapComments")
//    default List<Comment> mapComments(Task task) {
//
//        if (task.getComments() == null || task.getComments().isEmpty()) {
//            return new ArrayList<>();
//        }
//        return task.getComments();
//    }
}

