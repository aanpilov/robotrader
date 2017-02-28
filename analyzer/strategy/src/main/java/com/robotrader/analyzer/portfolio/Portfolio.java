/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer.portfolio;

import com.robotrader.core.objects.ConditionalOrder;
import eu.verdelhan.ta4j.Decimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanpilov
 */
public class Portfolio {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private Decimal position = Decimal.ZERO;    
    private final Map<Long, ConditionalOrder> activeOrders = new HashMap<>();
    private final Set<PortfolioListener> listeners = new HashSet<>();
    
    public void addPortfolioListener(PortfolioListener listener) {
        listeners.add(listener);
    }
    
    public Decimal getPosition() {
        return position;
    }

    public Map<Long, ConditionalOrder> getActiveOrders() {
        return activeOrders;
    }
    
    public void importOrders(Collection<ConditionalOrder> orders) {
        orders.forEach((newOrder) -> {
            activeOrders.put(newOrder.getOrderId(), newOrder);
        });
        
        Set<Long> removeIdentifiers = activeOrders.values().stream()
        .filter(o -> o.getStatus() != ConditionalOrder.OrderStatus.ACTIVE)
        .map(o -> o.getOrderId()).collect(Collectors.toSet());
        
        removeIdentifiers.forEach((removeIdentifier) -> {
            activeOrders.remove(removeIdentifier);
        });
    }
    
    public void importPositions(long newPosition) {
        if(!position.isEqual(Decimal.valueOf(newPosition))) {
            log.info("Position changed: " + newPosition);
            
            position = Decimal.valueOf(newPosition);
            listeners.forEach(listener -> listener.positionChanged(position));            
        }
    }
}