package org.fyp24064.im.handler;

import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class ConnectionSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        /*
        After connecting with the websocket server, the client subscribes to a specific path,
        waiting for any types of messages through the path
        If we need to subscribe to multiple paths, then our handler can be different for different POJOs
         */
        session.subscribe("/subscribe/chat/messages/user1", new ChatMessageSubscriptionHandler());
        session.subscribe("/subscribe/chat/creation/user1", new ChatRoomCreationHandler());
    }

    @Override
    public void handleException(StompSession session, @Nullable StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Error:" + exception);
    }

}
