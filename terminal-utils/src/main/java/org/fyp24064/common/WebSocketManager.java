package org.fyp24064.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.HashMap;

import static org.fyp24064.constants.WebSocketConstants.WS_URL;

public class WebSocketManager {
    @Getter
    private static final WebSocketManager instance = new WebSocketManager();
    private static HashMap<String, StompFrameHandler> subscriptionPaths = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final WebSocketClient client = new StandardWebSocketClient();
    private static final WebSocketStompClient stompClient = new WebSocketStompClient(client);
    private static StompSessionHandler sessionHandler;

    public void startWebSocket() {
        System.out.println(subscriptionPaths);
        sessionHandler = new ConnectionSessionHandler(subscriptionPaths);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter(objectMapper));
        stompClient.connectAsync(WS_URL, sessionHandler);
        stompClient.start();
    }

    public void stopWebSocket() {
        stompClient.stop();
        subscriptionPaths.clear();
    }

    public void addSubscriptionPaths(String path, StompFrameHandler handler) {
        subscriptionPaths.put(path, handler);
    }

    public WebSocketManager() {

    }

}
