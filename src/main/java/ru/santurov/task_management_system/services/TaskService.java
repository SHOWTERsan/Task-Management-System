package ru.santurov.task_management_system.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.exceptions.ResourceNotFoundException;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.repositories.TaskRepository;
import ru.santurov.task_management_system.services.mapper.TaskMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDTO getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задание не найдено."));
        return taskMapper.toTaskDTO(task);
    }
    //TODO наложить ограничения на такс дто
    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO) {
        Task task = taskMapper.toTask(taskCreateDTO);
        task = taskRepository.save(task);
        return taskMapper.toTaskResponseDTO(task);
    }

    public TaskResponseDTO updateTask(TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(taskUpdateDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задание не найдено."));
        taskMapper.toTask(taskUpdateDTO);
        task = taskRepository.save(task);
        return taskMapper.toTaskResponseDTO(task);
    }

    public PaginatedResponseDTO<TaskListDTO> getTasks(int page, int size) {
        Page<Task> tasksPage = taskRepository.findAll(PageRequest.of(page, size));
        List<TaskListDTO> taskDTOs = tasksPage.stream()
                .map(taskMapper::toTaskListDTO)
                .collect(Collectors.toList());
        return new PaginatedResponseDTO<>(
                taskDTOs,
                tasksPage.getNumber(),
                tasksPage.getTotalPages(),
                tasksPage.getTotalElements()
        );
    }

    @Transactional
    public void deleteTask(Long id) {
        try {
            if (!taskRepository.existsById(id)) {
                throw new ResourceNotFoundException("Задание с id " + id + " не найдено");
            }
            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Задание с id " + id + " не найдено");
        }
    }
}
