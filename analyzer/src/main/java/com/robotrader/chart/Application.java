/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.chart;

import com.robotrader.analyzer.FinamFileParser;
import com.robotrader.analyzer.Pivot;
import com.robotrader.analyzer.PivotsScanner;
import com.robotrader.analyzer.WavesFilter;
import com.robotrader.analyzer.WavesStorage;
import com.robotrader.core.factor.CandleStorage;
import com.robotrader.core.factor.Interval;
import java.util.Date;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author aanpilov
 */
public class Application extends ApplicationFrame {

    private XYPlot plot;

    /**
     * A demonstration application showing a candlestick chart.
     *
     * @param title the frame title.
     */
    public Application(final String title) {
        super(title);
        
        DateAxis xAxis = new DateAxis();        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRangeIncludesZero(false);
        plot = new XYPlot(null, xAxis, yAxis, null);

        JFreeChart chart = new JFreeChart(plot);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public void addDataset(XYDataset dataset, XYItemRenderer renderer) {
        int counter = plot.getDatasetCount();
        
        plot.setDataset(counter, dataset);
        plot.setRenderer(counter, renderer);
    }

    public static void main(final String[] args) throws Exception {
        CandleStorage storage = new FinamFileParser(null, Interval.ONE_HOUR).parse("src/test/resources/candles/candles.txt");
        CandleStoragePlotter plotter = new CandleStoragePlotter(storage);        
        
        PivotsScanner scanner = new PivotsScanner();
        for(Date date : storage.getDates()) {
            scanner.add(storage.getBar(date));
        }
        PivotsPlotter pivotsPlotter = new PivotsPlotter(scanner.getPivots());
        
        WavesStorage wavesStorage = new WavesStorage();
        for(Pivot pivot : scanner.getPivots()) {
            wavesStorage.addPivot(pivot);
        }
        WavesPlotter wavesPlotter = new WavesPlotter(wavesStorage.getWaves());
        
        WavesFilter filter = new WavesFilter();
        WavesStorage filteredWavesStorage = filter.filterCorrectionModule(wavesStorage);
        WavesPlotter filteredWavesPlotter = new WavesPlotter(filteredWavesStorage.getWaves());
        
        Application demo = new Application("Candlestick Demo");
        demo.addDataset(plotter.getDataset(), plotter.getRenderer());
//        demo.addDataset(pivotsPlotter.getDataset(), pivotsPlotter.getRenderer());        
//        demo.addDataset(wavesPlotter.getDataset(), wavesPlotter.getRenderer());        
        demo.addDataset(filteredWavesPlotter.getDataset(), filteredWavesPlotter.getRenderer());        

        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
}
