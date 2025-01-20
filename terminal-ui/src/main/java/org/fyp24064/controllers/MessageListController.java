package org.fyp24064.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

import java.util.*;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;

import org.fyp24064.im.model.ChatRoom;
import org.fyp24064.models.Message;
import org.springframework.stereotype.Component;

import org.fyp24064.im.service.IMService;
import org.fyp24064.im.model.ChatMessage;
import org.fyp24064.im.model.ChatRoom;


@Component
public class MessageListController {

    private int roomId;
    private String roomTitle;
    private String user;

    @FXML
    private Label groupNameLabel;

    @FXML
    private VBox messageList;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane messageScrollPane; // Added reference to the ScrollPane

    private IMService imService = new IMService();

    private ObservableList<Message> messageObservableList = FXCollections.observableArrayList();

    public ObservableList<Message> getMessageObservableList() {
        return messageObservableList;
    }

    // Initialize the controller
    @FXML
    public void initialize() {

        messageObservableList.addListener((javafx.collections.ListChangeListener.Change<? extends Message> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Message newMessage : change.getAddedSubList()) {
                        addMessageBubble(newMessage); // Add each new message to the UI
                    }
                }
            }
        });

        sendButton.setOnAction(event -> sendMessage());
    }


    public void displayMessages(int chatId) {

        String username = "user4";
        user = username;
        roomId = chatId;
        messageList.getChildren().clear();

        List<ChatRoom> chatRoomList = imService.getChatRoomsOfUser(username);
        List<ChatMessage> chatMessageList = imService.getChatMessages(chatId);

        for (ChatRoom chatRoom : chatRoomList) {

            if (chatRoom.getRoomId() == chatId){
                roomTitle = chatRoom.getRoomTitle();
                groupNameLabel.setText(roomTitle);
                break;
            }
        }
        for (ChatMessage chatMessage : chatMessageList) {

            String message = chatMessage.getContent();
            String sender = chatMessage.getSender();
            addMessageBubble(new Message(sender, message));
        }
        scrollToBottom();
    }


    private void addMessageBubble(Message message) {
        try {
            // Load the MessageBubble.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.fyp24064/MessageBubble.fxml"));
            Node messageNode = loader.load();

            // Get the controller for the MessageBubble
            MessageBubbleController controller = loader.getController();

            // Set the data for the message bubble
            controller.setSender(message.getSender());
            controller.setMessage(message.getText());

            // Add the message bubble to the VBox
            messageList.getChildren().add(messageNode);
            VBox.setMargin(messageNode, new Insets(10, 0, 10, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendMessage() {

        String text = messageInput.getText();

        if (text == null || text.isEmpty()) {
            return; // Ignore empty messages
        }
        try{
            imService.sendMessageToRoom(text, roomId, user);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed to send the message. Please try again.");
        }


        Message newMessage = new Message("You", text);
        addMessageBubble(newMessage);

        // Clear the input field
        messageInput.clear();
        scrollToBottom();
    }

    // Get the current time as a string
    private String getCurrentTime() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
    }

    private void scrollToBottom() {
        Platform.runLater(() -> {

        messageScrollPane.layout(); // Ensure layout is updated before scrolling
        messageScrollPane.setVvalue(1.0); // Scroll to the bottom
        });
    }
}