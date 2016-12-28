/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import com.robotrader.analyzer.chart.AbstractChartManager;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.File;
import org.joda.time.Period;
import com.robotrader.analyzer.io.FinamCsvTicksLoader;

/**
 *
 * @author aanpilov
 */
public class FinamFileChartManager extends AbstractChartManager {
    private final File file;
    
    public FinamFileChartManager(File file, Period period) {
        super(period);
        this.file = file;
    }
    
    private void addOnlineTick(Tick tick) {
        series.addTick(tick);
        
        listeners.forEach((listener) -> {listener.onlineTickAdded(tick);});
    }

    @Override
    public void run() {
        try{
            TimeSeries fileSeries = FinamCsvTicksLoader.loadSeries(file);
            for (int i = 0; i < fileSeries.getTickCount(); i++) {
                Tick tick = fileSeries.getTick(i);
                addOnlineTick(tick);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
