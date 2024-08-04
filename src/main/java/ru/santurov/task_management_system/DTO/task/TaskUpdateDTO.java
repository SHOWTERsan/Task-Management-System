package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для обновления задания")
public class TaskUpdateDTO extends BaseTaskDTO {

    @Pattern(regexp = "PENDING|PROCESSING|COMPLETED", message = "Указан неверный статус")
    @Schema(description = "Статус задания (возможные значения: PENDING, PROCESSING, COMPLETED)", example = "COMPLETED")
    private String status;

    public boolean areOtherFieldsNull() {
        return Stream.of(getTitle(), getDescription(), getPriority()).allMatch(Objects::isNull) && (getPerformers() == null || getPerformers().isEmpty());
    }
}
