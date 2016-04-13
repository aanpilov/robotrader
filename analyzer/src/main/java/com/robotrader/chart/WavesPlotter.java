/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.chart;

import com.robotrader.analyzer.Wave;
import java.util.List;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author aanpilov
 */
public class WavesPlotter {
    private List<Wave> waves;

    public WavesPlotter(List<Wave> waves) {
        this.waves = waves;
    }
    
    public XYItemRenderer getRenderer() {
        return new XYLineAndShapeRenderer();
    }
    
    public XYDataset getDataset() {
        final TimeSeries waveSeries = new TimeSeries("Waves");
        
        for(Wave wave : waves) {
            waveSeries.addOrUpdate(new Minute(wave.getStartDate()), wave.getStartValue());
            waveSeries.addOrUpdate(new Minute(wave.getEndDate()), wave.getEndValue());
        }
        
        final TimeSeriesCollection dataset = new TimeSeriesCollection();   
        dataset.addSeries(waveSeries);
        
        return dataset;
    }
}
