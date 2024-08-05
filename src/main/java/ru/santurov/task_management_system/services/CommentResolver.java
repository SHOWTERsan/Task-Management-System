package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.santurov.task_management_system.models.Comment;
import ru.santurov.task_management_system.repositories.CommentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentResolver {
    private final CommentRepository commentRepository;

    public List<Comment> resolveCommentsByTaskId(Long taskId) {
        return commentRepository.findCommentsByTask_Id(taskId);
    }
}
