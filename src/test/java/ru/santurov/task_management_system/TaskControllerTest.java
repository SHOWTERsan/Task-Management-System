package ru.santurov.task_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.DTO.comment.TaskCommentResponseDTO;
import ru.santurov.task_management_system.DTO.task.PaginatedResponseDTO;
import ru.santurov.task_management_system.DTO.task.TaskCreateDTO;
import ru.santurov.task_management_system.DTO.task.TaskResponseDTO;
import ru.santurov.task_management_system.DTO.task.TaskUpdateDTO;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.services.TaskService;
import ru.santurov.task_management_system.services.UserResolver;
import ru.santurov.task_management_system.services.mapper.TaskMapper;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
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


    @Test
    @WithMockUser
    void createTask_Success() throws Exception {
        taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("Fix bug in login");
        taskCreateDTO.setDescription("Fix the bug that causes login failure for users");
        taskCreateDTO.setPriority("HIGH");
        taskCreateDTO.setPerformers(List.of("Jon", "Anna", "Vasya"));

        taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1L);

        Task task = taskMapper.toTask(taskCreateDTO, userResolver);
        taskResponseDTO = taskMapper.toTaskResponseDTO(task);

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
        taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateTask_Success() throws Exception {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setTitle("Updated Title");

        TaskResponseDTO updatedTaskResponseDTO = new TaskResponseDTO();
        updatedTaskResponseDTO.setId(1L);
        updatedTaskResponseDTO.setTitle("Updated Title");

        when(taskService.updateTask(any(TaskUpdateDTO.class), eq(1L))).thenReturn(updatedTaskResponseDTO);

        mockMvc.perform(patch(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(taskService, times(1)).updateTask(any(TaskUpdateDTO.class), eq(1L));
    }

    @Test
    @WithMockUser
    void getTask_Success() throws Exception {
        taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1L);
        taskResponseDTO.setTitle("Fix bug in login");

        when(taskService.getTask(1L)).thenReturn(taskResponseDTO);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Fix bug in login"));

        verify(taskService, times(1)).getTask(1L);
    }

    @Test
    @WithMockUser
    void deleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @WithMockUser
    void getTaskWithComments_Success() throws Exception {
        CommentResponseDTO comment = new CommentResponseDTO();
        comment.setText("This is a comment");

        TaskCommentResponseDTO taskCommentResponseDTO = new TaskCommentResponseDTO();
        taskCommentResponseDTO.setId(1L);
        taskCommentResponseDTO.setTitle("Task with comments");
        taskCommentResponseDTO.setComments(List.of(comment));

        when(taskService.getTaskWithComments(1L)).thenReturn(taskCommentResponseDTO);

        mockMvc.perform(get(BASE_URL + "/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Task with comments"))
                .andExpect(jsonPath("$.comments[0].text").value("This is a comment"));

        verify(taskService, times(1)).getTaskWithComments(1L);
    }

    @Test
    @WithMockUser
    void getTasksByAuthorWithComments_Success() throws Exception {
        CommentResponseDTO comment = new CommentResponseDTO();
        comment.setText("This is a comment by author");

        TaskCommentResponseDTO taskCommentResponseDTO = new TaskCommentResponseDTO();
        taskCommentResponseDTO.setId(1L);
        taskCommentResponseDTO.setTitle("Task with comments");
        taskCommentResponseDTO.setComments(List.of(comment));

        PaginatedResponseDTO<TaskCommentResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                List.of(taskCommentResponseDTO), 0, 1, 1L);

        when(taskService.getTasksByAuthorWithComments(eq(1L), anyInt(), anyInt(), any(), any()))
                .thenReturn(paginatedResponse);

        mockMvc.perform(get(BASE_URL + "/author/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Task with comments"))
                .andExpect(jsonPath("$.data[0].comments[0].text").value("This is a comment by author"));

        verify(taskService, times(1)).getTasksByAuthorWithComments(eq(1L), anyInt(), anyInt(), any(), any());
    }

    @Test
    @WithMockUser
    void getTasksByPerformerWithComments_Success() throws Exception {
        CommentResponseDTO comment = new CommentResponseDTO();
        comment.setText("This is a comment by performer");

        TaskCommentResponseDTO taskCommentResponseDTO = new TaskCommentResponseDTO();
        taskCommentResponseDTO.setId(1L);
        taskCommentResponseDTO.setTitle("Task with comments");
        taskCommentResponseDTO.setComments(List.of(comment));

        PaginatedResponseDTO<TaskCommentResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                List.of(taskCommentResponseDTO), 0, 1, 1L);

        when(taskService.getTasksByPerformerWithComments(eq(1L), anyInt(), anyInt(), any(), any()))
                .thenReturn(paginatedResponse);

        mockMvc.perform(get(BASE_URL + "/performer/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Task with comments"))
                .andExpect(jsonPath("$.data[0].comments[0].text").value("This is a comment by performer"));

        verify(taskService, times(1)).getTasksByPerformerWithComments(eq(1L), anyInt(), anyInt(), any(), any());
    }
}