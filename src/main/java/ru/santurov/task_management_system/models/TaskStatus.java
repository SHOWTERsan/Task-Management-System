package ru.santurov.task_management_system.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус задания")
public enum TaskStatus {

    @Schema(description = "Задание ожидает выполнения")
    PENDING,

    @Schema(description = "Задание в процессе выполнения")
    PROCESSING,

    @Schema(description = "Задание завершено")
    COMPLETED
}
