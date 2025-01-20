package org.fyp24064.dashboard.model;

import javafx.scene.layout.BorderPane;
import org.jfree.chart.*;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.XYDataset;

public class LineChart extends Chart {
    private String MAPeriod;
    private XYDataset dataset_Closed;

    @Override
    public BorderPane constructNode(String[] args) {
        String stock = args[0];
        MAPeriod = (args.length > 1) ? args[1] : "0";
        dataset_Closed = createClosedDataset(stock, "1y");
        if (dataset_Closed == null) {
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
        JFreeChart chart = ChartFactory.createTimeSeriesChart(stock, "Date", "Price", dataset_Closed);
        chart.removeLegend();
        chart = styleChart(chart);
        XYPlot plot = chart.getXYPlot();

        // plotting n-day moving average on top (adj close)
        if (Integer.parseInt(MAPeriod) > 0) {
            XYDataset MAdataset = createClosedDataset(stock, "1y");
            MAdataset = MovingAverage.createMovingAverage(MAdataset, MAPeriod + "d-MA", Integer.parseInt(MAPeriod) * 24 * 60 * 60 * 1000L, 0L);
            plot.setDataset(1, MAdataset);
            plot.setRenderer(1, new StandardXYItemRenderer());
        }

        return chart;
    }
}
