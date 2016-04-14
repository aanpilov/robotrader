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
        if (lastCandle == null) {
            //first candle
            if (candle.getOpenValue().compareTo(candle.getCloseValue()) == -1) {
                up = true;
            } else {
                up = false;
            }

            lastCandle = candle;

            addPivot();
        } else {
            if (up) {
                if (candle.getMaxValue().compareTo(lastCandle.getMaxValue()) >= 0
                        && candle.getMinValue().compareTo(lastCandle.getMinValue()) <= 0) {
                    lastCandle = candle;
                } else if (candle.getMaxValue().compareTo(lastCandle.getMaxValue()) >= 0) {
                    lastCandle = candle;
                } else if (candle.getMinValue().compareTo(lastCandle.getMinValue()) < 0) {
                    up = false;
                    addPivot();
                    lastCandle = candle;
                }
            } else {
                if (candle.getMinValue().compareTo(lastCandle.getMinValue()) <= 0
                        && candle.getMaxValue().compareTo(lastCandle.getMaxValue()) >= 0) {
                    lastCandle = candle;
                } else if (candle.getMinValue().compareTo(lastCandle.getMinValue()) <= 0) {
                    lastCandle = candle;
                } else if (candle.getMaxValue().compareTo(lastCandle.getMaxValue()) > 0) {
                    up = true;
                    addPivot();
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
        List<Pivot> result = new ArrayList<>();
        
        result.addAll(pivots);
        BigDecimal value = up?lastCandle.getMaxValue():lastCandle.getMinValue();        
        result.add(new Pivot(lastCandle.getDate(), value, !up));        
        
        return result;
    }
}
