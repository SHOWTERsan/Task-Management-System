package ru.santurov.task_management_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для пользователя")
public class UserDTO {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Jon")
    private String username;

    @Schema(description = "Email пользователя", example = "jondoe@gmail.com")
    private String email;
}
