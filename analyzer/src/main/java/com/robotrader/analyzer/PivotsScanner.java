/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.core.factor.Candle;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author aanpilov
 */
public class PivotsScanner {    
    private Candle lastCandle = null;
    private boolean up = false;
    private Set<Pivot> pivots = new HashSet<>();
    
    public void add(Candle candle) {
        if(lastCandle == null) {            
            //first candle
            if(candle.getOpenValue().compareTo(candle.getCloseValue()) == -1) {
                up = true;
            } else {
                up = false;
            }
            
            lastCandle = candle;
            
            addPivot();
        } else {
            boolean directionChanged = false;
            if(up) {
                if(candle.getMinValue().doubleValue() < lastCandle.getMinValue().doubleValue()) {
                    directionChanged = true;
                    up = false;
                }
                if(candle.getMaxValue().doubleValue() >= lastCandle.getMaxValue().doubleValue()) {
                    lastCandle = candle;
                }
            }else {
                if(candle.getMaxValue().doubleValue() > lastCandle.getMaxValue().doubleValue()) {
                    directionChanged = true;
                    up = true;
                }
                if(candle.getMinValue().doubleValue() <= lastCandle.getMinValue().doubleValue()) {
                    lastCandle = candle;
                }
            }
            
            if(directionChanged) {
                addPivot();
            }
            
            lastCandle = candle;
        }
    }

    private void addPivot() {        
        BigDecimal value = up?lastCandle.getMinValue():lastCandle.getMaxValue();        
        pivots.add(new Pivot(lastCandle.getDate(), value, up));
    }
    
    public Set<Pivot> getPivots() {
        return pivots;
    }
}
