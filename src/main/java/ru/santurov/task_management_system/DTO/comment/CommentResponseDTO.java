package ru.santurov.task_management_system.DTO.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.santurov.task_management_system.DTO.UserDTO;

@Data
@Schema(description = "Комментарий")
public class CommentResponseDTO {
    @Schema(description = "Содержимое комментария")
    private String text;
    @Schema(description = "Автор комментария")
    private UserDTO author;
}
