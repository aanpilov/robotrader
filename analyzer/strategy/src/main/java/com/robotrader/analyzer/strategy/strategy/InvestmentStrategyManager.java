/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy.strategy;

import com.robotrader.core.factor.Bar;
import com.robotrader.core.factor.Paper;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.service.AdapterService;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import ta4j.strategy.Advice;
import ta4j.strategy.InvestmentStrategy;

/**
 *
 * @author aanpilov
 */
public class InvestmentStrategyManager extends Thread {
    private Paper paper;
    private AdapterService adapter;
    private boolean isPortfolio;

    public InvestmentStrategyManager(Paper paper, AdapterService adapter, boolean isPortfolio) {
        this.paper = paper;
        this.adapter = adapter;
        this.isPortfolio = isPortfolio;
    }
    
    public void run() {
        StringBuilder message = new StringBuilder();
        
        Advice advice = getAdvice(Interval.ONE_MONTH);
        if(advice == Advice.ENTER_LONG) {
            //На месяцах - покупать
            advice = getAdvice(Interval.ONE_WEEK);
            if(advice == Advice.ENTER_LONG) {
                message.append(getAdviceMessage(advice, paper, Interval.ONE_WEEK));
                
                advice = getAdvice(Interval.ONE_DAY);
                if(advice == Advice.ENTER_LONG) {
                    message.append(getAdviceMessage(advice, paper, Interval.ONE_DAY));
                }
            }
        } else if(isPortfolio) {
            if(advice == Advice.EXIT_LONG) {
                message.append(getAdviceMessage(advice, paper, Interval.ONE_MONTH));

                advice = getAdvice(Interval.ONE_WEEK);
                if(advice == Advice.EXIT_LONG) {
                    message.append(getAdviceMessage(advice, paper, Interval.ONE_WEEK));

                    advice = getAdvice(Interval.ONE_DAY);
                    if(advice == Advice.EXIT_LONG) {
                        message.append(getAdviceMessage(advice, paper, Interval.ONE_DAY));
                    }
                }
            } else if(advice == Advice.TREND_UP) {
                //Interval.ONE_MONTH - рост
                advice = getAdvice(Interval.ONE_WEEK);
                if(advice == Advice.EXIT_LONG) {
                    message.append(getAdviceMessage(advice, paper, Interval.ONE_WEEK));

                    advice = getAdvice(Interval.ONE_DAY);
                    if(advice == Advice.EXIT_LONG) {
                        message.append(getAdviceMessage(advice, paper, Interval.ONE_DAY));
                    }
                }

                if(advice == Advice.ENTER_LONG) {
                    message.append(getAdviceMessage(advice, paper, Interval.ONE_WEEK));

                    advice = getAdvice(Interval.ONE_DAY);
                    if(advice == Advice.ENTER_LONG) {
                        message.append(getAdviceMessage(advice, paper, Interval.ONE_DAY));
                    }
                }
            }
        }        
        
        System.out.print(message);
    }
    
    private String getAdviceMessage(Advice advice, Paper paper, Interval interval) {
        return new StringBuilder().append("Advice: ").append(advice).append(" Paper: ").append(paper.getPaperTradeCode()).append(" Interval: ").append(interval).append("\n").toString();
    }
    
    private Advice getAdvice(Interval interval) {
        List<Bar> archiveBars = adapter.getArchiveBars(paper, interval, 12);
        List<Tick> ticks = archiveBars.stream().map(bar -> {
            return new Tick(new DateTime(bar.getDate().getTime()), bar.getOpenValue().doubleValue(), bar.getMaxValue().doubleValue(), bar.getMinValue().doubleValue(), bar.getCloseValue().doubleValue(), bar.getVolume().doubleValue());
        }).collect(Collectors.toList());
        
        TimeSeries timeSeries = new TimeSeries(ticks);
        InvestmentStrategy strategy = new InvestmentStrategy(timeSeries);
        
        return strategy.getAdvice(timeSeries.getEnd());
    }
}
