package ru.santurov.task_management_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignInRequestDTO {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 2, max = 50, message = "Имя пользователя должно содержать от 2 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[^\\w\\s]).{8,}", message = "Пароль должен содержать не менее 8 символов, включая 1 цифру и 1 спецсимвол")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
