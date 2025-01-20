package org.fyp24064.im.handler;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.fyp24064.im.ChatMessage;
import org.fyp24064.models.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class ChatMessageSubscriptionHandler implements StompFrameHandler {

    private ObservableList<Message> obsList;

    public ChatMessageSubscriptionHandler(ObservableList<Message> messageObservableList){
        obsList = messageObservableList;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessage message = (ChatMessage) payload;
        Message msg = new Message(message.getSender(), message.getContent());
        Platform.runLater(() -> obsList.add(msg));

        System.out.println("Received message: " + msg.getText());
    }
}