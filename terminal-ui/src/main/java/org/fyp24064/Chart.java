package org.fyp24064;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Chart extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    // only for local testing now
    protected final String filePath = "./terminal-ui/src/main/java/org/fyp24064/BTC_OHLC.csv";
    protected TextField startDate = new TextField("2023-10-06");
    protected TextField endDate = new TextField("2024-10-06");
    protected OHLCDataset dataset_OHLC;
    protected IntervalXYDataset dataset_category;

    public abstract void start(Stage primaryStage);

    protected abstract void constructStage(Stage primaryStage);

    protected HBox createInputLayout(TextField startDateField, TextField endDateField, Button enterButton) {
        HBox inputLayout = new HBox(10);
        Text fromLabel = new Text("From: ");
        fromLabel.setFill(Color.WHITE);
        Text toLabel = new Text("To: ");
        toLabel.setFill(Color.WHITE);
        startDateField.setPrefWidth(100);
        endDateField.setPrefWidth(100);
        inputLayout.getChildren().addAll(fromLabel, startDateField, toLabel, endDateField, enterButton);
        inputLayout.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-alignment: center");
        return inputLayout;
    }

    protected abstract JFreeChart createChart();

    protected JFreeChart styleChart(JFreeChart chart) {
        // dark mode
        chart.setBackgroundPaint(java.awt.Color.DARK_GRAY);
        chart.getTitle().setPaint(java.awt.Color.white);
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

    protected boolean validateDates(String startDateStr, String endDateStr) {
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

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}