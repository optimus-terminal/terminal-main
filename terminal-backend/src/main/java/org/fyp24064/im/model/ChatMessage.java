package org.fyp24064.im.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatMessage {
    private String content;
    private int roomId;
    private String sender;
}
