/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.core.factor.Candle;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aanpilov
 */
public class PivotsScanner {    
    private Candle lastCandle = null;
    private boolean up = false;
    private List<Pivot> pivots = new ArrayList<>();
    
    public void add(Candle candle) throws Exception {
        if(candle.getDate().compareTo(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("02.02.2016 10:00:00")) == 0) {
                System.out.println("Start debug...");
        }
        
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
            if(up) {
                if(candle.getMinValue().doubleValue() < lastCandle.getMinValue().doubleValue()) {                    
                    up = false;   
                    if(candle.getMaxValue().doubleValue() >= lastCandle.getMaxValue().doubleValue()) {
                        lastCandle = candle;
                        addPivot();
                    } else {
                        addPivot();
                        lastCandle = candle;
                    }
                } else if(candle.getMaxValue().doubleValue() >= lastCandle.getMaxValue().doubleValue()) {
                    lastCandle = candle;
                }
            }else {
                if(candle.getMaxValue().doubleValue() > lastCandle.getMaxValue().doubleValue()) {                    
                    up = true;      
                    if(candle.getMinValue().doubleValue() <= lastCandle.getMinValue().doubleValue()) {
                        lastCandle = candle;
                        addPivot();
                    } else {
                        addPivot();
                        lastCandle = candle;
                    }                    
                } else if(candle.getMinValue().doubleValue() <= lastCandle.getMinValue().doubleValue()) {
                    lastCandle = candle;
                }
            }
        }
    }

    private void addPivot() {        
        BigDecimal value = up?lastCandle.getMinValue():lastCandle.getMaxValue();        
        pivots.add(new Pivot(lastCandle.getDate(), value, up));
    }
    
    public List<Pivot> getPivots() {
        return pivots;
    }
}
