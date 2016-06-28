/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.process;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.LevelFactor;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.process.FactorSetProducer;
import com.robotrader.storage.adapter.FactorStorage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.log4j.Logger;

/**
 *
 * @author 1
 */
public class AverageEnvelopeLevelsProducer implements FactorSetProducer {
    private Logger log = Logger.getLogger(getClass());    
    private FactorStorage factorStorage;
    
    private static final String LEVEL_ENVELOPE_CODE = "AVG_ENV";
    private static final String LEVEL_AVERAGE_CODE = "AVG";
    
    private String factorCode;
    private Long intervalCount;
    private Interval interval;
    private BigDecimal envelopeLevel;
    //TODO Вынести Scale в FactorHeader
    private Long scale;

    public void setFactorStorage(FactorStorage factorStorage) {
        this.factorStorage = factorStorage;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public void setIntervalCount(Long intervalCount) {
        this.intervalCount = intervalCount;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public void setEnvelopeLevel(BigDecimal envelopeLevel) {
        this.envelopeLevel = envelopeLevel;
    }

    public void setScale(Long scale) {
        this.scale = scale;
    }

    @Override
    public FactorSet produce() throws Exception {
        FactorSet factorSet = new FactorSet();
        
        BigDecimal averageValue = factorStorage.getAverageValue(factorCode, interval, intervalCount);
        log.debug(factorCode + " average value: " + averageValue);
        if(averageValue != null) {
            BigDecimal averageScaledValue = averageValue.setScale(scale.intValue(), RoundingMode.HALF_UP);
            log.debug(factorCode + " average scaled value: " + averageScaledValue);
            LevelFactor averageLevelFactor = new LevelFactor(LEVEL_AVERAGE_CODE, factorCode, averageScaledValue);
            factorSet.add(averageLevelFactor);
            
            BigDecimal envelopeLowLevelValue = averageValue.multiply(BigDecimal.ONE.subtract(envelopeLevel)).setScale(scale.intValue(), RoundingMode.HALF_UP);
            log.debug(factorCode + " low scaled value: " + envelopeLowLevelValue);
            LevelFactor envelopeLowLevelFactor = new LevelFactor(LEVEL_ENVELOPE_CODE, factorCode, envelopeLowLevelValue);
            factorSet.add(envelopeLowLevelFactor);
            
            BigDecimal envelopeHighLevelValue = averageValue.multiply(BigDecimal.ONE.add(envelopeLevel)).setScale(scale.intValue(), RoundingMode.HALF_UP);
            log.debug(factorCode + " high scaled value: " + envelopeHighLevelValue);
            LevelFactor envelopeHighLevelFactor = new LevelFactor(LEVEL_ENVELOPE_CODE, factorCode, envelopeHighLevelValue);
            factorSet.add(envelopeHighLevelFactor);
        }
        
        return factorSet;
    }
    
}
