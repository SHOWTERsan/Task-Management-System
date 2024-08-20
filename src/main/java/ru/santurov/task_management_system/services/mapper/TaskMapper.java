package ru.santurov.task_management_system.services.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.santurov.task_management_system.DTO.comment.TaskCommentResponseDTO;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.models.User;
import ru.santurov.task_management_system.services.CommentResolver;
import ru.santurov.task_management_system.services.UserResolver;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Autowired
    protected UserResolver userResolver;

    @Autowired
    protected CommentResolver commentResolver;

    public abstract TaskResponseDTO toTaskResponseDTO(Task task);

    @Mapping(target = "comments", expression = "java(commentResolver.resolveCommentsByTaskId(task.getId()))")
    public abstract TaskCommentResponseDTO toTaskCommentResponseDTO(Task task);

    @Mapping(target = "author", expression = "java(userResolver.resolveCurrentUser())")
    @Mapping(target = "performers", expression = "java(taskCreateDTO.getPerformers().stream().map(userResolver::resolveByUsername).toList())")
    @Mapping(target = "status", constant = "PENDING")
    public abstract Task toTask(TaskCreateDTO taskCreateDTO);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "performers", ignore = true)
    @Mapping(target = "status", expression = "java(taskUpdateDTO.getStatus() != null ? Enum.valueOf(TaskStatus.class, taskUpdateDTO.getStatus().toUpperCase()) : task.getStatus())")
    @Mapping(target = "priority", expression = "java(taskUpdateDTO.getPriority() != null ? Enum.valueOf(TaskPriority.class, taskUpdateDTO.getPriority().toUpperCase()) : task.getPriority())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateTaskFromDto(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);

    public void updatePerformers(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task) {
        List<String> performers = taskUpdateDTO.getPerformers();
        if (performers != null && !performers.isEmpty()) {
            List<User> mappedPerformers = performers.stream()
                            .map(userResolver::resolveByUsername)
                            .toList();
            task.getPerformers().addAll(mappedPerformers);
        }
    }
}
