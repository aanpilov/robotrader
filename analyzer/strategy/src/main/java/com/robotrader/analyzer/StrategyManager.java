/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import com.robotrader.analyzer.strategy.tajtutorial.loader.FinamCsvTicksLoader;
import com.robotrader.analyzer.strategy.tajtutorial.strategies.Advice;
import com.robotrader.analyzer.strategy.tajtutorial.strategies.ReductionStrategy;
import com.robotrader.analyzer.strategy.tajtutorial.strategies.SimpleStrategy;
import com.robotrader.analyzer.strategy.tajtutorial.strategies.Strategy;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ta4j.analysis.criteria.ClearProfitCriterion;

/**
 *
 * @author aanpilov
 */
public class StrategyManager {    
    private final TimeSeries series;
    private final Strategy strategy;
    
    private final Portfolio portfolio;
    private final List<Order> orders = new ArrayList<>();
    
    public StrategyManager(Strategy strategy, Portfolio portfolio) {
        this.portfolio = portfolio;        
        this.strategy = strategy;
        series = strategy.getTimeSeries();
    }
    
    public void addTick(Tick tick) {
        series.addTick(tick);
        
        processLastTick();
    }

    public List<Order> getOrders() {
        return orders;
    }
    
    private Optional<Order> processLastTick() {
        int endIndex = series.getEnd();
        
        Optional<Order> order = portfolio.processNewTick(series.getLastTick());
        if(order.isPresent()) {
            orders.add(order.get());
        }
        
        if(portfolio.getPosition().isPositive() && strategy.getAdvice(endIndex) == Advice.EXIT_LONG) {
            portfolio.setOrder(series.getLastTick(), Decimal.ZERO);
        }
        
        if(portfolio.getPosition().isNegative() && strategy.getAdvice(endIndex) == Advice.EXIT_SHORT) {
            portfolio.setOrder(series.getLastTick(), Decimal.ZERO);
        }
        
        if(!portfolio.getPosition().isPositive() && strategy.getAdvice(endIndex) == Advice.ENTER_LONG) {
            portfolio.setOrder(series.getLastTick(), Decimal.ONE);
        }
        
        if(!portfolio.getPosition().isNegative() && strategy.getAdvice(endIndex) == Advice.ENTER_SHORT) {
            portfolio.setOrder(series.getLastTick(), Decimal.valueOf(-1));
        }
        
        if(portfolio.getPosition().isPositive() && strategy.getAdvice(endIndex) == Advice.REDUCE_LONG) {
            portfolio.reducePosition(series.getLastTick());
        }
        
        if(portfolio.getPosition().isNegative() && strategy.getAdvice(endIndex) == Advice.REDUCE_SHORT) {
            portfolio.reducePosition(series.getLastTick());
        }
        
        return order;
    }
    
    public static void main(String[] args) throws Exception {
        Portfolio portfolio = new Portfolio(Decimal.valueOf(1000d), Decimal.valueOf(1000));
        TimeSeries testTimeSeries = FinamCsvTicksLoader.loadSeries(new File("src/test/resources/finam/SBER_H.csv"));
        
//        Strategy strategy = new SimpleStrategy();
        ReductionStrategy strategy = new ReductionStrategy();
        
        StrategyManager strategyManager = new StrategyManager(strategy, portfolio);
        
        for (int i = 0; i < testTimeSeries.getTickCount(); i++) {            
            strategyManager.addTick(testTimeSeries.getTick(i));
        }
        
        List<Order> orders = strategyManager.getOrders();
        
        //Clear profit
        System.out.println("Clear profit: " + new ClearProfitCriterion().calculate(orders));
    }
}
