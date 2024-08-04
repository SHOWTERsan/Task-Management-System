package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.DTO.task.TaskUpdateDTO;
import ru.santurov.task_management_system.models.Task;

@Service
@RequiredArgsConstructor
public class TaskAuthorizationService {

    private final UserResolver userResolver;

    public boolean canUpdateTask(Task task) {
        return task.getAuthor().getId().equals(userResolver.resolveCurrentUser().getId());
    }

    public boolean canUpdateTaskStatus(Task task, TaskUpdateDTO taskUpdateDTO) {
        boolean isOnlyStatus = taskUpdateDTO.areOtherFieldsNull(); // все ли поля поля пусты, кроме status
        boolean isPerformer = task.getPerformers().stream()
                .anyMatch(performer ->  performer.getId().equals(userResolver.resolveCurrentUser().getId()));

        return taskUpdateDTO.getStatus() != null && isOnlyStatus && isPerformer;
    }
}
