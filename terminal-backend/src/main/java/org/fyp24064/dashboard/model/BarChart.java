package org.fyp24064.dashboard.model;

import javafx.scene.layout.BorderPane;
import org.jfree.chart.*;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import java.util.List;

public class BarChart extends Chart {
    private IntervalXYDataset dataset_bar;

    @Override
    public BorderPane constructNode(String[] args) {
        String stock = args[0];
        dataset_bar = createDataset(stock, "1y");
        if (dataset_bar == null) {
            return null;
        }
        JFreeChart chart = createChart(stock);
        ChartViewer viewer = new ChartViewer(chart);

        BorderPane root = new BorderPane();
        root.setCenter(viewer);
        root.getStyleClass().add("pane");

        return root;
    }

    protected JFreeChart createChart(String stock) {
        JFreeChart chart = ChartFactory.createXYBarChart(stock, "Date", true,"Volume", dataset_bar);
        chart = styleChart(chart);
        XYBarRenderer renderer = (XYBarRenderer) chart.getXYPlot().getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setDefaultSeriesVisibleInLegend(false);
        renderer.setMargin(0.1);
        return chart;
    }

    private IntervalXYDataset createDataset(String stock, String period) {
        TimeSeries series = new TimeSeries("Stock Volume", "Date", "Volume");
        
        try {
            List<HistoricalQuote> data = stockService.getQuotes(stock, period);
            if (data == null) {
                return null;
            }
            for (HistoricalQuote quote : data) {
                series.addOrUpdate(
                        new Second(quote.getDate()),
                        quote.getVolume()
                );
            }
        } catch (Exception e) {
            return null;
        }
        return new TimeSeriesCollection(series);
    }

}