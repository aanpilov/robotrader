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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanpilov
 */
public abstract class AbstractChartManager extends Thread implements ChartManager {
    private final Logger log = LoggerFactory.getLogger(getClass());
    protected final Set<ChartListener> listeners = new HashSet<>();
    protected final TimeSeries series;
    protected final Period period;
    protected final Security security;

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
        if(series.getTickCount() == 0) {
             return Optional.empty();
        }
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
        if(tick.getBeginTime().getHourOfDay() <= 10){
            log.warn("Invalid Tick begin date: " + tick.getBeginTime());
            return;
        }
        
        log.info("Add archive tick of " + security.getSecurityCode() + ": " + tick);        
        series.addTick(tick);
        
        if(!tick.inPeriod(DateTime.now())) {
            listeners.forEach((listener) -> {listener.archiveTickAdded(tick);});
        }        
    }
    
    protected void addTrade(DateTime timestamp, Decimal tradeAmount, Decimal tradePrice) {
        DateTime startTime = timestamp.minus(timestamp.getMillis() % period.toStandardDuration().getMillis());
        
        if(startTime.getHourOfDay() < 10) {
            log.warn("Invalid trade date: " + timestamp);
            return;
        }
        
        if(series.getTickCount() == 0) {
            newTickFromTrade(timestamp, tradeAmount, tradePrice);
            return;
        }
        
        if(series.getLastTick().inPeriod(timestamp)) {
            series.getLastTick().addTrade(tradeAmount, tradePrice);
        } else {
            if(series.getTickCount() > 1 && series.getLastTick().getEndTime().isEqual(startTime)) {
                //Exclude single aggregated
                log.info("Add online tick of " + security.getSecurityCode() + ": " + series.getLastTick());
                listeners.forEach((listener) -> {listener.onlineTickAdded(series.getLastTick());});
            }
            
            newTickFromTrade(timestamp, tradeAmount, tradePrice);
        }
    }

    private void newTickFromTrade(DateTime timestamp, Decimal tradeAmount, Decimal tradePrice) {
        DateTime endTime = timestamp.minus(timestamp.getMillis() % period.toStandardDuration().getMillis()).plus(period);
        
        Tick newTick = new Tick(period, endTime);
        newTick.addTrade(tradeAmount, tradePrice);
        series.addTick(newTick);
    }
}
