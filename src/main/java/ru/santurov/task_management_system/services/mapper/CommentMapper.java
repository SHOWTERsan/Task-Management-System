package ru.santurov.task_management_system.services.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.santurov.task_management_system.DTO.comment.CommentCreateDTO;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.models.Comment;
import ru.santurov.task_management_system.services.TaskResolver;
import ru.santurov.task_management_system.services.UserResolver;

import java.util.List;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class CommentMapper {
    protected UserResolver userResolver;
    protected TaskResolver taskResolver;

    @Mapping(target = "task", expression = "java(taskResolver.resolveTaskById(taskId))")
    @Mapping(target = "author", expression = "java(userResolver.resolveByUsername(userResolver.resolveCurrentUser().getUsername()))")
    public abstract Comment toComment(CommentCreateDTO commentCreateDTO, Long taskId);

    public abstract CommentResponseDTO toCommentResponse(Comment comment);

    public abstract List<CommentResponseDTO> toCommentResponseList(List<Comment> comments);

}
