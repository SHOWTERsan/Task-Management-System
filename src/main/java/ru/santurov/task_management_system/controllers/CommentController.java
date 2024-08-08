package ru.santurov.task_management_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.santurov.task_management_system.DTO.comment.CommentCreateDTO;
import ru.santurov.task_management_system.DTO.comment.CommentResponseDTO;
import ru.santurov.task_management_system.services.CommentService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(description = "Создание комментария по id задачи")
    @PostMapping("{taskId}")
    public ResponseEntity<CommentResponseDTO> create(@PathVariable Long taskId, @RequestBody @Valid CommentCreateDTO commentCreateDTO) {
        CommentResponseDTO  comment = commentService.createComment(commentCreateDTO, taskId);
        return ResponseEntity.ok(comment);
    }

}

