package org.fyp24064.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageBubbleController {

    @FXML
    private Label senderLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label messageLabel;

    // Assume that all data has been fetched from the postgre database in the MessageListController
    // MessageListController will pass in details to MessageBubbleController for creation

    // Method to set sender's name
    public void setSender(String name) {
        senderLabel.setText(name);
    }

    // Method to set time of the message
    public void setTime(String time) {
        timeLabel.setText(time);
    }

    // Method to set the message text
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}