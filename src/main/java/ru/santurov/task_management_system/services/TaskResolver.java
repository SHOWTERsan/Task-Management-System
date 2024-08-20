package ru.santurov.task_management_system.services;

import ru.santurov.task_management_system.models.Task;

public interface TaskResolver {

    Task resolveTaskById(Long id);
}
