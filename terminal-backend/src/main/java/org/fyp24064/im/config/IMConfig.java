package org.fyp24064.im.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IMConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
