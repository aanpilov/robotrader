/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.analyzer.strategy.Advice;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import org.apache.log4j.Logger;

/**
 *
 * @author aav
 */
public class InvestmentStrategyWithoutPosition extends Thread {
    private Logger log = Logger.getLogger(getClass());
    
    private Paper paper;
    private AdapterService adapter;

    public InvestmentStrategyWithoutPosition(Paper paper, AdapterService adapter) {
        this.paper = paper;
        this.adapter = adapter;
    }
    
    public void run() {
        StringBuilder message = new StringBuilder();
        
        if(isSatisfied(Interval.ONE_MONTH, Advice.advBuy)) {
            //На месяцах - покупать
            log.debug("Advice: " + Advice.advBuy + " Paper: " + paper.getPaperTradeCode() + " Interval: " + Interval.ONE_MONTH);
            
            if(isSatisfied(Interval.ONE_WEEK, Advice.advBuy)) {
                log.debug("Advice: " + Advice.advBuy + " Paper: " + paper.getPaperTradeCode() + " Interval: " + Interval.ONE_WEEK);
                
                message.append("Advice: ").append(Advice.advBuy).append(" Paper: ").append(paper.getPaperTradeCode()).append(" Interval: ").append(Interval.ONE_WEEK).append("\n");
                
                if(isSatisfied(Interval.ONE_DAY, Advice.advBuy)) {
                    message.append("Advice: ").append(Advice.advBuy).append(" Paper: ").append(paper.getPaperTradeCode()).append(" Interval: ").append(Interval.ONE_DAY).append("\n");
                } else {
                    log.debug("Advice: " + Advice.advNothing + " Paper: " + paper.getPaperTradeCode() + " Interval: " + Interval.ONE_DAY);                    
                }
            } else {
                log.debug("Advice: " + Advice.advNothing + " Paper: " + paper.getPaperTradeCode() + " Interval: " + Interval.ONE_WEEK);                
            }         
        } else {
            //На месяцах - пусто
            log.debug("Advice: " + Advice.advNothing + " Paper: " + paper.getPaperTradeCode() + " Interval: " + Interval.ONE_MONTH);
        }
        System.out.print(message);
    }
    
    private boolean isSatisfied(Interval interval, Advice advice) {
        StrategySpecification specification = new StrategySpecification(paper, interval, advice);
        
        StrategyImpl strategy = new StrategyImpl(adapter, specification);
        strategy.run();
        
        return specification.isSatisfied(strategy);
    }
}
