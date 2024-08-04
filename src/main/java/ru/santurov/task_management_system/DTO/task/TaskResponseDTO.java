package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.santurov.task_management_system.DTO.UserDTO;

import java.util.List;

@Data
@Schema(description = "DTO для ответа с заданием")
public class TaskResponseDTO {

    @Schema(description = "ID задания")
    private Long id;

    @Schema(description = "Название задания")
    @NotBlank(message = "Название не может быть пустым")
    private String title;

    @Schema(description = "Описание задания")
    private String description;

    @Schema(description = "Статус задания (возможные значения: PENDING, PROCESSING, COMPLETED)")
    private String status;

    @Schema(description = "Приоритет задания (возможные значения: HIGH, MEDIUM, LOW)")
    private String priority;

    @Schema(description = "Автор задания")
    private UserDTO  author;

    @Schema(description = "Исполнитель задания")
    private List<UserDTO> performers;
}
