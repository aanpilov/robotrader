/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.trader;

import com.robotrader.analyzer.portfolio.Portfolio;
import com.robotrader.analyzer.chart.ChartManager;
import com.robotrader.analyzer.portfolio.PortfolioListener;
import com.robotrader.analyzer.strategy.StrategyListener;
import com.robotrader.analyzer.strategy.StrategyManager;
import com.robotrader.core.objects.ConditionalOrder;
import com.robotrader.core.service.AsyncAdapterService;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta4j.strategy.Advice;

/**
 *
 * @author aanpilov
 */
public class StrategyTrader implements StrategyListener, PortfolioListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private final Portfolio portfolio;
    private final ChartManager chartManager;    
    private final AsyncAdapterService adapterService;
    
    private final Decimal riskThreshold;
    private final Decimal positionThreshold;
    private Tick positionTick;
    
    public StrategyTrader(Portfolio portfolio, ChartManager chartManager, StrategyManager strategyManager, AsyncAdapterService adapterService, Decimal riskThreshold, Decimal positionThreshold) {
        this.portfolio = portfolio;
        this.chartManager = chartManager;        
        this.adapterService = adapterService;
        this.riskThreshold = riskThreshold;
        this.positionThreshold = positionThreshold;
        
        strategyManager.addStrategyListener(this);
        portfolio.addPortfolioListener(this);
    }
    
    @Override
    public void onAdvice(Tick tick, Advice advice) {
        Tick tradeTick = chartManager.getTickStartedAt(tick.getBeginTime()).get();

        try {
            if(portfolio.getPosition().isZero()) {
                removeActiveOrders();
            }
            
            if (portfolio.getPosition().isPositive() && advice == Advice.EXIT_LONG) {
                closePosition(tradeTick);
            }

            if (portfolio.getPosition().isNegative() && advice == Advice.EXIT_SHORT) {
                closePosition(tradeTick);
            }

            if (!portfolio.getPosition().isPositive() && advice == Advice.ENTER_LONG) {
                openPosition(tradeTick, Decimal.ONE);
            }

            if (!portfolio.getPosition().isNegative() && advice == Advice.ENTER_SHORT) {
                openPosition(tradeTick, Decimal.valueOf(-1));
            }

            if (portfolio.getPosition().isPositive() && advice == Advice.REDUCE_LONG) {
                reducePosition(tradeTick);
            }

            if (portfolio.getPosition().isNegative() && advice == Advice.REDUCE_SHORT) {
                reducePosition(tradeTick);
            }
        } catch (Exception e) {
            log.error("Processing error", e);
            return;
        }
    }
    
    private void closePosition(Tick tick) throws Exception {
        removeActiveOrders();
        
        Decimal amount = Decimal.ZERO.minus(portfolio.getPosition());
        
        ConditionalOrder order = createOrder(amount, tick);
        tradeOrder(order);
    }
    
    private void openPosition(Tick tick, Decimal requiredPositionSign) throws Exception {
        removeActiveOrders();
        
        Decimal newPosition = calculateNewPosition(tick, requiredPositionSign);
        Decimal amount = newPosition.minus(portfolio.getPosition());
        
        ConditionalOrder order = createOrder(amount, tick);
        tradeOrder(order);
        
        positionTick = tick;
    }
    
    private void reducePosition(Tick tick) throws Exception {
        removeActiveOrders();
        
        Decimal newPosition = calculateReducedPosition();
        if(newPosition.isNaN()) {
            log.info("Reduction not available");
            return;
        }
        
        Decimal amount = newPosition.minus(portfolio.getPosition());
        ConditionalOrder reductionOrder = createOrder(amount, tick);
        
        log.info("Reduction order: " + reductionOrder);
        adapterService.createConditionalOrder(reductionOrder);
        
        ConditionalOrder stopOrder = createStopOrder(amount);
        adapterService.createConditionalOrder(stopOrder);
    }
    
    private void removeActiveOrders() throws Exception {
        Set<Long> activeOrders = new HashSet<>();
        activeOrders.addAll(portfolio.getActiveOrders().keySet());        
        for (Long activeOrderId : activeOrders) {
            adapterService.removeOrder(activeOrderId);
        }
    }
    
    private ConditionalOrder createOrder(Decimal amount, Tick tick) {
        if(amount.isGreaterThan(Decimal.ZERO)) {
            return ConditionalOrder.buyLastUp(0, 0, chartManager.getSecurity(), new Double(amount.abs().toDouble()).longValue(), tick.getMaxPrice().toDouble(), tick.getMaxPrice().toDouble());
        } else {
            return ConditionalOrder.sellLastDown(0, 0, chartManager.getSecurity(), new Double(amount.abs().toDouble()).longValue(), tick.getMinPrice().toDouble(), tick.getMinPrice().toDouble());
        }
    }
    
    private ConditionalOrder createStopOrder(Decimal amount) {
        ConditionalOrder order;
        if(amount.isPositive()) {
            Decimal stopLevel = positionTick.getMaxPrice().minus(positionTick.getMaxPrice().multipliedBy(riskThreshold));
            if(stopLevel.isLessThanOrEqual(positionTick.getMinPrice()))stopLevel = positionTick.getMinPrice();
            order = ConditionalOrder.sellLastDown(0, 0, chartManager.getSecurity(), new Double(amount.abs().toDouble()).longValue(), stopLevel.toDouble(), stopLevel.toDouble());
        } else {
            Decimal stopLevel = positionTick.getMinPrice().plus(positionTick.getMinPrice().multipliedBy(riskThreshold));
            if(stopLevel.isGreaterThanOrEqual(positionTick.getMaxPrice()))stopLevel = positionTick.getMaxPrice();
            order = ConditionalOrder.buyLastUp(0, 0, chartManager.getSecurity(), new Double(amount.abs().toDouble()).longValue(), stopLevel.toDouble(), stopLevel.toDouble());
        }
        
        log.info("Stop order: " + order);
        
        return order;
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
        return Decimal.valueOf(Math.rint(newPosition.multipliedBy(requiredPositionSign).toDouble()));
    }
    

    private Decimal calculateReducedPosition() {
        Decimal reducedPosition = Decimal.valueOf(Math.rint(portfolio.getPosition().dividedBy(Decimal.TWO).toDouble()));
        
        if(reducedPosition.isZero() || reducedPosition.abs().isGreaterThanOrEqual(portfolio.getPosition().abs())) {
            reducedPosition = Decimal.NaN;
        }
        
        return reducedPosition;
    }
    
    @Override
    public void positionChanged(Decimal position) {
        if(!position.isZero()) {
            ConditionalOrder stopOrder = createStopOrder(portfolio.getPosition());            
            tradeOrder(stopOrder);
        }
    }
    
    public Optional<Order> getLiquidationOrder() {
        TimeSeries tradeSeries = chartManager.getChart();
        if (portfolio.getPosition().isPositive()) {
            return Optional.of(Order.sellAt(tradeSeries.getEnd(), tradeSeries.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else if (portfolio.getPosition().isNegative()) {
            return Optional.of(Order.buyAt(tradeSeries.getEnd(), tradeSeries.getLastTick().getClosePrice(), portfolio.getPosition().abs()));
        } else {
            return Optional.empty();
        }
    }
}
