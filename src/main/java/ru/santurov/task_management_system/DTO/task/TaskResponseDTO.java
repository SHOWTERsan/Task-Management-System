package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.santurov.task_management_system.DTO.UserDTO;
import ru.santurov.task_management_system.models.TaskStatus;

@Data
@Schema(description = "DTO для ответа с заданием")
public class TaskResponseDTO {

    @Schema(description = "ID задания", example = "1")
    private Long id;

    @Schema(description = "Название задания", example = "Fix bug in login")
    @NotBlank(message = "Название не может быть пустым")
    private String title;

    @Schema(description = "Описание задания", example = "Fix the bug that causes login failure for users")
    private String description;

    @Schema(description = "Статус задания (возможные значения: PENDING, PROCESSING, COMPLETED)", example = "COMPLETED")
    private String status;

    @Schema(description = "Приоритет задания (возможные значения: HIGH, MEDIUM, LOW)", example = "HIGH")
    private String priority;

    @Schema(description = "Автор задания")
    private UserDTO  author;

    @Schema(description = "Исполнитель задания")
    private UserDTO performer;
}
