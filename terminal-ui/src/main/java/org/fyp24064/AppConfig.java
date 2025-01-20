package org.fyp24064;

import org.fyp24064.userData.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public User user() {
        return new User();
    }
}
