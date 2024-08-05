package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.repositories.CommentRepository;
import ru.santurov.task_management_system.services.mapper.CommentMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentResolver {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentResponseDTO> resolveCommentsByTaskId(Long taskId) {
        return commentMapper.toCommentResponseList(commentRepository.findCommentsByTask_Id(taskId));
    }
}
