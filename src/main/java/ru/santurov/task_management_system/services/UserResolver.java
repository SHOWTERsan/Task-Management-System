package ru.santurov.task_management_system.services;

import ru.santurov.task_management_system.models.User;

public interface UserResolver {

    User resolveCurrentUser();

    User resolveByUsername(String username);
}
