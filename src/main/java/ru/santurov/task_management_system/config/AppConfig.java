package ru.santurov.task_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.santurov.task_management_system.repositories.UserRepository;
import ru.santurov.task_management_system.services.UserResolver;
import ru.santurov.task_management_system.services.UserResolverImpl;

@Configuration
public class AppConfig {

    @Bean
    public UserResolver userResolver(UserRepository userRepository) {
        return new UserResolverImpl(userRepository);
    }
}
