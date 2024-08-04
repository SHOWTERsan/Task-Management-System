package ru.santurov.task_management_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.santurov.task_management_system.DTO.task.*;
import ru.santurov.task_management_system.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создание задания")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        TaskResponseDTO taskResponseDTO = taskService.createTask(taskCreateDTO);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Редактирование задания по id")
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskResponseDTO taskResponseDTO = taskService.updateTask(taskUpdateDTO, id);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Задание по id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.getTask(id);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Все задания(постранично)")
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<TaskResponseDTO>> getTasks(
            @Parameter(description = "Номер страницы для пагинации", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы для пагинации", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponseDTO<TaskResponseDTO> paginatedTasks = taskService.getTasks(page, size);
        return ResponseEntity.ok(paginatedTasks);
    }

    @Operation(summary = "Удалить задание по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}