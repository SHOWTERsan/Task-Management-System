package ru.santurov.task_management_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.santurov.task_management_system.DTO.JwtAuthenticationResponseDTO;
import ru.santurov.task_management_system.DTO.SignInRequestDTO;
import ru.santurov.task_management_system.DTO.SignUpRequestDTO;
import ru.santurov.task_management_system.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sing-up")
    public JwtAuthenticationResponseDTO signUp(@RequestBody @Valid SignUpRequestDTO request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponseDTO singIn(@RequestBody @Valid SignInRequestDTO request) {
        return authenticationService.signIn(request);
    }
}

