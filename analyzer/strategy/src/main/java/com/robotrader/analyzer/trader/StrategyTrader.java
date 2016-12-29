/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.trader;

import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.strategy.StrategyListener;
import com.robotrader.analyzer.strategy.StrategyManager;
import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.objects.Security;
import com.robotrader.core.service.AsyncAdapterService;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta4j.strategy.Advice;

/**
 *
 * @author aanpilov
 */
public class StrategyTrader implements StrategyListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private final Portfolio portfolio;
    private final ChartManager chartManager;    
    private final StrategyManager strategyManager;
    private final AsyncAdapterService adapterService;
    
    private final Decimal riskThreshold;
    private final Decimal positionThreshold;
    
    private ConditionalOrder order, linkedOrder;
    private final List<Order> orders = new ArrayList<>();

    public StrategyTrader(Portfolio portfolio, ChartManager chartManager, StrategyManager strategyManager, AsyncAdapterService adapterService, Decimal riskThreshold, Decimal positionThreshold) {
        this.portfolio = portfolio;
        this.chartManager = chartManager;
        this.strategyManager = strategyManager;
        this.adapterService = adapterService;
        this.riskThreshold = riskThreshold;
        this.positionThreshold = positionThreshold;
    }
    
    public void start() throws Exception {
        chartManager.start();
        strategyManager.start();
        adapterService.subscribe(new Security());
    }
    
    @Override
    public void onAdvice(Tick tick, Advice advice) {
        Tick tradeTick = chartManager.getTickStartedAt(tick.getBeginTime()).get();
        Optional<Order> order = portfolio.processNewTick(tradeTick);
        if(order.isPresent()) {
            orders.add(order.get());
        }
        
        if(portfolio.getPosition().isPositive() && advice == Advice.EXIT_LONG) {
            setOrder(tradeTick, Decimal.ZERO);
        }
        
        if(portfolio.getPosition().isNegative() && advice == Advice.EXIT_SHORT) {
            setOrder(tradeTick, Decimal.ZERO);
        }
        
        if(!portfolio.getPosition().isPositive() && advice == Advice.ENTER_LONG) {
            setOrder(tradeTick, Decimal.ONE);
        }
        
        if(!portfolio.getPosition().isNegative() && advice == Advice.ENTER_SHORT) {
            setOrder(tradeTick, Decimal.valueOf(-1));
        }
        
        if(portfolio.getPosition().isPositive() && advice == Advice.REDUCE_LONG) {
            reducePosition(tradeTick);
        }
        
        if(portfolio.getPosition().isNegative() && advice == Advice.REDUCE_SHORT) {
            reducePosition(tradeTick);
        }
    }
    
    private void setOrder(Tick tick, Decimal requiredPositionSign) {
        Decimal tickModule = tick.getMaxPrice().minus(tick.getMinPrice());
        if(tickModule.isZero()) {
            log.error("Tick module is ZERO: " + tick);
            return;
        }
        Decimal newPosition = calculateNewPosition(tickModule, requiredPositionSign);
        Decimal amount = newPosition.minus(portfolio.getPosition());
        
        createOrder(amount, tick);
        
        setLinkedOrder(tick, newPosition);
    }
    
    private void createOrder(Decimal amount, Tick tick) {        
        if(amount.isGreaterThan(Decimal.ZERO)) {
            order = ConditionalOrder.buyLastUp(0, 0, chartManager.getSecurity(), 0, tick.getMaxPrice().toDouble(), amount.toDouble());
        } else {
            order = ConditionalOrder.sellLastDown(0, 0, chartManager.getSecurity(), 0, tick.getMinPrice().toDouble(), amount.toDouble());
        }
        
        log.info("Order: " + order);
    }
    
    private Decimal calculateNewPosition(Decimal tickModule, Decimal requiredPositionSign) {
        Decimal newPosition = riskThreshold.dividedBy(tickModule);
        if(newPosition.isGreaterThan(positionThreshold)) {
            newPosition = positionThreshold;
        }
        
        return Decimal.valueOf(Math.rint(newPosition.multipliedBy(requiredPositionSign).toDouble()));
    }
    
    private void reducePosition(Tick tick) {
        if(linkedOrder != null) {
            Decimal amount = (order.isBuy())?Decimal.valueOf(order.getQuantity()):Decimal.ZERO.minus(Decimal.valueOf(order.getQuantity()));
            createOrder(amount, tick);
            return;
        }
        
        Decimal newPosition = calculateReducedPosition();
        if(newPosition.isNaN()) {
            log.info("Reduction not available");
            return;
        }
        
        if(order.isBuy()) {
            linkedOrder = ConditionalOrder.buyLastUp(0, 0, chartManager.getSecurity(), 0, order.getPrice(), newPosition.abs().toDouble());                    
        } else {
            linkedOrder = ConditionalOrder.sellLastDown(0, 0, chartManager.getSecurity(), 0, order.getPrice(), newPosition.abs().toDouble());                    
        }
        
        log.info("Reduced order: " + linkedOrder);
        
        Decimal amount = Decimal.ZERO.minus(portfolio.getPosition().minus(newPosition));
        createOrder(amount, tick);
    }
    
    private Decimal calculateReducedPosition() {
        Decimal reducedPosition = Decimal.valueOf(Math.rint(portfolio.getPosition().dividedBy(Decimal.TWO).toDouble()));
        
        if(reducedPosition.isZero() || reducedPosition.abs().isGreaterThanOrEqual(portfolio.getPosition().abs())) {
            reducedPosition = Decimal.NaN;
        }
        
        return reducedPosition;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
    
    public Optional<Order> getLiquidationOrder() {
        TimeSeries tradeSeries = chartManager.getChart();
        if(portfolio.getPosition().isPositive()) {
            return Optional.of(Order.sellAt(tradeSeries.getEnd(), tradeSeries.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else if(portfolio.getPosition().isNegative()) {
            return Optional.of(Order.buyAt(tradeSeries.getEnd(), tradeSeries.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else {
            return Optional.empty();
        }
    }
    
    private void setLinkedOrder(Tick tick, Decimal newPosition) {
        if(order != null && !newPosition.isZero()) {
            if(order.isBuy()) {
                linkedOrder = ConditionalOrder.sellLastDown(0, 0, chartManager.getSecurity(), 0, tick.getMinPrice().toDouble(), newPosition.abs().toDouble());                
            } else {
                linkedOrder = ConditionalOrder.buyLastUp(0, 0, chartManager.getSecurity(), 0, tick.getMaxPrice().toDouble(), newPosition.abs().toDouble());                
            }
        } else {
            linkedOrder = null;
        }
        
        log.info("Linked order: " + linkedOrder);
    }
    
    
//    public static void main(String[] args) throws Exception {
//        Decimal initCapital = Decimal.valueOf(2200);
//        
//        Portfolio portfolio = new Portfolio(initCapital.dividedBy(Decimal.valueOf(22)), Decimal.NaN);
//        ChartManager chartManager = new FinamFileChartManager(new File("src/test/resources/finam/SBER_H_2016.csv"), Period.hours(1));
//        ChartManager tradeManager = new FinamFileChartManager(new File("src/test/resources/finam/SBER_H_2016.csv"), Period.hours(1));
//        
////        Strategy strategy = new SimpleStrategy(chartManager.getChart());
//        ReductionStrategy strategy = new ReductionStrategy(chartManager.getChart());
//        
//        StrategyManager strategyManager = new StrategyManager(chartManager, strategy);
//        
//        StrategyTrader strategyTrader = new StrategyTrader(portfolio, tradeManager, strategyManager);
//        strategyTrader.start();
//        
//        Thread.sleep(15 * 1000);
//
//        List<Order> orders = strategyTrader.getOrders();
//        Optional<Order> liquidationOrder = strategyTrader.getLiquidationOrder();
//        if(liquidationOrder.isPresent()) {
//            orders.add(liquidationOrder.get());
//        }
//        
//        //Clear profit
//        System.out.println("Clear profit: " + new ClearProfitCriterion().calculate(orders));
//        System.out.println("Clear profit %: " + (new ClearProfitCriterion().calculate(orders)/initCapital.toDouble()) * 100);
//    }
    
}
