/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.mock;

import com.robotrader.analyzer.trader.AbstractStrategyTrader;
import com.robotrader.analyzer.portfolio.Portfolio;
import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.portfolio.PortfolioManager;
import com.robotrader.analyzer.strategy.StrategyManager;
import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.service.AsyncAdapterService;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author aav
 */
public class MockStrategyTrader extends AbstractStrategyTrader {
    private final AsyncAdapterService adapterService;
    
    private final Decimal riskThreshold;
    private final Decimal positionThreshold;
    private final Decimal conditionSkip = Decimal.ZERO;
    private final Decimal priceSkip = Decimal.ZERO;
    private Decimal stopLevel = Decimal.NaN;
    
    public MockStrategyTrader(PortfolioManager portfolioManager, ChartManager chartManager, StrategyManager strategyManager, AsyncAdapterService adapterService, Decimal riskThreshold, Decimal positionThreshold) {
        super(portfolioManager.getSecurityPortfolio(chartManager.getSecurity()), chartManager, strategyManager);
        this.adapterService = adapterService;
        this.riskThreshold = riskThreshold;
        this.positionThreshold = positionThreshold;
    }

    @Override
    protected void closePosition(Tick tick) throws Exception {
        removeActiveOrders();
        
        Decimal amount = Decimal.ZERO.minus(portfolio.getPosition());
        Decimal price = (amount.isPositive())?tick.getMaxPrice():tick.getMinPrice();
        
        ConditionalOrder order = createOrder(amount, price);
        tradeOrder(order);
    }

    @Override
    protected void openPosition(Tick tick, Decimal requiredPositionSign) throws Exception {
        removeActiveOrders();
        
        Decimal newPosition = calculateNewPosition(tick, requiredPositionSign);
        Decimal amount = newPosition.minus(portfolio.getPosition());
        Decimal price = (amount.isPositive())?tick.getMaxPrice():tick.getMinPrice();
        
        ConditionalOrder order = createOrder(amount, price);
        tradeOrder(order);
        
        stopLevel = calculateStopPrice(tick, amount, price);
    }

    @Override
    protected void reducePosition(Tick tick) throws Exception {
        removeActiveOrders();
        
        Decimal newPosition = calculateReducedPosition();
        if(newPosition.isNaN()) {
            log.info("Reduction not available");
            return;
        }
        
        Decimal amount = newPosition.minus(portfolio.getPosition());
        Decimal price = (amount.isPositive())?tick.getMaxPrice():tick.getMinPrice();
        ConditionalOrder reductionOrder = createOrder(amount, price);
        
        log.info("Reduction order: " + reductionOrder);
        adapterService.createConditionalOrder(reductionOrder);
        
        ConditionalOrder stopOrder = createStopOrder(Decimal.ZERO.minus(newPosition));
        adapterService.createConditionalOrder(stopOrder);
    }

    @Override
    protected void removeActiveOrders() throws Exception {
        Set<Long> activeOrders = new HashSet<>();
        activeOrders.addAll(portfolio.getActiveOrders().keySet());        
        for (Long activeOrderId : activeOrders) {
            adapterService.removeOrder(activeOrderId);
        }
    }
    
    private ConditionalOrder createOrder(Decimal amount, Decimal level) {
        if(amount.isPositive()) {
            final double condition = level.plus(conditionSkip).toDouble();
            final double price = level.plus(priceSkip).toDouble();
            
            return ConditionalOrder.buyLastUp(0, 0, chartManager.getSecurity(), new Double(amount.abs().toDouble()).longValue(), price, condition);
        } else {
            final double condition = level.minus(conditionSkip).toDouble();
            final double price = level.minus(priceSkip).toDouble();
        
            return ConditionalOrder.sellLastDown(0, 0, chartManager.getSecurity(), new Double(amount.abs().toDouble()).longValue(), price, condition);
        }
    }
    
    private ConditionalOrder createStopOrder(Decimal amount) {        
        if(stopLevel == null || stopLevel.isNaN()) {
            throw new RuntimeException("StopLevel not setted!!!");
        }
        
        ConditionalOrder stopOrder = createOrder(amount, stopLevel);
        log.info("Stop order: " + stopOrder);
        return stopOrder;
    }
    
    private void tradeOrder(ConditionalOrder order) {
        try{
            adapterService.createConditionalOrder(order);
        } catch(Exception e) {
            log.error("Exception", e);
        }
    }
    
    private Decimal calculateNewPosition(Tick tick, Decimal requiredPositionSign) throws Exception {
        Decimal tickModule = tick.getMaxPrice().minus(tick.getMinPrice());
        if(tickModule.isZero()) {
            throw new Exception("TickModule is zero: " + tick);
        }
        Decimal newPosition = positionThreshold;
        int result = new BigDecimal(newPosition.multipliedBy(requiredPositionSign).toDouble()).setScale(0, RoundingMode.DOWN).intValue();
        
        return Decimal.valueOf(result);
    }
    
    private Decimal calculateReducedPosition() {
        Decimal reducedPosition = Decimal.valueOf(Math.rint(portfolio.getPosition().dividedBy(Decimal.TWO).toDouble()));
        
        if(reducedPosition.isZero() || reducedPosition.abs().isGreaterThanOrEqual(portfolio.getPosition().abs())) {
            reducedPosition = Decimal.NaN;
        }
        
        return reducedPosition;
    }
    
    private Decimal calculateStopPrice(Tick tick, Decimal amount, Decimal positionPrice) {
        Decimal stopPrice;
        if(amount.isPositive()) {
            stopPrice = positionPrice.minus(positionPrice.multipliedBy(riskThreshold));
            if(stopPrice.isLessThan(tick.getMinPrice()))stopPrice = tick.getMinPrice();
        } else {
            stopPrice = positionPrice.plus(positionPrice.multipliedBy(riskThreshold));
            if(stopPrice.isGreaterThan(tick.getMaxPrice()))stopPrice = tick.getMaxPrice();
        }
                
        return stopPrice;
    }
    
    @Override
    public void positionChanged(Decimal position) {
        if(!position.isZero()) {
            Decimal amount = Decimal.ZERO.minus(portfolio.getPosition());
            ConditionalOrder stopOrder = createStopOrder(amount);
            tradeOrder(stopOrder);
        }
    }
}
