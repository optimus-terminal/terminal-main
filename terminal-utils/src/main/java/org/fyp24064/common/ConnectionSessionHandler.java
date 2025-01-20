package org.fyp24064.common;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ConnectionSessionHandler extends StompSessionHandlerAdapter {
    private HashMap<String, StompFrameHandler> subscriptionPaths;
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        for (Map.Entry<String, StompFrameHandler> subscriptionPath : subscriptionPaths.entrySet()) {
            session.subscribe(subscriptionPath.getKey(), subscriptionPath.getValue());
        }
        System.out.println("Connected!");
    }

    @Override
    public void handleException(StompSession session, @Nullable StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Error:" + exception);
    }

}
