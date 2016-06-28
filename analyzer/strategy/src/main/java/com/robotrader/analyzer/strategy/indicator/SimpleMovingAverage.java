/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.indicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author anpilov_av
 */
public class SimpleMovingAverage {
    private final int period;
    
    private SortedMap<Date, Double> values = new TreeMap<>();

    public SimpleMovingAverage(int period, SortedMap values) {
        this.period = period;
        this.values = values;
    }
    
    public Double calculate(Date date) {
        Double result = null;
        
        ArrayList<Date> dateList = new ArrayList<>(values.keySet());        
        int currentPosition = dateList.indexOf(date);
        
        if(currentPosition + 1 >= period) {
            result = 0d;
            
            int startPosition = currentPosition - period + 1;                        
            for (int i = startPosition; i <= currentPosition; i++) {
                result += values.get(dateList.get(i));
            }
            
            result /= period;
        }
        
        return result;
    }
}
