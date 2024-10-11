package org.fyp24064;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.jfree.chart.*;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CandlestickChart extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    // only for local testing now
    private final String filePath = "./terminal-ui/src/main/java/org/fyp24064/BTC_OHLC.csv";
    private TextField startDate = new TextField("2023-10-06");
    private TextField endDate = new TextField("2024-10-06");
    private OHLCDataset dataset;

    @Override
    public void start(Stage primaryStage) {
        dataset = createDataset();
        constructStage(primaryStage);
    }

    private void constructStage(Stage primaryStage) {
        JFreeChart chart = createChart();
        ChartViewer viewer = new ChartViewer(chart);

        Button enterButton = new Button("Enter");
        enterButton.setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
        enterButton.setOnAction(e -> {
            try {
                if (validateDates(startDate.getText(), endDate.getText())) {
                    dataset = createDataset();
                    viewer.setChart(createChart());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // borderpane fits only 5 elements: top, bottom, left, center, and right
        BorderPane root = new BorderPane();
        root.setCenter(viewer); // the chart component
        root.setBottom(createInputLayout(startDate, endDate, enterButton)); // the date range input
        root.setStyle("-fx-background-color: #333333;");

        primaryStage.setScene(new Scene(root, 1000, 400));
        primaryStage.setTitle("Candlestick Chart");
        primaryStage.show();
    }

    private HBox createInputLayout(TextField startDateField, TextField endDateField, Button enterButton) {
        HBox inputLayout = new HBox(10);
        Text fromLabel = new Text("From: ");
        fromLabel.setFill(Color.WHITE);
        Text toLabel = new Text("To: ");
        toLabel.setFill(Color.WHITE);
        inputLayout.getChildren().addAll(fromLabel, startDateField, toLabel, endDateField, enterButton);
        inputLayout.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-alignment: center");
        return inputLayout;
    }

    private JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createCandlestickChart("BTC", "Date", "Price", dataset, false);
        chart.setBackgroundPaint(java.awt.Color.DARK_GRAY);
        chart.getTitle().setPaint(java.awt.Color.white);

        // dark mode
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(java.awt.Color.DARK_GRAY);
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
        plot.getDomainAxis().setLabelPaint(java.awt.Color.WHITE);
        plot.getRangeAxis().setLabelPaint(java.awt.Color.WHITE);
        plot.getDomainAxis().setTickLabelPaint(java.awt.Color.WHITE);
        plot.getRangeAxis().setTickLabelPaint(java.awt.Color.WHITE);
        plot.setDomainGridlinePaint(java.awt.Color.GRAY);
        plot.setRangeGridlinePaint(java.awt.Color.GRAY);
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        return chart;
    }

    private OHLCDataset createDataset() {
        OHLCSeriesCollection collection = new OHLCSeriesCollection();
        OHLCSeries series = new OHLCSeries("Stock Data");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // skip header
            reader.readLine();

            Date startDay = dateFormat.parse(startDate.getText());
            Date endDay = dateFormat.parse(endDate.getText());

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Date date = dateFormat.parse(values[0]);
                double open = Double.parseDouble(values[1]);
                double high = Double.parseDouble(values[2]);
                double low = Double.parseDouble(values[3]);
                double close = Double.parseDouble(values[4]);

                // check if the date is within the specified range (inclusive)
                if (!date.before(startDay) && !date.after(endDay)) {
                    series.add(new Day(date), open, high, low, close);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        collection.addSeries(series);
        return collection;
    }

    private boolean validateDates(String startDateStr, String endDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // enforce strict parsing

        try {
            Date startDateParsed = dateFormat.parse(startDateStr);
            Date endDateParsed = dateFormat.parse(endDateStr);

            if (startDateParsed.after(endDateParsed)) {
                showAlert("Invalid Dates", "Start date must be before or equal to end date.");
                return false;
            }
            return true;
        } catch (ParseException e) {
            showAlert("Invalid Date Format", "Please use the format: YYYY-MM-DD");
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}