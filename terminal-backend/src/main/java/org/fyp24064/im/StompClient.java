package org.fyp24064.im;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fyp24064.im.handler.ChatMessageSubscriptionHandler;
import org.fyp24064.im.service.IMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.fyp24064.common.WebSocketManager;

import java.util.List;
import java.util.Scanner;

import static org.fyp24064.constants.WebSocketConstants.WS_SUBSCRIPTION_PATH;

// Purpose of CommandLineRunner is that it would execute the run() method, which is used to execute some code
// after the spring boot application has started.
// This class serves as a demonstration of the instant messaging API.
@SpringBootApplication
@Component
public class StompClient implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(StompClient.class);
    private static final String URL = "ws://localhost:8080/connect/chat";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static WebSocketManager webSocketManager = WebSocketManager.getInstance();

    public static void main(String[] args) {
        SpringApplication.run(StompClient.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<ChatMessage> chatMessageList = IMService.getChatMessages(1);

        for (ChatMessage chatMessage : chatMessageList) {
            System.out.printf("%s %s %s%n", chatMessage.getContent(), chatMessage.getSender(), chatMessage.getRoomId());
        }

        List<ChatRoom> chatRoomList = IMService.getChatRoomsOfUser("user1");
        for (ChatRoom chatRoom : chatRoomList) {
            System.out.println(chatRoom.getMessages());
        }

        String URL = String.format(WS_SUBSCRIPTION_PATH, "user4");
        System.out.println(URL);
        webSocketManager.addSubscriptionPaths(URL, new ChatMessageSubscriptionHandler());
        webSocketManager.startWebSocket();
        new Scanner(System.in).nextLine();
    }
}