package org.fyp24064.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.SplitPane;

import java.io.IOException;

public class ChatAppController {

    @FXML
    private SplitPane splitPane;

    private ChatListController chatListController;
    private MessageListController messageListController;

    @FXML
    private void initialize() {
        try {
            // Load ChatList.fxml and get its controller
            FXMLLoader chatListLoader = new FXMLLoader(getClass().getResource("/org.fyp24064/ChatList.fxml"));
            AnchorPane chatListPane = chatListLoader.load();
            chatListController = chatListLoader.getController();

            // Load MessageList.fxml and get its controller
            FXMLLoader messageListLoader = new FXMLLoader(getClass().getResource("/org.fyp24064/MessageList.fxml"));
            AnchorPane messageListPane = messageListLoader.load();
            messageListController = messageListLoader.getController();

            splitPane.getItems().addAll(chatListPane, messageListPane);

            chatListController.setOnChatSelected(chatId -> {
                messageListController.displayMessages(chatId); // Pass chatId to MessageListController
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading included FXML files: " + e.getMessage());
        }
    }

}