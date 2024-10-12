package org.fyp24064;

import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CandlestickChart extends Chart {

    private TextField MAPeriod = new TextField("10");

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        dataset_OHLC = createDataset();
        constructStage(primaryStage);
    }

    @Override
    protected void constructStage(Stage primaryStage) {
        JFreeChart chart = createChart();
        ChartViewer viewer = new ChartViewer(chart);

        Text MALabel = new Text("Moving Average Period (days): ");
        MALabel.setFill(Color.WHITE);
        MAPeriod.setPrefWidth(40);
        HBox MAInput = new HBox(MALabel, MAPeriod);
        MAInput.setStyle("-fx-alignment: center");

        Button enterButton = new Button("Enter");
        enterButton.setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
        enterButton.setOnAction(e -> {
            try {
                if (validateDates(startDate.getText(), endDate.getText()) && validateMA(MAPeriod.getText())) {
                    dataset_OHLC = createDataset();
                    viewer.setChart(createChart());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox DateInput = createInputLayout(startDate, endDate, enterButton); // the date range input

        // borderpane fits only 5 elements: top, bottom, left, center, and right
        BorderPane root = new BorderPane();
        root.setCenter(viewer); // the chart component
        HBox bottom = new HBox(MAInput, DateInput);
        bottom.setStyle("-fx-alignment: center;");
        root.setBottom(bottom);
        root.setStyle("-fx-background-color: #333333;");

        primaryStage.setScene(new Scene(root, 1000, 400));
        primaryStage.setTitle("Candlestick Chart");
        primaryStage.show();
    }

    protected JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createCandlestickChart("BTC", "Date", "Price", dataset_OHLC, false);
        chart = styleChart(chart);
        XYPlot plot = chart.getXYPlot();
        CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);

        // plotting n-day moving average on top (adj close)
        XYDataset MAdataset = createMADataset();
        MAdataset = MovingAverage.createMovingAverage(MAdataset, "10d-MA", Integer.parseInt(MAPeriod.getText())*24*60*60*1000L, 0L);
        plot.setDataset(1, MAdataset);
        plot.setRenderer(1, new StandardXYItemRenderer());

        return chart;
    }

    private OHLCDataset createDataset() {
        OHLCSeriesCollection collection = new OHLCSeriesCollection();
        OHLCSeries series = new OHLCSeries("Stock OHLC");

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

    private XYDataset createMADataset() {
        TimeSeriesCollection collection = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Stock MA");

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
                double adj_close = Double.parseDouble(values[5]);

                // check if the date is within the specified range (inclusive)
                if (!date.before(startDay) && !date.after(endDay)) {
                    series.add(new Day(date), adj_close);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        collection.addSeries(series);
        return collection;
    }

    private boolean validateMA(String text) {
        try {
            int period = Integer.parseInt(text);
            if (period < 1 || period > 365) {
                showAlert("Invalid Period", "Period must be an integer between 1 and 365.");
                return false;
            }
            return true;
        } catch (Exception e) {
            showAlert("Invalid Period", "Please enter an integer between 1 and 365.");
            return false;
        }
    }

}