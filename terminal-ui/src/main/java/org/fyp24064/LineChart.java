package org.fyp24064;

import javafx.scene.layout.BorderPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LineChart extends Chart{
    private String MAPeriod;
    private XYDataset dataset_adjClosed;

    @Override
    protected BorderPane constructNode(String[] args) {
        String stock = args[0];
        MAPeriod = (args.length > 1)? args[1] : "0";
        dataset_adjClosed = createDataset(stock);
        JFreeChart chart = createChart(stock);
        ChartViewer viewer = new ChartViewer(chart);

        BorderPane root = new BorderPane();
        root.setCenter(viewer);
        root.getStyleClass().add("pane");

        return root;
    }

    protected JFreeChart createChart(String stock) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(stock, "Date", "Price", dataset_adjClosed);
        chart.removeLegend();
        chart = styleChart(chart);
        XYPlot plot = chart.getXYPlot();

        // plotting n-day moving average on top (adj close)
        if (Integer.parseInt(MAPeriod) > 0) {
            XYDataset MAdataset = createMADataset(stock);
            MAdataset = MovingAverage.createMovingAverage(MAdataset, MAPeriod + "d-MA", Integer.parseInt(MAPeriod) * 24 * 60 * 60 * 1000L, 0L);
            plot.setDataset(1, MAdataset);
            plot.setRenderer(1, new StandardXYItemRenderer());
        }

        return chart;
    }

    private XYDataset createDataset(String stock) {
        TimeSeriesCollection collection = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Stock adjClosed");
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
                double adjClosed = Double.parseDouble(values[5]);

                if (!date.before(startDay) && !date.after(endDay)) {
                    series.add(new Day(date), adjClosed);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        collection.addSeries(series);
        return collection;
    }
}
