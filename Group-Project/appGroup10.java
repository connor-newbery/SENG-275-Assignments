package org.jfree.part_2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.demo2.BarChartDemo1;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;

//this part of the app was written by Charlie Wager

//extends Application frame to ensure compatability with the bar chart demo methods that will be copied
public class appGroup10 extends ApplicationFrame {

    //logic from barChartDemo
    public appGroup10(String title) {
        super(title);
        CategoryDataset dataset = createBarChartDataset.createBarChartDataset("select * from tracks");
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);

        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(850, 600));
        setContentPane(chartPanel);
    }

    //taken from bar chart demo and then refactored using intelliJ
    private static JFreeChart createChart(CategoryDataset dataset) {
        return getjFreeChart(dataset);
    }

    public static JFreeChart getjFreeChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Songs in the Database under 40 000 milliseconds", null /* x-axis label*/,
                "Milliseconds" /* y-axis label */, dataset);

        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }

    public static void main(String[] args) {
        //taken from barchart demo
        appGroup10 demo = new appGroup10("Group 10 CHART");
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
