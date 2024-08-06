package ru.santurov.task_management_system.services.mapper;

import org.mapstruct.*;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.santurov.task_management_system.DTO.comment.TaskCommentResponseDTO;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.models.User;
import ru.santurov.task_management_system.services.CommentResolver;
import ru.santurov.task_management_system.services.UserResolver;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserResolver.class, CommentResolver.class})
public interface TaskMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "performers", source = "performers")
    TaskResponseDTO toTaskResponseDTO(Task task);

    @Mapping(target = "comments", expression = "java(commentResolver.resolveCommentsByTaskId(task.getId()))")
    TaskCommentResponseDTO toTaskCommentResponseDTO(Task task, @Context CommentResolver commentResolver);

    @Mapping(target = "author", expression = "java(userResolver.resolveCurrentUser())")
    @Mapping(target = "performers", expression = "java(taskCreateDTO.getPerformers().stream().map(userResolver::resolveByUsername).toList())")
    @Mapping(target = "status", constant = "PENDING")
    Task toTask(TaskCreateDTO taskCreateDTO, @Context UserResolver userResolver);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "performers", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task, @Context UserResolver userResolver);

    default void updatePerformers(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task, UserResolver userResolver) {
        List<String> performers = taskUpdateDTO.getPerformers();
        if (performers != null || !performers.isEmpty()) {
            List<User> mappedPerformers = performers.stream()
                            .map(userResolver::resolveByUsername)
                            .toList();
            task.getPerformers().addAll(mappedPerformers);
        }
    }
}
