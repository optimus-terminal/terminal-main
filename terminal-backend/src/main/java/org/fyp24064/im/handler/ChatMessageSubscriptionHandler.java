package org.fyp24064.im.handler;

import org.fyp24064.im.model.ChatMessage;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class ChatMessageSubscriptionHandler implements StompFrameHandler {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessage message = (ChatMessage) payload;
        System.out.println(message.getContent());
        System.out.println(message.getSender());
        System.out.println(message.getRoomId());

    }
}
