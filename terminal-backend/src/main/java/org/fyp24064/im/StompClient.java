package org.fyp24064.im;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fyp24064.im.handler.ConnectionSessionHandler;
import org.fyp24064.im.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

/**
 * For Instant Messaging functions, we are going to use two features from Redis: Pub/Sub Service and Key-Value Pair Storage
 * For Pub/Sub, it allows real time data streaming between publisher and subscribers.
 * For Key-Value Pair Storage, it allows persistent data to be stored inside the database.
 */
@SpringBootApplication
public class StompClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StompClient.class);
    private static final String URL = "ws://localhost:8080/connect/chat";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter(objectMapper));
        StompSessionHandler sessionHandler = new ConnectionSessionHandler();
//        stompClient.connectAsync(URL, sessionHandler);
        stompClient.connectAsync(URL, sessionHandler);
        stompClient.start();
        System.out.println(stompClient.isRunning());
        new Scanner(System.in).nextLine();
    }
}