package org.fyp24064.models;

public class Message {
    private String sender;
    private String text;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    // Getter for sender
    public String getSender() {
        return sender;
    }

    // Setter for sender
    public void setSender(String sender) {
        this.sender = sender;
    }

    // Getter for message text
    public String getText() {
        return text;
    }

    // Setter for message text
    public void setText(String text) {
        this.text = text;
    }

}
