/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.strategy;

import ta4j.strategy.Advice;
import com.robotrader.analyzer.chart.ChartListener;
import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.trader.Portfolio;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ta4j.strategy.Strategy;

/**
 *
 * @author aanpilov
 */
public class StrategyManager implements ChartListener {    
    private final TimeSeries series;
    
    private final Strategy strategy;    
    private final Portfolio portfolio;
    private final ChartManager chartManager;
    
    private final List<Order> orders = new ArrayList<>();
    
    public StrategyManager(ChartManager chartManager, Strategy strategy, Portfolio portfolio) {
        this.portfolio = portfolio;        
        this.strategy = strategy;
        series = strategy.getTimeSeries();
        this.chartManager = chartManager;        
    }

    public void start() {
        chartManager.addChartListener(this);
        chartManager.start();
    }
    
    @Override
    public void archiveTickAdded(Tick tick) {        
    }

    @Override
    public void onlineTickAdded(Tick tick) {
        processLastTick();
    }

    public List<Order> getOrders() {
        return orders;
    }
    
    public Optional<Order> getLiquidationOrder() {
        if(portfolio.getPosition().isPositive()) {
            return Optional.of(Order.sellAt(series.getEnd(), series.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else if(portfolio.getPosition().isNegative()) {
            return Optional.of(Order.buyAt(series.getEnd(), series.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else {
            return Optional.empty();
        }
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
}
