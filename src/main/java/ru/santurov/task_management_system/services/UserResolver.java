package ru.santurov.task_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.santurov.task_management_system.models.User;
import ru.santurov.task_management_system.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserResolver {
    private final UserRepository userRepository;

    public User resolveByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }
}
