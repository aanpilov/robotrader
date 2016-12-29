/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.trader;

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
    
    public Decimal getPosition() {
        return position;
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
}
