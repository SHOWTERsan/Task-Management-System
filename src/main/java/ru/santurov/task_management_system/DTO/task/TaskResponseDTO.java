package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.santurov.task_management_system.models.User;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для ответа с заданием")
public class TaskResponseDTO extends BaseTaskDTO {

    @Schema(description = "ID задания", example = "1")
    private Long id;

    @Schema(description = "Автор задания")
    private User author;

    @Schema(description = "Исполнитель задания")
    private User performer;
}
