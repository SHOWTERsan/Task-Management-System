package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Базовый DTO для задачи")
public class BaseTaskDTO {

    @Schema(description = "Название задачи", example = "Fix bug in login")
    @NotBlank(message = "Название задачи не может быть пустым", groups = CreateValidationGroup.class)
    private String title;

    @Schema(description = "Описание задачи", example = "Fix the bug that causes login failure for users")
    private String description;

    @Pattern(regexp = "(?i)HIGH|MEDIUM|LOW", message = "Указан неверный приоритет")
    @Schema(description = "Приоритет задачи (возможные значения: HIGH, MEDIUM, LOW)", example = "HIGH")
    @NotBlank(message = "Приоритет задачи не может быть пустым", groups = CreateValidationGroup.class)
    private String priority;

    @Schema(description = "Исполнители задачи", example = "[\"Jon\", \"Anna\", \"Vasya\"]")
    @NotEmpty(message = "Нужно указать исполнителей задачи", groups = CreateValidationGroup.class)
    private List<String> performers;
}