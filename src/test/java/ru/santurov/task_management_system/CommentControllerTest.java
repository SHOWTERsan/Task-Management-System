package ru.santurov.task_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.santurov.task_management_system.DTO.UserDTO;
import ru.santurov.task_management_system.DTO.comment.CommentCreateDTO;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.exceptions.ResourceNotFoundException;
import ru.santurov.task_management_system.services.CommentService;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private final String BASE_URL = "/api/comments";

    @Test
    @WithMockUser
    void createComment_Success() throws Exception {
        // Prepare test data
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO();
        commentCreateDTO.setText("This is a test comment");

        UserDTO authorDTO = new UserDTO();
        authorDTO.setUsername("testUser");

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setText("This is a test comment");
        commentResponseDTO.setAuthor(authorDTO);

        // Mock the service call
        when(commentService.createComment(any(CommentCreateDTO.class), eq(1L)))
                .thenReturn(commentResponseDTO);

        // Perform the request and verify the response
        mockMvc.perform(post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("This is a test comment"))
                .andExpect(jsonPath("$.author.username").value("testUser"));

        verify(commentService, times(1)).createComment(any(CommentCreateDTO.class), eq(1L));
    }

    @Test
    @WithMockUser
    void createComment_InvalidInput() throws Exception {
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO();
        commentCreateDTO.setText("");

        mockMvc.perform(post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateDTO)))
                .andExpect(status().isBadRequest());

        verify(commentService, times(0)).createComment(any(CommentCreateDTO.class), anyLong());
    }

    @Test
    @WithMockUser
    void createComment_TaskNotFound() throws Exception {
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO();

        commentCreateDTO.setText("This is a test comment");

        // Mock the service call to throw a NotFoundException
        when(commentService.createComment(any(CommentCreateDTO.class), eq(1L)))
                .thenThrow(new ResourceNotFoundException("Задачи с id " + 1L + " не существует"));

        // Perform the request and expect a 404 Not Found status
        mockMvc.perform(post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateDTO)))
                .andExpect(status().isNotFound());
    }
}
