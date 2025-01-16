package org.fyp24064;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fyp24064.controllers.ChatAppController;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.fyp24064/ChatApp.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/org.fyp24064/styles.css").toExternalForm());

        ChatAppController chatAppController = loader.getController();


        primaryStage.setTitle("Instant Messaging Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}