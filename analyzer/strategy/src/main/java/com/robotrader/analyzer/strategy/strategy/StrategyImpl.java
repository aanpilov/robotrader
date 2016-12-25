/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.analyzer.strategy.Advice;
import com.robotrader.analyzer.strategy.indicator.Stochastic;
import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.BarStorage;
import com.robotrader.core.service.AdapterService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aav
 */
public class StrategyImpl extends Thread implements Strategy {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private AdapterService adapterService;
    private BarStorage barStorage;
    private Stochastic stochastic;    
    private StrategySpecification specification;

    public StrategyImpl(AdapterService adapterService, StrategySpecification spec) {
        super(spec.getPaper().getPaperTradeCode());
        this.specification = spec;
        this.adapterService = adapterService;        
        barStorage = new BarStorage(spec.getPaper(), spec.getInterval(), 5);
        stochastic = new Stochastic(5, 3, 3, barStorage);
    }
    
    private void addBar(Bar bar) {
        barStorage.addBar(bar);
        stochastic.calculate(bar.getDate());
    }
    
    @Override
    public Advice getAdvice() {
        /*TODO:
        List<Indicator> indicators = strategy.getIndicators();
        
        
        for(Indicator indicator : indicators) {
            advice = advice.and(indicator.getAdvice());
        }*/
        log.debug("Sochasic value: " + stochastic.getValues());
        return stochastic.getAdvice();
    }

    @Override
    public void run() {
        List<Bar> archiveBars = adapterService.getArchiveBars(specification.getPaper(), specification.getInterval(), 12);
            
        for(Bar b : archiveBars) {
            addBar(b);
        }
    }
}
