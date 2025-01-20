package org.fyp24064;

import javafx.scene.Node;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.fyp24064.StockData.HistoricalQuote;

public abstract class Chart {
    protected StockService stockService = new StockService();

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

    protected XYDataset createClosedDataset(String stock, String period) {
        TimeSeriesCollection collection = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Stock MA");

        try {
            for (HistoricalQuote quote : stockService.getQuotes(stock, period)) {
                series.addOrUpdate(new Second(quote.getDate()), quote.getClose());
            }
            collection.addSeries(series);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }

        return collection;
    }
}