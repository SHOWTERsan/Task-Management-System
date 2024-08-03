package ru.santurov.task_management_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.santurov.task_management_system.DTO.JwtAuthenticationResponseDTO;
import ru.santurov.task_management_system.DTO.SignInRequestDTO;
import ru.santurov.task_management_system.DTO.SignUpRequestDTO;
import ru.santurov.task_management_system.exceptions.UserAlreadyExistsException;
import ru.santurov.task_management_system.services.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testSuccessfulSignUp() throws Exception {
        SignUpRequestDTO signUpRequest = new SignUpRequestDTO();
        signUpRequest.setUsername("Jon");
        signUpRequest.setEmail("jondoe@gmail.com");
        signUpRequest.setPassword("my!1secret1#password");

        JwtAuthenticationResponseDTO response = new JwtAuthenticationResponseDTO("test-jwt-token");

        when(authenticationService.signUp(any(SignUpRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/sing-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    void testSignUpWithExistingUsername() throws Exception {
        SignUpRequestDTO signUpRequest = new SignUpRequestDTO();
        signUpRequest.setUsername("existingUser");
        signUpRequest.setEmail("newemail@gmail.com");
        signUpRequest.setPassword("my!1secret1#password");

        when(authenticationService.signUp(any(SignUpRequestDTO.class)))
                .thenThrow(new UserAlreadyExistsException("Пользователь с таким именем уже существует"));

        mockMvc.perform(post("/api/auth/sing-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Пользователь с таким именем уже существует"));
    }

    @Test
    void testSignUpWithInvalidEmail() throws Exception {
        SignUpRequestDTO signUpRequest = new SignUpRequestDTO();
        signUpRequest.setUsername("Jon");
        signUpRequest.setEmail("invalid-email");
        signUpRequest.setPassword("my!1secret1#password");

        mockMvc.perform(post("/api/auth/sing-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email адрес должен быть в формате user@example.com"));
    }

    @Test
    void testSuccessfulSignIn() throws Exception {
        SignInRequestDTO signInRequest = new SignInRequestDTO();
        signInRequest.setUsername("Jon");
        signInRequest.setPassword("my!1secret1#password");

        JwtAuthenticationResponseDTO response = new JwtAuthenticationResponseDTO("test-jwt-token");

        when(authenticationService.signIn(any(SignInRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    void testSignInWithInvalidCredentials() throws Exception {
        SignInRequestDTO signInRequest = new SignInRequestDTO();
        signInRequest.setUsername("Jon");
        signInRequest.setPassword("wrongpa2№ssword");

        when(authenticationService.signIn(any(SignInRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Неверное имя пользователя или пароль"));

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Неверное имя пользователя или пароль"));
    }
}
