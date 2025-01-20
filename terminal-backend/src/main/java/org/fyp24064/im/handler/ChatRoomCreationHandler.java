package org.fyp24064.im.handler;

import org.fyp24064.im.model.ChatRoom;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class ChatRoomCreationHandler implements StompFrameHandler {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatRoom.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

    }
}
