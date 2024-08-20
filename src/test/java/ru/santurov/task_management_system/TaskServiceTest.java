package ru.santurov.task_management_system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ru.santurov.task_management_system.DTO.task.PaginatedResponseDTO;
import ru.santurov.task_management_system.DTO.task.TaskCreateDTO;
import ru.santurov.task_management_system.DTO.task.TaskResponseDTO;
import ru.santurov.task_management_system.DTO.task.TaskUpdateDTO;
import ru.santurov.task_management_system.exceptions.InsufficientPermissionsException;
import ru.santurov.task_management_system.exceptions.ResourceNotFoundException;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.repositories.TaskRepository;
import ru.santurov.task_management_system.services.CommentResolver;
import ru.santurov.task_management_system.services.TaskAuthorizationService;
import ru.santurov.task_management_system.services.TaskService;
import ru.santurov.task_management_system.services.UserResolver;
import ru.santurov.task_management_system.services.mapper.TaskMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserResolver userResolver;

    @Mock
    private CommentResolver commentResolver;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskAuthorizationService taskAuthorizationService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetTask() {
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(new TaskResponseDTO());

        TaskResponseDTO result = taskService.getTask(1L);

        assertNotNull(result);
        verify(taskRepository).findById(1L);
        verify(taskMapper).toTaskResponseDTO(task);
    }

    @Test
    void testGetTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTask(1L));
    }

    @Test
    void testCreateTask() {
        TaskCreateDTO createDTO = new TaskCreateDTO();
        Task task = new Task();

        when(taskMapper.toTask(createDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(new TaskResponseDTO());

        TaskResponseDTO result = taskService.createTask(createDTO);

        assertNotNull(result);
        verify(taskMapper).toTask(createDTO);
        verify(taskRepository).save(task);
        verify(taskMapper).toTaskResponseDTO(task);
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskAuthorizationService.canUpdateTask(task)).thenReturn(true);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(new TaskResponseDTO());

        TaskResponseDTO result = taskService.updateTask(updateDTO, 1L);

        assertNotNull(result);
        verify(taskRepository).findById(1L);
        verify(taskAuthorizationService).canUpdateTask(task);
        verify(taskMapper).updateTaskFromDto(updateDTO, task);
        verify(taskRepository).save(task);
        verify(taskMapper).toTaskResponseDTO(task);
    }

    @Test
    void testUpdateTask_NotAuthorized() {
        Task task = new Task();
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskAuthorizationService.canUpdateTask(task)).thenReturn(false);
        when(taskAuthorizationService.canUpdateTaskStatus(task, updateDTO)).thenReturn(false);

        assertThrows(InsufficientPermissionsException.class, () -> taskService.updateTask(updateDTO, 1L));
    }

    @Test
    void testGetTasks() {
        Task task = new Task();
        List<Task> tasks = Collections.singletonList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);

        when(taskRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(taskPage);
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(new TaskResponseDTO());

        PaginatedResponseDTO<TaskResponseDTO> result = taskService.getTasks(0, 10, "status", "priority");

        assertNotNull(result);
        assertEquals(1, result.getTotalItems());
        verify(taskRepository).findAll(any(Specification.class), any(PageRequest.class));
        verify(taskMapper).toTaskResponseDTO(task);
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskAuthorizationService.canUpdateTask(task)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotAuthorized() {
        Task task = new Task();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskAuthorizationService.canUpdateTask(task)).thenReturn(false);

        assertThrows(InsufficientPermissionsException.class, () -> taskService.deleteTask(1L));
    }
}
