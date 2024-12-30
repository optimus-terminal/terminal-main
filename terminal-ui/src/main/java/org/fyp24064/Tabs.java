package org.fyp24064;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.panemu.tiwulfx.control.dock.DetachableTabPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Tabs extends Application {
    private DetachableTabPane tabPane;
    private TextField input;

    public static void run(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        tabPane = new DetachableTabPane();
        tabPane.getStyleClass().add(DetachableTabPane.STYLE_CLASS_FLOATING);

        HBox controls = createControls();
        controls.getStyleClass().add("pane");

        SplitPane sp = new SplitPane(tabPane);
        sp.getStyleClass().add("pane");
        VBox screen = new VBox(controls, sp);
        screen.getStyleClass().add("pane");
        VBox.setVgrow(sp, Priority.ALWAYS);

        Scene scene = new Scene(screen, 1000, 600);
        scene.getStylesheets().add("stylesheet.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Optimus");
        primaryStage.show();
    }

    private HBox createControls() {
        input = new TextField();
        input.getStyleClass().add("input");
        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) { addTab(input.getText().split(" ")); }
        });
        return new HBox(input);
    }

    private void addTab(String[] args) {
        String tabName;
        Node tabContent;
        Set<String> stocks = Set.of("AAPL", "BTC-USD", "ETH-USD", "GOOG", "INTC", "NVDA", "TSLA");

        if (
                args.length < 2 || args.length > 3
                || (!args[0].equals("im") && !stocks.contains(args[1].toUpperCase()))
                || (args.length == 3 && !args[2].matches("\\d+"))
        ) {
            input.selectAll();
            return;
        }

        if (args.length == 3) {
            if (args[0].equals("line")) {
                String stock = args[1].toUpperCase();
                String MAPeriod = args[2];
                tabName = stock + " Closed " + MAPeriod + "d-MA";
                tabContent = new LineChart().constructNode(new String[] {stock, MAPeriod});
            }
            else if (args[0].equals("candle")) {
                String stock = args[1].toUpperCase();
                String MAPeriod = args[2];
                tabName = stock + " OHLC " + MAPeriod + "d-MA";
                tabContent = new CandlestickChart().constructNode(new String[] {stock, MAPeriod});
            }
            else {
                input.selectAll();
                return;
            }
        }
        else {
            switch (args[0]) {
                case "line" -> {
                    String stock = args[1].toUpperCase();
                    tabName = stock + " Closed";
                    tabContent = new LineChart().constructNode(new String[] {stock});
                }
                case "candle" -> {
                    String stock = args[1].toUpperCase();
                    tabName = stock + " OHLC";
                    tabContent = new CandlestickChart().constructNode(new String[] {stock});
                }
                case "bar" -> {
                    String stock = args[1].toUpperCase();
                    tabName = stock + " Volume";
                    tabContent = new BarChart().constructNode(new String[] {stock});
                }
                case "im" -> {
                    String receiver = args[1];
                    tabName = "IM: " + receiver;
                    tabContent = new Text("Instant Messaging Placeholder");
                }
                case "news" -> {
                    String stock = args[1].toUpperCase();
                    tabName = stock + " News";
                    tabContent = new Text("News Placeholder");
                }
                default -> {
                    input.selectAll();
                    return;
                }
            }
        }

        Tab tab = new Tab(tabName, tabContent);
        tab.getStyleClass().add("tab");
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectLast();
    }
}