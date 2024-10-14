package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.Comment;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.model.CommentRequestDTO;
import com.example.TaskManagement.model.CommentResponseDTO;
import com.example.TaskManagement.repositories.CommentRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentResponseDTO getById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return toDto(comment);
    }

    public List<CommentResponseDTO> listComment(Long taskId) {
        log.info("Get all comments task " + taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        return commentRepository.findAll()
                .stream().filter(comment -> comment.getTask().equals(task))
                .map(this::toDto)
                .toList();
    }

    public void create(CommentRequestDTO commentRequestDTO, Long taskId) {
        log.info("Create comment by author");
        User user = userRepository.findByUsername(commentRequestDTO.getAuthorName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Comment comment = toEntity(commentRequestDTO, taskId);
        commentRepository.save(comment);
    }

    public void update(CommentRequestDTO commentDto, Long taskId) {
        log.info("Update comment");
        Comment comment = toEntity(commentDto, taskId);
        commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        log.info("Delete comment by id " + id);
        commentRepository.deleteById(id);
    }

    public CommentResponseDTO toDto(Comment comment) {
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        Task task = taskRepository.findById(comment.getTask().getId())
                .orElseThrow(() -> new TaskNotFoundException(comment.getTask().getId()));
        commentResponseDTO.setTask(task.getTitle());
        commentResponseDTO.setText(comment.getText());
        commentResponseDTO.setAuthorName(comment.getAuthor().getUsername());
        return commentResponseDTO;
    }

    public Comment toEntity(CommentRequestDTO commentRequestDTO, Long taskId) {
        Comment comment = new Comment();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        User author = userRepository.findByUsername(commentRequestDTO.getAuthorName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        comment.setTask(task);
        comment.setText(commentRequestDTO.getText());
        comment.setAuthor(author);
        return comment;
    }
}