package org.fyp24064.im;

import lombok.Getter;
import lombok.Setter;
import org.fyp24064.im.ChatMessage;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ChatRoom implements Serializable {
    private int roomId;
    private String roomTitle;
    private List<String> members;
    private String lastMessage;
    private List<ChatMessage> messages;
}
