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
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
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
}