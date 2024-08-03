package ru.santurov.task_management_system.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.models.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    // Mapping Task to TaskDTO
    TaskDTO toTaskDTO(Task task);

    // Mapping TaskDTO to Task
    Task toTask(TaskDTO taskDTO);

    // Mapping Task to TaskResponseDTO
    @Mapping(source = "author.username", target = "author.username")
    @Mapping(source = "performer.username", target = "performer.username")
    TaskResponseDTO toTaskResponseDTO(Task task);

    // Mapping Task to TaskListDTO
    @Mapping(source = "author.username", target = "authorName")
    @Mapping(source = "performer.username", target = "performerName")
    TaskListDTO toTaskListDTO(Task task);

    // Mapping TaskCreateDTO to Task
    Task toTask(TaskCreateDTO taskCreateDTO);

    // Mapping TaskUpdateDTO to Task
    Task toTask(TaskUpdateDTO taskUpdateDTO);
}