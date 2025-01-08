package org.fyp24064;

import javafx.scene.layout.BorderPane;
import org.jfree.chart.*;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.Second;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

import java.util.List;

public class CandlestickChart extends Chart {
    private String MAPeriod;
    private OHLCDataset dataset_OHLC;

    @Override
    protected BorderPane constructNode(String[] args) {
        String stock = args[0];
        MAPeriod = (args.length > 1) ? args[1] : "0";
        dataset_OHLC = createDataset(stock, "1d");
        if (dataset_OHLC == null) {
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
        JFreeChart chart = ChartFactory.createCandlestickChart(stock, "Date", "Price", dataset_OHLC, false);
        chart = styleChart(chart);
        XYPlot plot = chart.getXYPlot();
        CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
        plot.getRangeAxis().setRange(renderer.findRangeBounds(dataset_OHLC));

        // plotting n-day moving average on top (adj close)
        if (Integer.parseInt(MAPeriod) > 0) {
            XYDataset MAdataset = createClosedDataset(stock, "1d");
            MAdataset = MovingAverage.createMovingAverage(MAdataset, MAPeriod + "d-MA", Integer.parseInt(MAPeriod) * 24 * 60 * 60 * 1000L, 0L);
            plot.setDataset(1, MAdataset);
            plot.setRenderer(1, new StandardXYItemRenderer());
        }

        return chart;
    }

    private OHLCDataset createDataset(String stock, String period) {
        OHLCSeriesCollection collection = new OHLCSeriesCollection();
        OHLCSeries series = new OHLCSeries("Stock OHLC");

        try {
            List<StockData.HistoricalQuote> data = stockService.getQuotes(stock, period);
            if (data == null) {
                return null;
            }
            for (StockData.HistoricalQuote quote : data) {
                series.add(
                        new Second(quote.getDate()),
                        quote.getOpen(),
                        quote.getHigh(),
                        quote.getLow(),
                        quote.getClose()
                );
            }
            collection.addSeries(series);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return collection;
    }
}