package org.fyp24064;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import com.panemu.tiwulfx.control.dock.DetachableTabPane;

public class Tabs extends Application {
    private DetachableTabPane tabPane;
    public static void run(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        tabPane = new DetachableTabPane();
        tabPane.getStyleClass().add(DetachableTabPane.STYLE_CLASS_FLOATING);

        // Control panel for adding new tabs
        Label stockLabel = new Label("Name of stock: ");
        stockLabel.setStyle("-fx-text-fill: white;");
        ComboBox<String> stockName = new ComboBox<>();
        stockName.getItems().addAll(
                "AAPL", "BTC-USD", "ETH-USD", "GOOG", "INTC", "NVDA", "TSLA"
        );
        stockName.setStyle("-fx-background-color: #424242;");
        stockName.setCellFactory(stringListView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
            }
        });
        stockName.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
            }
        });
        Label chartLabel = new Label("Type of chart: ");
        chartLabel.setStyle("-fx-text-fill: white;");
        ComboBox<String> chartName = new ComboBox<>();
        chartName.getItems().addAll(
                "Candlestick Chart (OHLC)", "Bar Chart (Volume)"
        );
        chartName.setStyle("-fx-background-color: #424242;");
        chartName.setCellFactory(stringListView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
            }
        });
        chartName.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
            }
        });
        Button addTabButton = new Button("Create tab");
        addTabButton.setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
        addTabButton.setOnAction(actionEvent -> {
            Tab tab = new Tab(stockName.getValue() + " " + chartName.getValue(),
                    (Objects.equals(chartName.getValue(), "Candlestick Chart (OHLC)"))?
                            new CandlestickChart().constructNode(stockName.getValue()) :
                            new BarChart().constructNode(stockName.getValue())
            );
            tab.setStyle("-fx-background-color: #424242; -fx-text-base-color: white;");
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();
        });

        HBox controls = new HBox(stockLabel, stockName, chartLabel, chartName, addTabButton);
        controls.setStyle("-fx-background-color: #333333; -fx-alignment: center;");

        SplitPane sp = new SplitPane(tabPane);
        sp.setStyle("-fx-background-color: #333333;");
        VBox screen = new VBox(controls, sp);
        screen.setStyle("-fx-padding: 10; -fx-background-color: #333333;");
        VBox.setVgrow(sp, Priority.ALWAYS);

        primaryStage.setScene(new Scene(screen, 1000, 600));
        primaryStage.setTitle("Optimus");
        primaryStage.show();

    }
}
