package org.fyp24064.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.List;
import javafx.geometry.Insets;

import javafx.scene.control.ScrollPane;

import org.fyp24064.models.Message;

public class MessageListController {

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

    // Initialize the controller
    @FXML
    public void initialize() {
        // Set up the Send Button action
        sendButton.setOnAction(event -> sendMessage());
        //mockMessageData();
    }

    public Map<Integer, List<Message>> messageData = new HashMap<>();


    public void displayMessages(int chatId) {
        messageList.getChildren().clear();
        String serverIP = "http://localhost:8080";
        String getChatRoomPath = "/chat/messages/";
        String apiUrI = serverIP + getChatRoomPath + String.valueOf(chatId);

        // TODO: HTTP Request based on chatId

        try {
            // Send HTTP Request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrI))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                String responseBody = response.body();

                // Parse the JSON response into a JsonNode tree
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseBody);

                // Create a list to hold the Message objects
                List<Message> messages = new ArrayList<>();

                // Loop over each JSON object in the array
                for (JsonNode node : rootNode) {

                    String sender = node.get("sender").asText();
                    String text = node.get("content").asText();

                    messages.add(new Message(sender, null, text)); // Pass null for "time" if you don't have it
                }

                if (!messages.isEmpty()) {
                    // Set the group name dynamically
                    groupNameLabel.setText("Chat " + chatId);

                    // Add all messages to the UI
                    for (Message message : messages) {
                        addMessageBubble(message);
                    }
                    scrollToBottom();
                } else {
                    groupNameLabel.setText("No Messages");
                }
            } else {
                System.err.println("Failed to fetch chat messages: HTTP " + response.statusCode());
                groupNameLabel.setText("Error: Could not load messages");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching chat messages: " + e.getMessage());
            groupNameLabel.setText("Error: Could not load messages");
        }

    }

    // Add a message bubble to the list
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

    // Handle sending a new message
    private void sendMessage() {
        String text = messageInput.getText();
        if (text == null || text.isEmpty()) {
            return; // Ignore empty messages
        }

        // Add the message to the UI (as if it were sent by the user)
        Message newMessage = new Message("You", getCurrentTime(), text);
        addMessageBubble(newMessage);

        // Clear the input field
        messageInput.clear();
        scrollToBottom();
        // MessageService.saveMessage(newMessage);
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