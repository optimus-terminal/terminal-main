package org.fyp24064.im;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fyp24064.im.handler.ConnectionSessionHandler;
import org.fyp24064.im.model.ChatMessage;
import org.fyp24064.im.model.ChatRoom;
import org.fyp24064.im.service.IMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

// Purpose of CommandLineRunner is that it would execute the run() method, which is used to execute some code
// after the spring boot application has started.
@SpringBootApplication
@Component
public class StompClient implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(StompClient.class);
    private static final String URL = "ws://localhost:8080/connect/chat";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // We cannot inject a bean into a static variable
    @Autowired
    private IMService imService;

    public static void main(String[] args) {
        SpringApplication.run(StompClient.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<ChatMessage> chatMessageList = imService.getChatMessages(1);

        // TODO: Write methods to display all of those into logs
        for (ChatMessage chatMessage : chatMessageList) {
            System.out.printf("%s %s %s%n", chatMessage.getContent(), chatMessage.getSender(), chatMessage.getRoomId());
        }

        List<ChatRoom> chatRoomList = imService.getChatRoomsOfUser("user1");
        for (ChatRoom chatRoom : chatRoomList) {
            System.out.println(chatRoom.getMessages());
        }

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter(objectMapper));
        StompSessionHandler sessionHandler = new ConnectionSessionHandler();
        stompClient.connectAsync(URL, sessionHandler);
        stompClient.start();

        new Scanner(System.in).nextLine();
    }
}