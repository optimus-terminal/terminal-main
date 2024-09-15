package org.fyp24064;

import org.fyp24064.config.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


@SpringBootApplication
public class InstantMessagingRedisApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantMessagingRedisApplication.class);
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private Receiver receiver;
    @EventListener(ApplicationReadyEvent.class)
    public void sendMessage() throws InterruptedException {
        // Dependency Injection does not work on static methods
        while (receiver.getCount() == 0) {
            LOGGER.info("Sending messages");
            template.convertAndSend("chat", "Hello World");
            Thread.sleep(500L);
        }
        System.exit(0);
    }
    public static void main(String[] args) {
        SpringApplication.run(InstantMessagingRedisApplication.class, args);
    }
}