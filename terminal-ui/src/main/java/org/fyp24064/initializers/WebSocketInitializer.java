package org.fyp24064.initializers;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import org.fyp24064.im.handler.ChatMessageSubscriptionHandler;
import org.fyp24064.im.service.IMService;
import org.fyp24064.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.fyp24064.common.WebSocketManager;

import static org.fyp24064.constants.WebSocketConstants.WS_SUBSCRIPTION_PATH;

public class WebSocketInitializer {

    private final WebSocketManager webSocketManager = new WebSocketManager();

    public void initializeWebSocket(ObservableList<Message> messageObservableList, String username) {
        // Set up the WebSocket URL
        String URL = String.format(WS_SUBSCRIPTION_PATH, username);
        System.out.println("WebSocket URL: " + URL);

        ChatMessageSubscriptionHandler handler = new ChatMessageSubscriptionHandler(messageObservableList);

        // Add the subscription and start the WebSocket
        webSocketManager.addSubscriptionPaths(URL, handler);
        webSocketManager.startWebSocket();
    }
}