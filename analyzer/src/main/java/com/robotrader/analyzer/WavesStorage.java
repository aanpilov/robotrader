/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aanpilov
 */
public class WavesStorage {
    private List<Wave> waves = new ArrayList<>();
    private Pivot startPivot = null;
    
    public void addPivot(Pivot pivot) {
        if(startPivot == null) {
            startPivot = pivot;
        } else {
           createNewWave(pivot);
        }        
    }

    private void createNewWave(Pivot pivot) {
        Date lastDate;
        BigDecimal lastValue;
        
        if(waves.size() > 0) {
            Wave lastWave = waves.get(waves.size() - 1);
            lastDate = lastWave.getEndDate();
            lastValue = lastWave.getEndValue();
        } else {
            lastDate = startPivot.getDate();
            lastValue = startPivot.getValue();
        }
        
        Wave newWave = new Wave(lastDate, lastValue, pivot.getDate(), pivot.getValue());
        waves.add(newWave);
    }

    public List<Wave> getWaves() {
        return waves;
    }
}
