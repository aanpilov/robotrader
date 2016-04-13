/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import java.math.BigDecimal;

/**
 *
 * @author aanpilov
 */
public class WavesFilter {

    public WavesStorage filterCorrectionModule(WavesStorage storage) throws Exception {
        WavesStorage result = new WavesStorage();

        Wave tWave = null;
        Wave cWave = null;

        for (Wave wave : storage.getWaves()) {
            if (tWave == null) {
                tWave = wave;
                
                result.addPivot(new Pivot(tWave.getStartDate(), tWave.getStartValue(), tWave.isUp()));
                
                continue;
            }

            if (cWave == null) {
                cWave = wave;
            }

            if (wave.isUp() == tWave.isUp()) {
                if (tWave.isUp() && (wave.getEndValue().compareTo(tWave.getEndValue()) >= 0)) {
                    tWave.setEndDate(wave.getEndDate());
                    tWave.setEndValue(wave.getEndValue());
                    
                    cWave = null;
                    continue;
                }

                if (!tWave.isUp() && (wave.getEndValue().compareTo(tWave.getEndValue()) <= 0)) {//                    
                    tWave.setEndDate(wave.getEndDate());
                    tWave.setEndValue(wave.getEndValue());

                    cWave = null;
                    continue;
                }
            }

            if (wave.isUp() == cWave.isUp()) {
                if (cWave.isUp() && (wave.getEndValue().compareTo(cWave.getEndValue()) >= 0)) {
                    cWave.setEndDate(wave.getEndDate());
                    cWave.setEndValue(wave.getEndValue());
                }

                if (!cWave.isUp() && (wave.getEndValue().compareTo(cWave.getEndValue()) <= 0)) {
                    cWave.setEndDate(wave.getEndDate());
                    cWave.setEndValue(wave.getEndValue());
                }

                if(cWave.getMod().compareTo(tWave.getMod().multiply(new BigDecimal("0.382"))) >= 0) {
                    result.addPivot(new Pivot(tWave.getEndDate(), tWave.getEndValue(), !tWave.isUp()));

                    tWave = cWave;
                    cWave = null;
                }
            }
        }
        
        result.addPivot(new Pivot(tWave.getEndDate(), tWave.getEndValue(), tWave.isUp()));
        result.addPivot(new Pivot(cWave.getEndDate(), cWave.getEndValue(), cWave.isUp()));
        
        return result;
    }
}