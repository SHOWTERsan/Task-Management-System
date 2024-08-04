package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Базовый DTO для задания")
public class BaseTaskDTO {

    @Schema(description = "Название задания", example = "Fix bug in login")
    @NotBlank(message = "Название не может быть пустым")
    private String title;

    @Schema(description = "Описание задания", example = "Fix the bug that causes login failure for users")
    private String description;

    @Schema(description = "Приоритет задания (возможные значения: HIGH, MEDIUM, LOW)", example = "HIGH")
    private String priority;

    @Schema(description = "Имя исполнителя задания", example = "Jane Smith")
    private String performerUsername;
}