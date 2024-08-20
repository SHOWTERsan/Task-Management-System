package ru.santurov.task_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.santurov.task_management_system.repositories.CommentRepository;
import ru.santurov.task_management_system.repositories.TaskRepository;
import ru.santurov.task_management_system.repositories.UserRepository;
import ru.santurov.task_management_system.services.*;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    public TaskResolver taskResolver(TaskRepository taskRepository) {
        return new TaskResolverImpl(taskRepository);
    }

    @Bean
    @Primary
    public UserResolver userResolver(UserRepository userRepository) {
        return new UserResolverImpl(userRepository);
    }
}
