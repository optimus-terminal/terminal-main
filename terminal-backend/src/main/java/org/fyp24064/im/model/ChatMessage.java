package org.fyp24064.im.model;


import jakarta.persistence.*;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false)
    private String messageContent;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false)
    private String messageSender;

    private String messageChannel;

}
