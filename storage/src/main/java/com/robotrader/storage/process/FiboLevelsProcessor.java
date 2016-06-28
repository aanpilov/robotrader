/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.process;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.IntervalFactor;
import com.robotrader.core.factor.LevelFactor;
import com.robotrader.core.process.FactorSetProcessor;
import com.robotrader.core.util.jaxb.JAXBUtil;
import com.robotrader.storage.entities.FactorFiboLevel;
import com.robotrader.storage.entities.FactorFiboLevelDao;
import com.robotrader.storage.entities.FactorHeader;
import com.robotrader.storage.entities.FactorHeaderDao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author 1
 */
@Component(value = "fiboLevelsProcessor")
public class FiboLevelsProcessor implements FactorSetProcessor {
    private Logger log = Logger.getLogger(getClass());
    @Autowired
    private FactorHeaderDao headerDao;
    @Autowired
    private FactorFiboLevelDao fiboLevelDao;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private JAXBUtil jaxbUtil;    
        
    private static final String LEVELS_CHANGE_EVENT_TARGET = "FIBO.LEVELS";
    private static final String LEVEL_FIBO_CODE = "FIBO";
    private final BigDecimal[] FIBO_LEVELS = new BigDecimal[]{        
        new BigDecimal(0), 
        new BigDecimal(0.236), 
        new BigDecimal(0.382),
        new BigDecimal(0.5),
        new BigDecimal(0.618),
        new BigDecimal(0.764),
        new BigDecimal(1),
        new BigDecimal(1.236),
        new BigDecimal(1.382),
        new BigDecimal(1.5),
        new BigDecimal(1.618),
        new BigDecimal(1.764),
    };

    /**
     * Установка ближайших значений уровней
     * @param factorSet 
     */
    @Override
    public FactorSet process(FactorSet factorSet) throws Exception {
        FactorSet result = new FactorSet();
        result.add(factorSet);
        
        for(IntervalFactor intervalFactor : factorSet.getIntervalFactors().getFactors()) {
            FactorHeader factorHeader = headerDao.findByCode(intervalFactor.getHeader());
            FactorFiboLevel fiboLevels = getFiboLevels(factorHeader, intervalFactor.getOpenValue());
            
            if(fiboLevels != null) {
                LevelFactor lowLevelFactor = new LevelFactor(LEVEL_FIBO_CODE, intervalFactor.getHeader(), fiboLevels.getLowLevel());
                result.add(lowLevelFactor);
                LevelFactor highLevelFactor = new LevelFactor(LEVEL_FIBO_CODE, intervalFactor.getHeader(), fiboLevels.getHighLevel());
                result.add(highLevelFactor);
            }
        }
        
        return result;
    }

        /**
     * Корелляция уровней по архивным данным
     * @param factorSet 
     */
    public void corellate(FactorSet factorSet) throws Exception {
        log.debug("corellate() start...");
        
        FactorHeader sberHeader = headerDao.findByCode("SBER");
        FactorHeader sberFutHeader = headerDao.findByCode("SBER_FUT_PRICE");
        
        IntervalFactor sberFactor = factorSet.getIntervalFactors().get(sberHeader.getCode());
        IntervalFactor sberFutFactor = factorSet.getIntervalFactors().get(sberFutHeader.getCode());
        
        if(sberFactor != null && sberFutFactor != null) {
            log.debug("SBER close: " + sberFactor.getCloseValue().toPlainString());
            log.debug("SBER_FUT close: " + sberFutFactor.getCloseValue().toPlainString());
            
            BigDecimal futDiff = sberFactor.getCloseValue().multiply(new BigDecimal("100")).subtract(sberFutFactor.getCloseValue());
            log.debug("SBER_FUT diff: " + futDiff.toPlainString());

            FactorFiboLevel sberLevel = fiboLevelDao.getLevel(sberHeader.getId());
            log.debug("SBER Fibo levels: " + sberLevel);

            FactorFiboLevel sberFutLevel = new FactorFiboLevel();
            sberFutLevel.setFactorId(sberFutHeader.getId());
            sberFutLevel.setLowLevel(sberLevel.getLowLevel().multiply(new BigDecimal("100")).subtract(futDiff));
            sberFutLevel.setHighLevel(sberLevel.getHighLevel().multiply(new BigDecimal("100")).subtract(futDiff));
            log.debug("SBER_FUT Fibo levels: " + sberLevel);
            
            fiboLevelDao.updateLevel(sberFutLevel);
            
            publish();
        }
    }
    
    /**
     * Обновление уровней
     */
    public void update(FactorSet factorSet) {
        throw new UnsupportedOperationException("unsupported operation");
    }
    
    /**
     * Публикация события с актуальными уровнями
     */
    @PostConstruct
    private void publish() throws Exception {
        FactorSet factorSet = new FactorSet();
        
        List<FactorFiboLevel> fiboLevels = fiboLevelDao.listLevels();
        for(FactorFiboLevel level : fiboLevels) {
            FactorHeader factorHeader = headerDao.findById(level.getFactorId());
            
            LevelFactor lowLevelFactor = new LevelFactor(LEVEL_FIBO_CODE, factorHeader.getCode(), level.getLowLevel());
            factorSet.add(lowLevelFactor);
            LevelFactor highLevelFactor = new LevelFactor(LEVEL_FIBO_CODE, factorHeader.getCode(), level.getHighLevel());
            factorSet.add(highLevelFactor);
        }
        
        String factorSetStr = jaxbUtil.marshallObject(factorSet);
        jmsTemplate.convertAndSend(LEVELS_CHANGE_EVENT_TARGET, factorSetStr);
    }
    
    private FactorFiboLevel getFiboLevels(FactorHeader factorHeader, BigDecimal openValue) {
        FactorFiboLevel result = null;
        
        FactorFiboLevel factorFibo = fiboLevelDao.getLevel(factorHeader.getId());
        if(factorFibo != null) {
            BigDecimal fiboEnvelopeSize = factorFibo.getHighLevel().subtract(factorFibo.getLowLevel()).abs();
            BigDecimal fiboStartLevel = (factorFibo.getHighLevel().compareTo(factorFibo.getLowLevel()) == 1)?factorFibo.getLowLevel():factorFibo.getHighLevel();
            
            BigDecimal lowEnvelopeLevel = fiboStartLevel;
            BigDecimal highEnvelopeLevel = null;
            for(BigDecimal fiboLevel : FIBO_LEVELS) {
                highEnvelopeLevel = fiboStartLevel.add(fiboLevel.multiply(fiboEnvelopeSize));
                if(highEnvelopeLevel.compareTo(openValue) > 0) {
                    break;
                } else {
                    lowEnvelopeLevel = highEnvelopeLevel;
                }
            }
            
            if(openValue.compareTo(lowEnvelopeLevel) > 0 && highEnvelopeLevel.compareTo(openValue) > 0) {
                result = new FactorFiboLevel();
                result.setFactorId(factorHeader.getId());
                result.setLowLevel(lowEnvelopeLevel.setScale(factorHeader.getScale().intValue(), RoundingMode.HALF_UP));
                result.setHighLevel(highEnvelopeLevel.setScale(factorHeader.getScale().intValue(), RoundingMode.HALF_UP));
            }
        }
        
        return result;
    }
}
