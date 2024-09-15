package org.fyp24064.model;


import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    private String messageSender;
    private String messageReceiver;
    private String messageChannel;
    private String messageContent;

}
