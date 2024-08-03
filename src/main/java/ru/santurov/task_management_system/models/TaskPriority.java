package ru.santurov.task_management_system.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Приоритет задания")
public enum TaskPriority {

    @Schema(description = "Высокий приоритет")
    HIGH,

    @Schema(description = "Средний приоритет")
    MEDIUM,

    @Schema(description = "Низкий приоритет")
    LOW
}
