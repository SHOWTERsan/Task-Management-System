package ru.santurov.task_management_system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.santurov.task_management_system.DTO.JwtAuthenticationResponseDTO;
import ru.santurov.task_management_system.DTO.SignInRequestDTO;
import ru.santurov.task_management_system.DTO.SignUpRequestDTO;
import ru.santurov.task_management_system.models.Role;
import ru.santurov.task_management_system.models.User;
import ru.santurov.task_management_system.services.AuthenticationService;
import ru.santurov.task_management_system.services.JwtService;
import ru.santurov.task_management_system.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void testSignUp() {
        SignUpRequestDTO signUpRequest = new SignUpRequestDTO();
        signUpRequest.setUsername("Jon");
        signUpRequest.setEmail("jondoe@gmail.com");
        signUpRequest.setPassword("my!1secret1#password");

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .role(Role.ROLE_USER)
                .build();

        when(userService.create(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("test-jwt-token");

        JwtAuthenticationResponseDTO response = authenticationService.signUp(signUpRequest);

        assertEquals("test-jwt-token", response.getToken());
    }

    @Test
    void testSignIn() {
        UserDetailsService userDetailsServiceMock = mock(UserDetailsService.class);

        User user = new User();
        user.setUsername("Jon");
        user.setPassword("my!1secret1#password");

        when(userDetailsServiceMock.loadUserByUsername("Jon")).thenReturn(user);
        when(userService.userDetailsService()).thenReturn(userDetailsServiceMock);
        when(jwtService.generateToken(any(User.class))).thenReturn("test-jwt-token");

        SignInRequestDTO signInRequest = new SignInRequestDTO();
        signInRequest.setUsername("Jon");
        signInRequest.setPassword("my!1secret1#password");

        JwtAuthenticationResponseDTO response = authenticationService.signIn(signInRequest);

        assertEquals("test-jwt-token", response.getToken());
    }

}
