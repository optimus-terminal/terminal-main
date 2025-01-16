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

        if ((args.length < 2 || args.length > 3
                || (args.length == 3 && !args[2].matches("\\d+")))
                && (!args[0].equals("im"))
        ) {
            input.selectAll();
            return;
        }

        String command = args[0].toLowerCase();
        String arg = args[1].toUpperCase();
        String MAPeriod = (args.length == 3)? args[2] : null;

        switch (command) {
            case "line":
                if (MAPeriod == null) {
                    tabName = arg + " Closed";
                    tabContent = new LineChart().constructNode(new String[] {arg});
                } else {
                    tabName = arg + " Closed " + MAPeriod + "d-MA";
                    tabContent = new LineChart().constructNode(new String[] {arg, MAPeriod});
                }
                break;
            case "candle":
                if (MAPeriod == null) {
                    tabName = arg + " OHLC";
                    tabContent = new CandlestickChart().constructNode(new String[] {arg});
                } else {
                    tabName = arg + " OHLC " + MAPeriod + "d-MA";
                    tabContent = new CandlestickChart().constructNode(new String[] {arg, MAPeriod});
                }
                break;
            case "bar":
                tabName = arg + " Volume";
                tabContent = new BarChart().constructNode(new String[] {arg});
                break;
            case "im":
                tabName = "IM: " + arg;
                tabContent = new Text("Instant Messaging Placeholder");
                break;
            case "news":
                tabName = arg + " News";
                tabContent = new NewsDisplay().constructNode(arg);
                break;
            default:
                input.selectAll();
                return;
        }

        if (tabContent == null) {
            input.selectAll();
            return;
        }

        Tab tab = new Tab(tabName, tabContent);
        tab.getStyleClass().add("tab");
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectLast();
    }
}