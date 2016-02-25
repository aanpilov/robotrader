/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.date.DateUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author aanpilov
 */
public class EWEngine extends ApplicationFrame {

    /**
     * A demonstration application showing a candlestick chart.
     *
     * @param title  the frame title.
     */
    public EWEngine(final String title) {

        super(title);

        final DefaultHighLowDataset dataset = createHighLowDataset();
        final JFreeChart chart = createChart(dataset);
        chart.getXYPlot().setOrientation(PlotOrientation.VERTICAL);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The dataset.
     */
    private JFreeChart createChart(final DefaultHighLowDataset dataset) {
        final JFreeChart chart = ChartFactory.createCandlestickChart(
            "Candlestick Demo",
            "Time", 
            "Value",
            dataset, 
            true
        );
        return chart;        
    }
    
    /**
     * Creates a sample high low dataset.
     *
     * @return a sample high low dataset.
     */
    private DefaultHighLowDataset createHighLowDataset() {

        final Date[] date = new Date[47];
        final double[] high = new double[47];
        final double[] low = new double[47];
        final double[] open = new double[47];
        final double[] close = new double[47];
        final double[] volume = new double[47];

        final int jan = 1;
        final int feb = 2;

        date[0]  = DateUtilities.createDate(2001, jan, 4, 12, 0);
        high[0]  = 47.0;
        low[0]   = 33.0;
        open[0]  = 35.0;
        close[0] = 33.0;
        volume[0] = 100.0;

        date[1]  = DateUtilities.createDate(2001, jan, 5, 12, 0);
        high[1]  = 47.0;
        low[1]   = 32.0;
        open[1]  = 41.0;
        close[1] = 37.0;
        volume[1] = 150.0;

        date[2]  = DateUtilities.createDate(2001, jan, 6, 12, 0);
        high[2]  = 49.0;
        low[2]   = 43.0;
        open[2]  = 46.0;
        close[2] = 48.0;
        volume[2] = 70.0;

        date[3]  = DateUtilities.createDate(2001, jan, 7, 12, 0);
        high[3]  = 51.0;
        low[3]   = 39.0;
        open[3]  = 40.0;
        close[3] = 47.0;
        volume[3] = 200.0;

        date[4]  = DateUtilities.createDate(2001, jan, 8, 12, 0);
        high[4]  = 60.0;
        low[4]   = 40.0;
        open[4]  = 46.0;
        close[4] = 53.0;
        volume[4] = 120.0;

        date[5]  = DateUtilities.createDate(2001, jan, 9, 12, 0);
        high[5]  = 62.0;
        low[5]   = 55.0;
        open[5]  = 57.0;
        close[5] = 61.0;
        volume[5] = 110.0;

        date[6]  = DateUtilities.createDate(2001, jan, 10, 12, 0);
        high[6]  = 65.0;
        low[6]   = 56.0;
        open[6]  = 62.0;
        close[6] = 59.0;
        volume[6] = 70.0;

        date[7]  = DateUtilities.createDate(2001, jan, 11, 12, 0);
        high[7]  = 55.0;
        low[7]   = 43.0;
        open[7]  = 45.0;
        close[7] = 47.0;
        volume[7] = 20.0;

        date[8]  = DateUtilities.createDate(2001, jan, 12, 12, 0);
        high[8]  = 54.0;
        low[8]   = 33.0;
        open[8]  = 40.0;
        close[8] = 51.0;
        volume[8] = 30.0;

        date[9]  = DateUtilities.createDate(2001, jan, 13, 12, 0);
        high[9]  = 47.0;
        low[9]   = 33.0;
        open[9]  = 35.0;
        close[9] = 33.0;
        volume[9] = 100.0;

        date[10]  = DateUtilities.createDate(2001, jan, 14, 12, 0);
        high[10]  = 54.0;
        low[10]   = 38.0;
        open[10]  = 43.0;
        close[10] = 52.0;
        volume[10] = 50.0;

        date[11]  = DateUtilities.createDate(2001, jan, 15, 12, 0);
        high[11]  = 48.0;
        low[11]   = 41.0;
        open[11]  = 44.0;
        close[11] = 41.0;
        volume[11] = 80.0;

        date[12]  = DateUtilities.createDate(2001, jan, 17, 12, 0);
        high[12]  = 60.0;
        low[12]   = 30.0;
        open[12]  = 34.0;
        close[12] = 44.0;
        volume[12] = 90.0;

        date[13]  = DateUtilities.createDate(2001, jan, 18, 12, 0);
        high[13]  = 58.0;
        low[13]   = 44.0;
        open[13]  = 54.0;
        close[13] = 56.0;
        volume[13] = 20.0;

        date[14]  = DateUtilities.createDate(2001, jan, 19, 12, 0);
        high[14]  = 54.0;
        low[14]   = 32.0;
        open[14]  = 42.0;
        close[14] = 53.0;
        volume[14] = 70.0;

        date[15]  = DateUtilities.createDate(2001, jan, 20, 12, 0);
        high[15]  = 53.0;
        low[15]   = 39.0;
        open[15]  = 50.0;
        close[15] = 49.0;
        volume[15] = 60.0;

        date[16]  = DateUtilities.createDate(2001, jan, 21, 12, 0);
        high[16]  = 47.0;
        low[16]   = 33.0;
        open[16]  = 41.0;
        close[16] = 40.0;
        volume[16] = 30.0;

        date[17]  = DateUtilities.createDate(2001, jan, 22, 12, 0);
        high[17]  = 55.0;
        low[17]   = 37.0;
        open[17]  = 43.0;
        close[17] = 45.0;
        volume[17] = 90.0;

        date[18]  = DateUtilities.createDate(2001, jan, 23, 12, 0);
        high[18]  = 54.0;
        low[18]   = 42.0;
        open[18]  = 50.0;
        close[18] = 42.0;
        volume[18] = 150.0;

        date[19]  = DateUtilities.createDate(2001, jan, 24, 12, 0);
        high[19]  = 48.0;
        low[19]   = 37.0;
        open[19]  = 37.0;
        close[19] = 47.0;
        volume[19] = 120.0;

        date[20]  = DateUtilities.createDate(2001, jan, 25, 12, 0);
        high[20]  = 58.0;
        low[20]   = 33.0;
        open[20]  = 39.0;
        close[20] = 41.0;
        volume[20] = 80.0;

        date[21]  = DateUtilities.createDate(2001, jan, 26, 12, 0);
        high[21]  = 47.0;
        low[21]   = 31.0;
        open[21]  = 36.0;
        close[21] = 41.0;
        volume[21] = 40.0;

        date[22]  = DateUtilities.createDate(2001, jan, 27, 12, 0);
        high[22]  = 58.0;
        low[22]   = 44.0;
        open[22]  = 49.0;
        close[22] = 44.0;
        volume[22] = 20.0;

        date[23]  = DateUtilities.createDate(2001, jan, 28, 12, 0);
        high[23]  = 46.0;
        low[23]   = 41.0;
        open[23]  = 43.0;
        close[23] = 44.0;
        volume[23] = 60.0;

        date[24]  = DateUtilities.createDate(2001, jan, 29, 12, 0);
        high[24]  = 56.0;
        low[24]   = 39.0;
        open[24]  = 39.0;
        close[24] = 51.0;
        volume[24] = 40.0;

        date[25]  = DateUtilities.createDate(2001, jan, 30, 12, 0);
        high[25]  = 56.0;
        low[25]   = 39.0;
        open[25]  = 47.0;
        close[25] = 49.0;
        volume[25] = 70.0;

        date[26]  = DateUtilities.createDate(2001, jan, 31, 12, 0);
        high[26]  = 53.0;
        low[26]   = 39.0;
        open[26]  = 52.0;
        close[26] = 47.0;
        volume[26] = 60.0;

        date[27]  = DateUtilities.createDate(2001, feb, 1, 12, 0);
        high[27]  = 51.0;
        low[27]   = 30.0;
        open[27]  = 45.0;
        close[27] = 47.0;
        volume[27] = 90.0;

        date[28]  = DateUtilities.createDate(2001, feb, 2, 12, 0);
        high[28]  = 47.0;
        low[28]   = 30.0;
        open[28]  = 34.0;
        close[28] = 46.0;
        volume[28] = 100.0;

        date[29]  = DateUtilities.createDate(2001, feb, 3, 12, 0);
        high[29]  = 57.0;
        low[29]   = 37.0;
        open[29]  = 44.0;
        close[29] = 56.0;
        volume[29] = 20.0;

        date[30]  = DateUtilities.createDate(2001, feb, 4, 12, 0);
        high[30]  = 49.0;
        low[30]   = 40.0;
        open[30]  = 47.0;
        close[30] = 44.0;
        volume[30] = 50.0;

        date[31]  = DateUtilities.createDate(2001, feb, 5, 12, 0);
        high[31]  = 46.0;
        low[31]   = 38.0;
        open[31]  = 43.0;
        close[31] = 40.0;
        volume[31] = 70.0;

        date[32]  = DateUtilities.createDate(2001, feb, 6, 12, 0);
        high[32]  = 55.0;
        low[32]   = 38.0;
        open[32]  = 39.0;
        close[32] = 53.0;
        volume[32] = 120.0;

        date[33]  = DateUtilities.createDate(2001, feb, 7, 12, 0);
        high[33]  = 50.0;
        low[33]   = 33.0;
        open[33]  = 37.0;
        close[33] = 37.0;
        volume[33] = 140.0;

        date[34]  = DateUtilities.createDate(2001, feb, 8, 12, 0);
        high[34]  = 59.0;
        low[34]   = 34.0;
        open[34]  = 57.0;
        close[34] = 43.0;
        volume[34] = 70.0;

        date[35]  = DateUtilities.createDate(2001, feb, 9, 12, 0);
        high[35]  = 48.0;
        low[35]   = 39.0;
        open[35]  = 46.0;
        close[35] = 47.0;
        volume[35] = 70.0;

        date[36]  = DateUtilities.createDate(2001, feb, 10, 12, 0);
        high[36]  = 55.0;
        low[36]   = 30.0;
        open[36]  = 37.0;
        close[36] = 30.0;
        volume[36] = 30.0;

        date[37]  = DateUtilities.createDate(2001, feb, 11, 12, 0);
        high[37]  = 60.0;
        low[37]   = 32.0;
        open[37]  = 56.0;
        close[37] = 36.0;
        volume[37] = 70.0;

        date[38]  = DateUtilities.createDate(2001, feb, 12, 12, 0);
        high[38]  = 56.0;
        low[38]   = 42.0;
        open[38]  = 53.0;
        close[38] = 54.0;
        volume[38] = 40.0;

        date[39]  = DateUtilities.createDate(2001, feb, 13, 12, 0);
        high[39]  = 49.0;
        low[39]   = 42.0;
        open[39]  = 45.0;
        close[39] = 42.0;
        volume[39] = 90.0;

        date[40]  = DateUtilities.createDate(2001, feb, 14, 12, 0);
        high[40]  = 55.0;
        low[40]   = 42.0;
        open[40]  = 47.0;
        close[40] = 54.0;
        volume[40] = 70.0;

        date[41]  = DateUtilities.createDate(2001, feb, 15, 12, 0);
        high[41]  = 49.0;
        low[41]   = 35.0;
        open[41]  = 38.0;
        close[41] = 35.0;
        volume[41] = 20.0;

        date[42]  = DateUtilities.createDate(2001, feb, 16, 12, 0);
        high[42]  = 47.0;
        low[42]   = 38.0;
        open[42]  = 43.0;
        close[42] = 42.0;
        volume[42] = 10.0;

        date[43]  = DateUtilities.createDate(2001, feb, 17, 12, 0);
        high[43]  = 53.0;
        low[43]   = 42.0;
        open[43]  = 47.0;
        close[43] = 48.0;
        volume[43] = 20.0;

        date[44]  = DateUtilities.createDate(2001, feb, 18, 12, 0);
        high[44]  = 47.0;
        low[44]   = 44.0;
        open[44]  = 46.0;
        close[44] = 44.0;
        volume[44] = 30.0;

        date[45]  = DateUtilities.createDate(2001, feb, 19, 12, 0);
        high[45]  = 46.0;
        low[45]   = 40.0;
        open[45]  = 43.0;
        close[45] = 44.0;
        volume[45] = 50.0;

        date[46]  = DateUtilities.createDate(2001, feb, 20, 12, 0);
        high[46]  = 48.0;
        low[46]   = 41.0;
        open[46]  = 46.0;
        close[46] = 41.0;
        volume[46] = 100.0;

        return new DefaultHighLowDataset("Series 1", date, high, low, open, close, volume);

    }
    
    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final EWEngine demo = new EWEngine("Candlestick Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}

