package org.fyp24064.im;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String content;
    private int roomId;
    private String sender;
}
