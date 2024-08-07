package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для создания задания")
public class TaskCreateDTO extends BaseTaskDTO {
}
