package ru.santurov.task_management_system.services.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.services.UserResolver;

@Mapper(componentModel = "spring", uses = UserResolver.class)
public interface TaskMapper {

    TaskDTO toTaskDTO(Task task);

    Task toTask(TaskDTO taskDTO);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "performer", source = "performer")
    TaskResponseDTO toTaskResponseDTO(Task task);

    @Mapping(target = "author", expression = "java(userResolver.resolveByUsername(SecurityContextHolder.getContext().getAuthentication().getName()))")
    @Mapping(target = "performer", expression = "java(userResolver.resolveByUsername(taskCreateDTO.getPerformerUsername()))")
    @Mapping(target = "status", constant = "PENDING")
    Task toTask(TaskCreateDTO taskCreateDTO, @Context UserResolver userResolver, @Context SecurityContextHolder securityContext);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "performer", ignore = true)
    Task toTask(TaskUpdateDTO taskUpdateDTO);
}
