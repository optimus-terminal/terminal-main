package org.fyp24064.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Consumer;
import org.fyp24064.models.Message;

public class ChatListController {

    @FXML
    private VBox chatList;

    @FXML
    private Label chatNameLabel; // Label to display the username


    // Callback to notify which chat has been selected
    private Consumer<Integer> onChatSelected;
    private AnchorPane selectedChatUnit;

    public void setOnChatSelected(Consumer<Integer> onChatSelected) {
        this.onChatSelected = onChatSelected;
    }

    @FXML
    private void initialize() {


        String username = "user4";
        String serverIP = "http://localhost:8080";
        String getChatRoomPath = "/chat/chatRoom/";
        String apiUrI = serverIP + getChatRoomPath + username;

        chatNameLabel.setText(username);


        // TODO: Remove MOCK DATA
        /**
        chats.add(createChat(1, "Announcements"));
        chats.add(createChat(2, "Crypto Dev Team"));
        chats.add(createChat(3, "AI for Finance"));
        chats.add(createChat(4, "Blockchain Builders"));
        chats.add(createChat(5, "Quant Analysts Hub"));
        chats.add(createChat(6, "TechOps Team"));
        chats.add(createChat(7, "Data Science Squad"));
        chats.add(createChat(8, "Machine Learning Lab"));
        chats.add(createChat(9, "Startup Visionaries"));
        chats.add(createChat(10, "DevOps Excellence"));
         */

        // TODO: API fetch for all groups the users are a part of
        //       GET http://localhost:8080/chat/chatRoom/<username>
        //
        // TODO: For Endpoint testing: starts here: COPY FROM NOTION
        //ChatListController.java

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrI))
                    .GET()
                    .build();

            HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                String responseBody = response.body();
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> chatRooms = mapper.readValue(responseBody, List.class);

                for (Map<String, Object> chatRoom : chatRooms) {
                    // Use the correct keys from the JSON structure
                    Integer roomId = (Integer) chatRoom.get("roomId");
                    String roomTitle = (String) chatRoom.get("roomTitle");

                    // Add the chat room to the list (assuming createChat is a valid method)
                    addChatUnit(
                            roomId,
                            "# " + roomTitle
                    );
                }
            }
            else {
                System.err.println("Failed to fetch chat rooms: HTTP " + response.statusCode());
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println("Error fetching chat rooms: " + e.getMessage());
        }
    }

    // Method to add a chat unit to the chat list
    public void addChatUnit(int chatId, String chatName) {
        try {
            // Load the chatUnit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.fyp24064/chatUnit.fxml"));
            AnchorPane chatUnit = loader.load();

            ChatUnitController chatUnitController = loader.getController();
            chatUnitController.setChatData(chatName);

            chatUnit.setOnMouseClicked(event -> {
                handleChatUnitSelection(chatUnit);
                if (onChatSelected != null) {
                    onChatSelected.accept(chatId);
                }
            });

            // Add the ChatUnit to the chatList VBox
            chatList.getChildren().add(chatUnit);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ChatUnit: " + e.getMessage());
        }
    }

    private void handleChatUnitSelection(AnchorPane chatUnit) {
        // Reset the style of the previously selected chat unit
        if (selectedChatUnit != null) {
            selectedChatUnit.setStyle("-fx-background-color: transparent;");
        }

        // Apply a new style to the currently selected chat unit
        chatUnit.setStyle("-fx-background-color: #228B22;-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;"); // Green background for selected chat
        selectedChatUnit = chatUnit; // Store the currently selected chat unit
    }


}