package ru.santurov.task_management_system.DTO.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.santurov.task_management_system.DTO.task.TaskResponseDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Задача со списком комментрием")
public class TaskCommentResponseDTO extends TaskResponseDTO {
    @Schema(description = "Список комментариев")
    private List<CommentResponseDTO> comments;
}
