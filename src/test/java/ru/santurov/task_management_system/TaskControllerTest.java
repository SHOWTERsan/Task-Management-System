package ru.santurov.task_management_system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.santurov.task_management_system.DTO.UserDTO;
import ru.santurov.task_management_system.DTO.task.TaskCreateDTO;
import ru.santurov.task_management_system.DTO.task.TaskResponseDTO;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.services.TaskService;
import ru.santurov.task_management_system.services.UserResolver;
import ru.santurov.task_management_system.services.mapper.TaskMapper;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserResolver userResolver;

    @Autowired
    private TaskMapper taskMapper;

    private TaskCreateDTO taskCreateDTO;
    private TaskResponseDTO taskResponseDTO;
    private final String BASE_URL = "/api/tasks";
    @BeforeEach
    void setUp() {
        // Initialize DTOs
        taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("Fix bug in login");
        taskCreateDTO.setDescription("Fix the bug that causes login failure for users");
        taskCreateDTO.setPriority("HIGH");
        taskCreateDTO.setPerformers(List.of("Jon", "Anna", "Vasya"));

        taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1L);

        // Convert DTOs using the mapper
        Task task = taskMapper.toTask(taskCreateDTO, userResolver);
        taskResponseDTO = taskMapper.toTaskResponseDTO(task);
    }

    @Test
    @WithMockUser
    void createTask_Success() throws Exception {
        when(taskService.createTask(any(TaskCreateDTO.class))).thenReturn(taskResponseDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Fix bug in login"));

        verify(taskService, times(1)).createTask(any(TaskCreateDTO.class));
    }

    @Test
    @WithMockUser
    void createTask_InvalidInput() throws Exception {
        taskCreateDTO.setTitle("");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isBadRequest());
    }
}