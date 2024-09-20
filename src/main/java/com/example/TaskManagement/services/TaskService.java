package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.PriorityTask;
import com.example.TaskManagement.entity.StatusTask;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.model.CommentDTO;
import com.example.TaskManagement.model.TaskDTO;
import com.example.TaskManagement.repositories.CommentRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaskService {
    private final CommentRepository commentRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskDTO getById(Long id){
        log.info("Get by task id " + id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return toDto(task);
    }
    public Page<TaskDTO> getAllTasksAuthor(String username,int page, int size) {
        log.info("Get all task author");
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return taskRepository.findAllWithAuthor(user.getId(),pageable)
                .map(this::toDto);
    }
    public Page<TaskDTO> getAllTasksExecutor(String username,int page, int size) {
        log.info("Get all task executor");
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return taskRepository.findAllWithExecutor(user.getId(),pageable)
                .map(this::toDto);

    }
    public void create(TaskDTO taskDto, Long userId) {
        log.info("Create task");
        Task task = toEntity(taskDto);
        setDefaultValue(task);
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            task.setAuthor(user);
        taskRepository.save(task);
    }

    public void update(Task task) {
        log.info("Update task");
        taskRepository.save(task);
    }

    public void deleteById(Long id) {
        log.info("Delete by task id " + id);
        taskRepository.deleteById(id);
    }

    public void setDefaultValue(Task task){
        if (task.getStatus() == null){
            task.setStatus(StatusTask.WAITING);
        }
        if (task.getPriority() == null){
            task.setPriority(PriorityTask.MEDIUM);
        }
    }

    public void patchEntityTask(Long id,TaskDTO taskDto){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
            patchEntityTask(taskDto,task);
    }

    public void updateExecutor(Long taskId,String executorName){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        User user = userRepository.findByUsername(executorName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        task.setAuthor(user);
        taskRepository.save(task);
    }

//    public Page<CommentDTO> findAllCommentsWithTask(Long id){
//        return taskRepository.findAllCommentsWithTask(id)
//                .map(this::toDto);
//    }

    public TaskDTO toDto(Task task) {
        TaskDTO taskDto = modelMapper.map(task, TaskDTO.class);
        if (taskDto.getExecutorName() == null){
            taskDto.setExecutorName("The executor is not identified");
        }
        return taskDto;
    }

    public Task toEntity(TaskDTO taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }

    public void patchEntityTask(TaskDTO taskDto, Task task) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(taskDto, task);
    }
}
