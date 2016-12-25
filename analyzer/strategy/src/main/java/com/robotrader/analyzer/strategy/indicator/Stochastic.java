/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.indicator;

import com.robotrader.analyzer.strategy.Advice;
import com.robotrader.core.factor.BarStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anpilov_av
 */
public class Stochastic implements Indicator {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    protected final int kPeriod;
    private final int kSlowPeriod;
    private final int dSlowPeriod;
    
    protected SortedMap<Date, Double> highValues = new TreeMap<>();
    protected SortedMap<Date, Double> lowValues = new TreeMap<>();
    protected SortedMap<Date, Double> closeValues = new TreeMap<>(); 
    
    private final SortedMap<Date, Double> kValues = new TreeMap<>();     
    private final SortedMap<Date, Double> kAverageValues = new TreeMap<>();     
    private final SortedMap<Date, Double> dAverageValues = new TreeMap<>();         
    
    private final SortedMap<Date, Advice> trendValues = new TreeMap<>();
    private final SortedMap<Date, Boolean> trendChanges = new TreeMap<>();

    public Stochastic(int kPeriod, int kSlowPeriod, int dSlowPeriod, BarStorage barStorage) {
        this.kPeriod = kPeriod;
        this.kSlowPeriod = kSlowPeriod;
        this.dSlowPeriod = dSlowPeriod;
        this.highValues = barStorage.getHighValues();
        this.lowValues = barStorage.getLowValues();
        this.closeValues = barStorage.getCloseValues();
    }
    
    public void calculate(Date date) {        
        Double kValue = calculateKValue(date);
        
        if(kValue != null) {
            kValues.put(date, kValue);
            Double kAverageValue = new SimpleMovingAverage(kSlowPeriod, kValues).calculate(date);
            if(kAverageValue != null) {
                kAverageValues.put(date, kAverageValue);                
                Double dAverageValue = new SimpleMovingAverage(dSlowPeriod, kAverageValues).calculate(date);
                if(dAverageValue != null) {                    
                    dAverageValues.put(date, dAverageValue);
                    calculateTrend(date);
                    log.debug("On " + date + " value calculated " + getValue(date));
                }
            }
        }
    }

    protected Double calculateKValue(Date date) {
        Double result = null;        
        
        ArrayList<Date> dateList = new ArrayList<>(closeValues.keySet());        
        int currentPosition = dateList.indexOf(date);
        
        if(currentPosition + 1 >= kPeriod) {
            result = 0d;
            
            int startPosition = currentPosition - kPeriod + 1;
            
            Double lastCloseValue = closeValues.get(date);
            Double minLowValue = (Double) Collections.min(new ArrayList(lowValues.values()).subList(startPosition, currentPosition + 1));
            Double maxHighValue = (Double) Collections.max(new ArrayList(highValues.values()).subList(startPosition, currentPosition + 1));
           
            result = ((lastCloseValue - minLowValue)/(maxHighValue - minLowValue)) * 100;            
        }
        
        return result;
    }
    
    private IndicatorValue getValue(Date date) {
        return new IndicatorValue(kAverageValues.get(date), dAverageValues.get(date), trendValues.get(date), trendChanges.get(date));
    }
    
    private IndicatorValue getLastValue() {
        IndicatorValue result = null;
        
        if(!dAverageValues.isEmpty()) {
            Date lastKey = dAverageValues.lastKey();
            result = getValue(lastKey);
        }
        return result;
    }
    
    public SortedMap<Date, IndicatorValue> getValues() {
        SortedMap<Date, IndicatorValue> result = new TreeMap<>();
                
        for(Date date : dAverageValues.keySet()) {
            IndicatorValue indicatorValue = getValue(date);
            result.put(date, indicatorValue);
        }
        
        return result;
    }

    @Override
    public Advice getAdvice() {
        IndicatorValue lastValue = getLastValue();
        if(lastValue != null) {
            if(lastValue.getkValue().compareTo(20d) < 0 && lastValue.getdValue().compareTo(20d) < 0) return Advice.advBuy;
            if(lastValue.getkValue().compareTo(80d) > 0 && lastValue.getdValue().compareTo(80d) > 0) return Advice.advSell;
            
            //В интервале 20-80
            if(lastValue.getTrendChanged()) {
                return (lastValue.getTrendValue() == Advice.advUp)?Advice.advBuy: Advice.advSell;
            } else {
                return lastValue.getTrendValue();
            }
        }
        return Advice.advNothing;
    }

    private void calculateTrend(Date date) {
        Double kValue = kAverageValues.get(date);
        Double dValue = dAverageValues.get(date);
        
        Advice lastTrend = null;
        if(!trendValues.isEmpty()) {
            Date lastKey = trendValues.lastKey();
            lastTrend = trendValues.get(lastKey);
        }
        
        Advice trend;
        int trendValue = kValue.compareTo(dValue);
        if(trendValue == 0 && lastTrend != null) {
            trend = lastTrend.not();
        } else {
            trend = (trendValue > 0)? Advice.advUp : Advice.advDown;
        }
        
        trendValues.put(date, trend);
        
        if(lastTrend != null && trend != lastTrend) {
            trendChanges.put(date, Boolean.TRUE);
        } else {
            trendChanges.put(date, Boolean.FALSE);
        }
    }
    
    public class IndicatorValue {
        private final Double kValue;
        private final Double dValue;
        private final Boolean trendChanged;
        private final Advice trendValue;

        public IndicatorValue(Double kValue, Double dValue, Advice trendValue, Boolean trendChanged) {
            this.kValue = kValue;
            this.dValue = dValue;
            this.trendChanged = trendChanged;
            this.trendValue = trendValue;
        }

        public Double getkValue() {
            return kValue;
        }

        public Double getdValue() {
            return dValue;
        }

        public Boolean getTrendChanged() {
            return trendChanged;
        }

        public Advice getTrendValue() {
            return trendValue;
        }

        @Override
        public String toString() {
            return "IndicatorValue{" + "kValue=" + kValue + ", dValue=" + dValue + ", trendChanged=" + trendChanged + ", trendValue=" + trendValue + '}';
        }
    }
}
