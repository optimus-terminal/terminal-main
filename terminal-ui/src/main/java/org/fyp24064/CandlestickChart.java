package org.fyp24064;

import javafx.scene.layout.BorderPane;
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
    private String MAPeriod;
    private OHLCDataset dataset_OHLC;

    @Override
    protected BorderPane constructNode(String[] args) {
        String stock = args[0];
        MAPeriod = (args.length > 1)? args[1] : "0";
        dataset_OHLC = createDataset(stock);
        JFreeChart chart = createChart(stock);
        ChartViewer viewer = new ChartViewer(chart);

        BorderPane root = new BorderPane();
        root.setCenter(viewer);
        root.getStyleClass().add("pane");

        return root;
    }

    protected JFreeChart createChart(String stock) {
        JFreeChart chart = ChartFactory.createCandlestickChart(stock, "Date", "Price", dataset_OHLC, false);
        chart = styleChart(chart);
        XYPlot plot = chart.getXYPlot();
        CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);

        // plotting n-day moving average on top (adj close)
        if (Integer.parseInt(MAPeriod) > 0) {
            XYDataset MAdataset = createMADataset(stock);
            MAdataset = MovingAverage.createMovingAverage(MAdataset, MAPeriod + "d-MA", Integer.parseInt(MAPeriod) * 24 * 60 * 60 * 1000L, 0L);
            plot.setDataset(1, MAdataset);
            plot.setRenderer(1, new StandardXYItemRenderer());
        }

        return chart;
    }

    private OHLCDataset createDataset(String stock) {
        OHLCSeriesCollection collection = new OHLCSeriesCollection();
        OHLCSeries series = new OHLCSeries("Stock OHLC");
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
                double open = Double.parseDouble(values[1]);
                double high = Double.parseDouble(values[2]);
                double low = Double.parseDouble(values[3]);
                double close = Double.parseDouble(values[4]);

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

    private XYDataset createMADataset(String stock) {
        TimeSeriesCollection collection = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Stock MA");
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
                double adj_close = Double.parseDouble(values[5]);

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

}