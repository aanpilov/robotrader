/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.chart;

import com.robotrader.analyzer.Pivot;
import java.util.Set;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author aanpilov
 */
public class PivotsPlotter {
    private Set<Pivot> pivots;

    public PivotsPlotter(Set<Pivot> pivots) {
        this.pivots = pivots;
    }
    
    public XYItemRenderer getRenderer() {
        return new DefaultXYItemRenderer();
    }

    public XYDataset getDataset() {
        TimeSeries upSeries = new TimeSeries("UP");
        TimeSeries downSeries = new TimeSeries("DOWN");
        
        for(Pivot pivot : pivots) {
            if(pivot.isUp()) {
                upSeries.add(new Minute(pivot.getDate()), pivot.getValue());
            } else {
                downSeries.add(new Minute(pivot.getDate()), pivot.getValue());
            }
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(upSeries);
        dataset.addSeries(downSeries);        
        
        return dataset;
    }
}
