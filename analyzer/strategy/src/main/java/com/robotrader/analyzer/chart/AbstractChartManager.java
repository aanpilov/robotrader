/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.chart;

import com.robotrader.core.objects.Security;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.HashSet;
import java.util.Optional;
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
    private final Security security;

    public AbstractChartManager(Security security, Period period) {
        this.series = new TimeSeries();
        this.period = period;
        this.security = security;
    }
    
    @Override
    public TimeSeries getChart() {
        return series;
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public Security getSecurity() {
        return security;
    }

    @Override
    public void addChartListener(ChartListener listener) {
        listeners.add(listener);
    }

    @Override
    public Optional<Tick> getTickStartedAt(DateTime time) {
        if(series.getLastTick().getBeginTime().isEqual(time)) {
            return Optional.of(series.getLastTick());
        } else if(series.getLastTick().getBeginTime().isAfter(time)){
            for (int i = series.getEnd(); i >= 0; i--) {
                Tick tick = series.getTick(i);
                if(tick.getBeginTime().isEqual(time)) {
                    return Optional.of(tick);
                }
                if(tick.getBeginTime().isBefore(time)) {
                    break;
                }
            }
            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tick> getLastTick() {
        if(series.getTickCount() > 0) {
            return Optional.of(series.getLastTick());
        } else {
            return Optional.empty();
        }
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
