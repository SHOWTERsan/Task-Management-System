package ru.santurov.task_management_system.DTO.task;

import lombok.Data;

@Data
public class BaseTaskDTO {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long authorId;
    private Long performerId;
}
