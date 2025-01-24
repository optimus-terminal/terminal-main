package org.fyp24064.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class ChatUnitController {

    @FXML
    private Label chatLabel;

    /**
     * Sets the data for this chat unit.
     *
     * @param chatName      The name of the chat or contact.
     */
    public void setChatData(String chatName) {
        chatLabel.setText(chatName);
    }

    @FXML
    private void initialize() {
        // Optional: Add any initialization logic here if required.
    }
}