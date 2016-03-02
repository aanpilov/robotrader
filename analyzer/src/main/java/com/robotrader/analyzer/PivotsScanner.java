/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.core.factor.Candle;
import java.math.BigDecimal;

/**
 *
 * @author aanpilov
 */
public class PivotsScanner {    
    private Candle lastCandle = null;
    private boolean up = false;    
    
    public void add(Candle candle) {
        if(lastCandle == null) {            
            //first candle
            if(candle.getOpenValue().compareTo(candle.getCloseValue()) == -1) {
                up = true;
            } else {
                up = false;
            }
            
            lastCandle = candle;
            
            logPivot();
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
                logPivot();
            }
            
            lastCandle = candle;
        }
    }

    private void logPivot() {
        String direction = up?" UP ": " DOWN ";
        BigDecimal value = up?lastCandle.getMinValue():lastCandle.getMaxValue();
        System.out.println("Pivot " + direction + " AT " + lastCandle.getDate() + " FROM " + value);
    }
}
