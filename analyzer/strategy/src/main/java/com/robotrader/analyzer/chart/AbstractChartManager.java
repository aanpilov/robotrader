/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.HashSet;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 *
 * @author aanpilov
 */
public abstract class AbstractChartManager extends Thread implements ChartManager {
    protected final Set<ChartListener> listeners = new HashSet<>();
    protected final TimeSeries series;
    protected final Period period;

    public AbstractChartManager(Period period) {
        this.series = new TimeSeries();
        this.period = period;
    }
    
    @Override
    public TimeSeries getChart() {
        return series;
    }

    @Override
    public void addChartListener(ChartListener listener) {
        listeners.add(listener);
    }
    
    protected void addArchiveTick(Tick tick) {
        series.addTick(tick);
        
        listeners.forEach((listener) -> {listener.archiveTickAdded(tick);});
    }
    
    protected void addTrade(DateTime timestamp, Decimal tradeAmount, Decimal tradePrice) {
        if(series.getLastTick().inPeriod(timestamp)) {
            series.getLastTick().addTrade(tradeAmount, tradePrice);
        } else {
            listeners.forEach((listener) -> {listener.onlineTickAdded(series.getLastTick());});
            
            DateTime endTime = timestamp.minus(timestamp.getMillis() % period.toStandardDuration().getMillis()).plus(period);
            Tick newTick = new Tick(period, endTime);
            newTick.addTrade(tradeAmount, tradePrice);
            series.addTick(newTick);
        }
    }
}
