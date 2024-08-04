package ru.santurov.task_management_system.exceptions;

public class InsufficientPermissionsException extends RuntimeException{
    public InsufficientPermissionsException(String message) {
        super(message);
    }
}
