package com.example.TaskManagement.services;


import com.example.TaskManagement.entity.Comment;
import com.example.TaskManagement.entity.Task;
import com.example.TaskManagement.entity.User;
import com.example.TaskManagement.model.CommentDTO;
import com.example.TaskManagement.model.TaskDTO;
import com.example.TaskManagement.repositories.CommentRepository;
import com.example.TaskManagement.repositories.TaskRepository;
import com.example.TaskManagement.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentDTO getById(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return toDto(comment);
    }

//    public Collection<CommentDTO> getAll() {
//        log.info("Get all comments");
//        return commentRepository.findAll()
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }

    public void create(CommentDTO commentDto, Long userId) {
        log.info("Create comment by author");
        Comment comment = toEntity(commentDto);
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        comment.setAuthor(user);
        commentRepository.save(comment);
    }

    public void update(CommentDTO commentDto) {
        log.info("Update comment by author");
        Comment comment = toEntity(commentDto);
        commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        log.info("Delete comment by id " + id);
        commentRepository.deleteById(id);
    }

    public CommentDTO toDto(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    public Comment toEntity(CommentDTO commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}