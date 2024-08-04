package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Базовый DTO для задания")
public class BaseTaskDTO {

    @Schema(description = "Название задания", example = "Fix bug in login")
    private String title;

    @Schema(description = "Описание задания", example = "Fix the bug that causes login failure for users")
    private String description;

    @Pattern(regexp = "HIGH|MEDIUM|LOW", message = "Указан неверный приоритет")
    @Schema(description = "Приоритет задания (возможные значения: HIGH, MEDIUM, LOW)", example = "HIGH")
    private String priority;

    @Schema(description = "Исполнители задания", example = "[\"Jon\", \"Anna\", \"Vasya\"]")
    private List<String> performers;
}