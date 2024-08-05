package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.santurov.task_management_system.DTO.task.TaskUpdateDTO;
import ru.santurov.task_management_system.exceptions.InsufficientPermissionsException;
import ru.santurov.task_management_system.models.Task;

@Service
@RequiredArgsConstructor
public class TaskAuthorizationService {

    private final UserResolver userResolver;

    public boolean canUpdateTask(Task task) {
        boolean isAuthor = task.getAuthor().getId().equals(userResolver.resolveCurrentUser().getId());
        if (!isAuthor) {
            return false; // Если не автор, то редактирование всех полей невозможно
        }
        return true;
    }

    // Метод для проверки, может ли текущий пользователь редактировать только статус задачи (исполнитель)
    public boolean canUpdateTaskStatus(Task task, TaskUpdateDTO taskUpdateDTO) {
        boolean isOnlyStatus = taskUpdateDTO.areOtherFieldsNull(); // Проверка, что редактируется только статус
        boolean isPerformer = task.getPerformers().stream()
                .anyMatch(performer -> performer.getId().equals(userResolver.resolveCurrentUser().getId()));
        if (isPerformer && taskUpdateDTO.getStatus() != null && isOnlyStatus) {
            return true;
        }
        return false;
    }
}
