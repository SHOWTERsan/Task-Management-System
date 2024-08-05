package ru.santurov.task_management_system.DTO.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO для создания комментария")
public class CommentCreateDTO {
    @Schema(description = "Содержание комментария")
    @NotBlank(message = "Содержимое комментария не может быть пустым")
    private String text;
}
