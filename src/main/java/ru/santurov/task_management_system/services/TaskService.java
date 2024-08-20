package ru.santurov.task_management_system.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.DTO.comment.TaskCommentResponseDTO;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.exceptions.InsufficientPermissionsException;
import ru.santurov.task_management_system.exceptions.ResourceNotFoundException;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.repositories.TaskRepository;
import ru.santurov.task_management_system.repositories.TaskSpecification;
import ru.santurov.task_management_system.services.mapper.TaskMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserResolver userResolver;
    private final CommentResolver commentResolver;
    private final TaskMapper taskMapper;
    private final TaskAuthorizationService taskAuthorizationService;

    public TaskResponseDTO getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена."));
        return taskMapper.toTaskResponseDTO(task);
    }

    public TaskCommentResponseDTO getTaskWithComments(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена."));
        return taskMapper.toTaskCommentResponseDTO(task, commentResolver);
    }

    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO) {
        Task task = taskMapper.toTask(taskCreateDTO, userResolver);
        task = taskRepository.save(task);
        return taskMapper.toTaskResponseDTO(task);
    }

    public TaskResponseDTO updateTask(TaskUpdateDTO taskUpdateDTO, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена."));

        if (taskAuthorizationService.canUpdateTask(task)) {
            taskMapper.updateTaskFromDto(taskUpdateDTO, task, userResolver);
            taskMapper.updatePerformers(taskUpdateDTO, task, userResolver);
        } else if (taskAuthorizationService.canUpdateTaskStatus(task, taskUpdateDTO)) {
            taskMapper.updateTaskFromDto(taskUpdateDTO, task, userResolver);
        } else {
            throw new InsufficientPermissionsException("Недостаточно прав для выполнения этого действия.");
        }

        task = taskRepository.save(task);
        return taskMapper.toTaskResponseDTO(task);
    }


    public PaginatedResponseDTO<TaskResponseDTO> getTasks(int page, int size, String status, String priority) {
        Specification<Task> spec = TaskSpecification.withFilters(status, priority);
        Page<Task> tasksPage = taskRepository.findAll(spec, PageRequest.of(page, size));
        List<TaskResponseDTO> taskDTOs = tasksPage.stream()
                .map(taskMapper::toTaskResponseDTO)
                .collect(Collectors.toList());
        return new PaginatedResponseDTO<>(
                taskDTOs,
                tasksPage.getNumber(),
                tasksPage.getTotalPages(),
                tasksPage.getTotalElements()
        );
    }

    public PaginatedResponseDTO<TaskCommentResponseDTO> getTasksByAuthorWithComments(Long authorId, int page, int size, String status, String priority) {
        Specification<Task> spec = TaskSpecification.withFilters(status, priority);
        Page<Task> tasksPage = taskRepository.findTasksByAuthor_Id(spec,authorId, PageRequest.of(page, size));
        if (tasksPage.isEmpty()) {
            throw new ResourceNotFoundException("Автор с id " + authorId + " не существует или нее имеет опубликованных задач.");
        }
        List<TaskCommentResponseDTO> taskDTOs = tasksPage.stream()
                .map((Task task) -> taskMapper.toTaskCommentResponseDTO(task, commentResolver))
                .collect(Collectors.toList());
        return new PaginatedResponseDTO<>(
                taskDTOs,
                tasksPage.getNumber(),
                tasksPage.getTotalPages(),
                tasksPage.getTotalElements()
        );
    }
    public PaginatedResponseDTO<TaskCommentResponseDTO> getTasksByPerformerWithComments(Long performerId, int page, int size, String status, String priority) {
        Specification<Task> spec = TaskSpecification.withFilters(status, priority);
        Page<Task> tasksPage = taskRepository.findTasksByPerformers_Id(spec, performerId, PageRequest.of(page, size));
        if (tasksPage.isEmpty()) {
            throw new ResourceNotFoundException("Исполнитель с id " + performerId + " не существует или нее имеет задач.");
        }
        List<TaskCommentResponseDTO> taskDTOs = tasksPage.stream()
                .map((Task task) -> taskMapper.toTaskCommentResponseDTO(task, commentResolver))
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
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));
            if (!taskAuthorizationService.canUpdateTask(task))
                throw new InsufficientPermissionsException("Нужно быть автором задачи чтобы удалить.");

            var performers = task.getPerformers();
            if (performers != null && !performers.isEmpty()) {
                performers.clear();
                taskRepository.save(task);
            }

            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Задача с id " + id + " не найдена");
        }
    }
}
