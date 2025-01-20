package org.fyp24064.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import org.fyp24064.im.ChatRoom;
import org.fyp24064.im.service.IMService;
import org.fyp24064.userData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


@Component
@Controller
public class ChatListController {

    @FXML
    private VBox chatList;

    @FXML
    private Label chatNameLabel;

    private IMService imService = new IMService();

    private String username;
    private Consumer<Integer> onChatSelected;
    private AnchorPane selectedChatUnit;

    private User user;

    @Autowired
    public void setUser(User user) {
        this.user = user;
    }

    public void setOnChatSelected(Consumer<Integer> onChatSelected) {
        this.onChatSelected = onChatSelected;
    }

    @FXML
    private void initialize() {

        //this.username = user.getUsername();
        this.username = "user4";
        chatNameLabel.setText(username);

        List<org.fyp24064.im.ChatRoom> chatRoomList = imService.getChatRoomsOfUser(username);

        for (ChatRoom chatRoom : chatRoomList) {

            int roomID = chatRoom.getRoomId();
            String roomTitle = chatRoom.getRoomTitle();

            addChatUnit(
                    roomID,
                    "# " + roomTitle
            );
        }
    }

    // Method to add a chat unit to the chat list
    public void addChatUnit(int chatId, String chatName) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.fyp24064/ChatUnit.fxml"));
            AnchorPane chatUnit = loader.load();

            ChatUnitController controller = loader.getController();
            controller.setChatData(chatName);

            chatUnit.setOnMouseClicked(event -> {
                handleChatUnitSelection(chatUnit);
                if (onChatSelected != null) {
                    onChatSelected.accept(chatId);
                }
            });

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