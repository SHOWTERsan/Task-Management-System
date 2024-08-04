package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для создания задания")
public class TaskCreateDTO extends BaseTaskDTO {

    @Schema(description = "Название задания", example = "Fix bug in login")
    @NotBlank(message = "Название не может быть пустым")
    private String title;

}
