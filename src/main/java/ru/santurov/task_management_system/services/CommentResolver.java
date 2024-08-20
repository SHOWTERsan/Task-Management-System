package ru.santurov.task_management_system.services;

import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;

import java.util.List;

public interface CommentResolver {
    List<CommentResponseDTO> resolveCommentsByTaskId(Long taskId);
}
