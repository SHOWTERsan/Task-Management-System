package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.DTO.JwtAuthenticationResponseDTO;
import ru.santurov.task_management_system.DTO.SignInRequestDTO;
import ru.santurov.task_management_system.DTO.SignUpRequestDTO;
import ru.santurov.task_management_system.models.Role;
import ru.santurov.task_management_system.models.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
    * Регистрация пользователя
    *
    * @param request данные пользователя
    * @return токен
    */
    public JwtAuthenticationResponseDTO signUp(SignUpRequestDTO request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return  new JwtAuthenticationResponseDTO(jwt);
    }


    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponseDTO signIn(SignInRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDTO(jwt);
    }
}
