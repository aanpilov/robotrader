/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.Tick;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanpilov
 */
public class Portfolio {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private Decimal position = Decimal.ZERO;
    private Order order = null;
    private Order linkedOrder = null;
    
    private final Decimal riskThreshold;
    private final Decimal positionThreshold;

    public Portfolio(Decimal riskThreshold, Decimal positionThreshold) {
        this.riskThreshold = riskThreshold;
        this.positionThreshold = positionThreshold;
    }

    public Decimal getPosition() {
        return position;
    }
    
    public void setOrder(Tick tick, Decimal requiredPositionSign) {
        Decimal tickModule = tick.getMaxPrice().minus(tick.getMinPrice());
        Decimal newPosition = calculateNewPosition(tickModule, requiredPositionSign);
        Decimal amount = newPosition.minus(position);
        
        createOrder(amount, tick);
        
        setLinkedOrder(tick, newPosition);
    }

    private void createOrder(Decimal amount, Tick tick) {
        if(amount.isGreaterThan(Decimal.ZERO)) {
            order = Order.buyAt(0, tick.getMaxPrice(), amount.abs());            
        } else {
            order = Order.sellAt(0, tick.getMinPrice(), amount.abs());            
        }
        
        log.info("Order: " + order);
    }
    
    public void reducePosition(Tick tick) {
        if(linkedOrder != null) {
            Decimal amount = (order.isBuy())?order.getAmount():Decimal.ZERO.minus(order.getAmount());
            createOrder(amount, tick);
            return;
        }
        
        Decimal newPosition = calculateReducedPosition();
        if(newPosition.isNaN()) {
            log.info("Reduction not available");
            return;
        }
        
        if(order.isBuy()) {
            linkedOrder = Order.buyAt(0, order.getPrice(), newPosition.abs());
        } else {
            linkedOrder = Order.sellAt(0, order.getPrice(), newPosition.abs());
        }
        
        log.info("Reduced order: " + linkedOrder);
        
        Decimal amount = Decimal.ZERO.minus(position.minus(newPosition));
        createOrder(amount, tick);
    }
    
    private void setLinkedOrder(Tick tick, Decimal newPosition) {
        if(order != null && !newPosition.isZero()) {
            if(order.isBuy()) {
                linkedOrder = Order.sellAt(0, tick.getMinPrice(), newPosition.abs());
            } else {
                linkedOrder = Order.buyAt(0, tick.getMaxPrice(), newPosition.abs());
            }
        } else {
            linkedOrder = null;
        }
        
        log.info("Linked order: " + linkedOrder);
    }
    
    public Optional<Order> processNewTick(Tick tick) {
        Optional<Order> result = Optional.empty();
        
        if(fulfillOrder(tick)) {
            result = Optional.of(order);
            order = linkedOrder;
            linkedOrder = null;
        }
        
        return result;
    }

    private boolean fulfillOrder(Tick tick) {
        if(order!= null && tick.getMinPrice().isLessThanOrEqual(order.getPrice()) && tick.getMaxPrice().isGreaterThanOrEqual(order.getPrice())) {
            if(order.isBuy()) {
                position = position.plus(order.getAmount());
            } else {
                position = position.minus(order.getAmount());
            }
            
            log.info("Order fulfilled. Position: " + position);
            
            return true;
        }
        return false;
    }

    private Decimal calculateNewPosition(Decimal tickModule, Decimal requiredPositionSign) {
        Decimal newPosition = riskThreshold.dividedBy(tickModule);
        if(newPosition.isGreaterThan(positionThreshold)) {
            newPosition = positionThreshold;
        }
        
        return Decimal.valueOf(Math.rint(newPosition.multipliedBy(requiredPositionSign).toDouble()));
    }
    
    private Decimal calculateReducedPosition() {
        Decimal reducedPosition = Decimal.valueOf(Math.rint(position.dividedBy(Decimal.TWO).toDouble()));
        
        if(reducedPosition.isZero() || reducedPosition.abs().isGreaterThanOrEqual(position.abs())) {
            reducedPosition = Decimal.NaN;
        }
        
        return reducedPosition;
    }
}
