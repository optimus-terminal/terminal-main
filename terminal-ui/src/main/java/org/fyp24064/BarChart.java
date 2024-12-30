package org.fyp24064;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import org.jfree.chart.*;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BarChart extends Chart {
    private IntervalXYDataset dataset_bar;

    @Override
    protected BorderPane constructNode(String[] args) {
        String stock = args[0];
        dataset_bar = createDataset(stock);
        JFreeChart chart = createChart(stock);
        ChartViewer viewer = new ChartViewer(chart);

        Button enterButton = new Button("Enter");
        enterButton.setStyle("-fx-background-color: #424242; -fx-text-fill: white;");
        enterButton.setOnAction(e -> {
            dataset_bar = createDataset(stock);
            viewer.setChart(createChart(stock));
        });

        BorderPane root = new BorderPane();
        root.setCenter(viewer);
        root.getStyleClass().add("pane");

        return root;
    }

    protected JFreeChart createChart(String stock) {
        JFreeChart chart = ChartFactory.createXYBarChart(stock, "Date", true,"Volume (Billions)", dataset_bar);
        chart = styleChart(chart);
        XYBarRenderer renderer = (XYBarRenderer) chart.getXYPlot().getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setDefaultSeriesVisibleInLegend(false);
        renderer.setMargin(0.1);
        return chart;
    }

    private IntervalXYDataset createDataset(String stock) {
        TimeSeries series = new TimeSeries("Stock Volume", "Date", "Volume");
        String filePath = filePathPrefix + stock + ".csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            reader.readLine();

            Date startDay = dateFormat.parse(startDate);
            Date endDay = dateFormat.parse(endDate);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Date date = dateFormat.parse(values[0]);
                double volume = Double.parseDouble(values[6]) / 1E9;

                if (!date.before(startDay) && !date.after(endDay)) {
                    series.add(new Day(date), volume);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return new TimeSeriesCollection(series);
    }

}