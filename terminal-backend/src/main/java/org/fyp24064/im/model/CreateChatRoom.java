package org.fyp24064.im.model;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
public class CreateChatRoom implements Serializable {
    private String roomTitle;
    private List<String> members;
}
