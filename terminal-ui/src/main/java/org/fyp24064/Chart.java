package org.fyp24064;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Chart {

    // only for local testing now, will replace with data from API
    protected final String filePathPrefix = "./terminal-ui/src/main/resources/";
    protected String startDate = "2023-10-30";
    protected String endDate = "2024-10-30";

    protected abstract Node constructNode(String[] args);

    protected abstract JFreeChart createChart(String stock);

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

    protected XYDataset createMADataset(String stock) {
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