package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;


}
