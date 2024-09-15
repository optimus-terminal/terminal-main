package org.fyp24064.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fyp24064.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private final AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) throws JsonProcessingException {
        LOGGER.info("Original: " + message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage chatMessage = objectMapper.readValue(convertMessageFormat(message), ChatMessage.class);
            LOGGER.info("Received message from " + chatMessage.getMessageSender() + ": " + chatMessage.getMessageContent());
            counter.incrementAndGet();
        } catch (JsonProcessingException e) {
            LOGGER.error("Error deserializing message: " + e.getMessage());
            System.exit(0);
        }
    }

    public String convertMessageFormat(String message) {
         return message.substring(1, message.length() - 1).replace("\\\"", "\"");
    }
    public int getCount() {
        return counter.get();
    }
}
