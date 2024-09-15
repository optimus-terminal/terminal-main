package org.fyp24064;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.fyp24064.config.Receiver;
import org.fyp24064.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * For Instant Messaging functions, we are going to use two features from Redis: Pub/Sub Service and Key-Value Pair Storage
 * For Pub/Sub, it allows real time data streaming between publisher and subscribers.
 * For Key-Value Pair Storage, it allows persistent data to be stored inside the database.
 */
@SpringBootApplication
public class InstantMessagingRedisApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantMessagingRedisApplication.class);
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;
    @Resource
    private RedisTemplate<String, Object> template;
    @Autowired
    private Receiver receiver;
    @EventListener(ApplicationReadyEvent.class)
    public void sendMessage() throws Exception {
        ChatMessage chatMessage = new ChatMessage("123123", "123", "chat", "How is it going?");
        ObjectMapper objectMapper = new ObjectMapper();
        // Dependency Injection does not work on static methods
        while (receiver.getCount() == 0) {
            LOGGER.info("Sending messages " + objectMapper.writeValueAsString(chatMessage));
            template.convertAndSend(chatMessage.getMessageChannel(), objectMapper.writeValueAsString(chatMessage));
            Thread.sleep(500L);
        }
        System.exit(0);
    }
    public static void main(String[] args) {
        SpringApplication.run(InstantMessagingRedisApplication.class, args);
    }
}