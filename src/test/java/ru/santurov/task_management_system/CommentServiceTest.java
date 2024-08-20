package ru.santurov.task_management_system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.santurov.task_management_system.DTO.UserDTO;
import ru.santurov.task_management_system.DTO.comment.CommentCreateDTO;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.models.Comment;
import ru.santurov.task_management_system.models.Task;
import ru.santurov.task_management_system.models.User;
import ru.santurov.task_management_system.repositories.CommentRepository;
import ru.santurov.task_management_system.services.CommentService;
import ru.santurov.task_management_system.services.TaskResolver1;
import ru.santurov.task_management_system.services.UserResolver;
import ru.santurov.task_management_system.services.mapper.CommentMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserResolver userResolver;

    @Mock
    private TaskResolver1 taskResolver;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_Success() {
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO();
        commentCreateDTO.setText("This is a test comment");

        Task task = new Task();
        task.setId(1L);
        User author = new User();
        author.setUsername("testUser");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setText("This is a test comment");

        UserDTO user = new UserDTO();
        user.setUsername("testUser");

        CommentResponseDTO expectedResponse = new CommentResponseDTO();
        expectedResponse.setText("This is a test comment");
        expectedResponse.setAuthor(user);

        when(commentMapper.toComment(eq(commentCreateDTO), eq(1L)))
                .thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toCommentResponse(comment)).thenReturn(expectedResponse);

        CommentResponseDTO actualResponse = commentService.createComment(commentCreateDTO, 1L);

        assertEquals(expectedResponse, actualResponse);
        verify(commentRepository, times(1)).save(comment);
    }
}
