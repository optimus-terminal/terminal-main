package org.fyp24064;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fyp24064.controllers.ChatAppController;
import org.fyp24064.userData.User;

import org.fyp24064.userData.UserHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.fyp24064.im.config.IMConfig;

import java.util.Objects;
import java.util.Scanner;

@SpringBootApplication(scanBasePackages = "org.fyp24064")
public class Main extends Application{

    private ConfigurableApplicationContext springContext;
    private static String username; // Store the username

    @Override
    public void init() throws Exception {
        SpringApplication.run(Main.class);
        // springContext = new SpringApplicationBuilder(Main.class).run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        username = scanner.nextLine().trim();

        User user = new User();
        user.setUsername(username);
        UserHolder.getInstance().setUser(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.fyp24064/ChatApp.fxml"));

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org.fyp24064/styles.css")).toExternalForm());

        ChatAppController chatAppController = loader.getController();

        primaryStage.setTitle("Instant Messaging Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
        Tabs.run(args);
    }
}