/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author anpilov_av
 */
public class CandleStorage {
    private SortedMap<Date, Double> openValues = new TreeMap<>();
    private SortedMap<Date, Double> highValues = new TreeMap<>();
    private SortedMap<Date, Double> lowValues = new TreeMap<>();
    private SortedMap<Date, Double> closeValues = new TreeMap<>();
    private SortedMap<Date, Double> volumeValues = new TreeMap<>();
    
    private int capacity;
    private Paper paper;
    private Interval interval;

    public CandleStorage(Paper paper, Interval interval, int capacity) {
        this.paper = paper;
        this.interval = interval;
        this.capacity = capacity;
    }
    
    public void addBar(Candle b) {
        addOpenValue(b.getDate(), b.getOpenValue().doubleValue());
        addHighValue(b.getDate(), b.getMaxValue().doubleValue());
        addLowValue(b.getDate(), b.getMinValue().doubleValue());
        addCloseValue(b.getDate(), b.getCloseValue().doubleValue());
        addVolumeValue(b.getDate(), b.getVolume().doubleValue());
        resize();
    }
    
    public Candle getBar(Date date) {
        Candle result = null;
        
        if(openValues.containsKey(date)) {
            BigDecimal open = BigDecimal.valueOf(openValues.get(date));
            BigDecimal close = BigDecimal.valueOf(closeValues.get(date));
            BigDecimal high = BigDecimal.valueOf(highValues.get(date));
            BigDecimal low = BigDecimal.valueOf(lowValues.get(date));
            BigDecimal volume = BigDecimal.valueOf(volumeValues.get(date));
            
            result = new Candle(paper, date, interval, open, close, low, high, volume);
        }
        
        return result;
    }
    
    public Set<Date> getDates() {
        return openValues.keySet();
    }
    
    public Candle getLastBar() {
        return getBar(openValues.lastKey());
    }

    private void addOpenValue(Date date, Double open) {
        if(!openValues.containsKey(date)) {
            openValues.put(date, open);
        }
    }

    private void addHighValue(Date date, Double high) {
        if(!highValues.containsKey(date) || high.compareTo(highValues.get(date)) == 1) {
            highValues.put(date, high);
        }
    }

    private void addLowValue(Date date, Double low) {
        if(!lowValues.containsKey(date) || low.compareTo(lowValues.get(date)) == -1) {
            lowValues.put(date, low);
        }
    }

    private void addCloseValue(Date date, Double close) {
        closeValues.put(date, close);
    }

    private void addVolumeValue(Date date, Double volume) {
        volumeValues.put(date, volume);
    }
    
    private void resize() {
        if(openValues.size() > capacity)openValues.remove(openValues.firstKey());
        if(highValues.size() > capacity)highValues.remove(highValues.firstKey());
        if(lowValues.size() > capacity)lowValues.remove(lowValues.firstKey());
        if(closeValues.size() > capacity)closeValues.remove(closeValues.firstKey());
        if(volumeValues.size() > capacity)volumeValues.remove(volumeValues.firstKey());
    }

    public SortedMap<Date, Double> getOpenValues() {
        return openValues;
    }

    public SortedMap<Date, Double> getHighValues() {
        return highValues;
    }

    public SortedMap<Date, Double> getLowValues() {
        return lowValues;
    }

    public SortedMap<Date, Double> getCloseValues() {
        return closeValues;
    }
}
