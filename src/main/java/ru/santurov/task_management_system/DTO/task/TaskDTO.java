package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для задания")
public class TaskDTO extends BaseTaskDTO {

    @Schema(description = "ID задания", example = "1")
    private Long id;
}
