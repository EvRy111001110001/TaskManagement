package com.example.TaskManagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CommentRepository commentRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
        //
//        // Маппинг для TaskResponseDTO
//        modelMapper.addMappings(new PropertyMap<Task, TaskResponseDTO>() {
//            @Override
//            protected void configure() {
//                using(mapAuthorName()).map(source).setAuthorName(destination.getAuthorName());
//                using(mapExecutorName()).map(source).setExecutorName(destination.getExecutorName());
//                using(mapComments()).map(source).setComments(destination.getComments());
//            }
//        });
//
//        return modelMapper;
//    }
//
//    private org.modelmapper.Converter<Task, String> mapAuthorName() {
//        return context -> {
//            Task task = context.getSource();
//            if (task.getAuthor() != null) {
//                return task.getAuthor().getUsername();
//            }
//            return "Unknown Author";// исправить !
//        };
//    }
//
//    private org.modelmapper.Converter<Task, String> mapExecutorName() {
//        return context -> {
//            Task task = context.getSource();
//            if (task.getExecutor() != null) {
//                Optional<User> executor = userRepository.findUserById(task.getExecutor().getId());
//                return executor.map(User::getUsername).orElse("Executor not found");
//            }
//            return "You have not assigned a task executor";
//        };
//    }
//
//    private org.modelmapper.Converter<Task, List<CommentDTO>> mapComments(Long taskId) {
//        return context -> {
//            List<CommentDTO> commentDtos = commentRepository.findCommentsByTaskId(taskId, PageRequest.of(0, 5)) // Первые 5 комментариев
//                    .stream()
//                    .map(comment -> modelMapper.map(comment, CommentDTO.class))
//                    .collect(Collectors.toList());
//
//            if (commentDtos.isEmpty()) {
//                throw new RuntimeException("No comments yet.");
//            }
//            return commentDtos;
//        };

    }

}
