package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.DTO.comment.CommentCreateDTO;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.models.Comment;
import ru.santurov.task_management_system.repositories.CommentRepository;
import ru.santurov.task_management_system.services.mapper.CommentMapper;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentResponseDTO createComment(CommentCreateDTO commentCreateDTO, Long taskId) {
        Comment comment = commentMapper.toComment(commentCreateDTO, taskId);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }
}
