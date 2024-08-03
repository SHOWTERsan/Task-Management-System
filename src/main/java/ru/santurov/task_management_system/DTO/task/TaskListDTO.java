package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для списка заданий")
public class TaskListDTO extends BaseTaskDTO {

    @Schema(description = "ID задания", example = "1")
    private Long id;

    @Schema(description = "Имя автора задания", example = "Jon Doe")
    private String authorName;

    @Schema(description = "Имя исполнителя задания", example = "Jane Smith")
    private String performerName;
}
