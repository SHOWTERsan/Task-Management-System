package ru.santurov.task_management_system.services.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.santurov.task_management_system.DTO.comment.CommentCreateDTO;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.models.Comment;
import ru.santurov.task_management_system.services.TaskResolver;
import ru.santurov.task_management_system.services.UserResolver;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserResolver.class, TaskResolver.class})
public interface CommentMapper {

    @Mapping(target = "task", expression = "java(taskResolver.resolveTaskById(taskId))")
    @Mapping(target = "author", expression = "java(userResolver.resolveByUsername(SecurityContextHolder.getContext().getAuthentication().getName()))")
    Comment toComment(CommentCreateDTO commentCreateDTO, Long taskId, @Context UserResolver userResolver,@Context TaskResolver taskResolver, @Context SecurityContextHolder securityContext);

    CommentResponseDTO toCommentResponse(Comment comment);

    List<CommentResponseDTO> toCommentResponseList(List<Comment> comments);

}
