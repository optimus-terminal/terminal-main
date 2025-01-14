package org.fyp24064.im.handler;

import org.fyp24064.im.model.ChatMessage;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class ConnectionSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        /*
        After connecting with the websocket server, the client subscribes to a specific path,
        waiting for any types of messages through the path
        If we need to subscribe to multiple paths, then our handler can be different for different POJOs
         */
        session.subscribe("/subscribe/chat/messages/1", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }
    @Override
    public void handleException(StompSession session, @Nullable StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Error:" + exception);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        /* Logic for retrieving
        1. Store in the respective ChatRoom
        2. Refresh the loading of the UI
        */
        ChatMessage message = (ChatMessage) payload;
        System.out.println(message.getContent());
        System.out.println(message.getSender());
        System.out.println(message.getRoomId());
    }
}
