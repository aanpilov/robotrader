/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy;

import ta4j.strategy.Advice;
import com.robotrader.analyzer.chart.ChartListener;
import com.robotrader.analyzer.chart.ChartManager;
import eu.verdelhan.ta4j.Tick;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta4j.strategy.Strategy;

/**
 *
 * @author aanpilov
 */
public class StrategyManager implements ChartListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Strategy strategy;    
    
    private final Set<StrategyListener> strategyListeners = new HashSet<>();
    
    public StrategyManager(ChartManager chartManager, Strategy strategy) {
        this.strategy = strategy;        
        chartManager.addChartListener(this);
    }
    
    public void addStrategyListener(StrategyListener strategyListener) {
        strategyListeners.add(strategyListener);
    }

    @Override
    public void archiveTickAdded(Tick tick) {        
    }

    @Override
    public void onlineTickAdded(Tick tick) {
        int end = strategy.getTimeSeries().getEnd();
        Advice advice = strategy.getAdvice(end);
        strategyListeners.forEach(listener -> listener.onAdvice(tick, advice));
    }
}
