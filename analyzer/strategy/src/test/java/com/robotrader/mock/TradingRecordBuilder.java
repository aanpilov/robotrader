/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.mock;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Order;
import eu.verdelhan.ta4j.TradingRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aav
 */
public class TradingRecordBuilder {
    public static TradingRecord build(Order... orders) {
        List<Order> normalizedOrders = normalizeOrders(orders);
        Order[] ordersArray = new Order[normalizedOrders.size()];
        normalizedOrders.toArray(ordersArray);
        
        return new TradingRecord(ordersArray);
    }

    private static List<Order> normalizeOrders(Order[] orders) {
        List<Order> normalized = new ArrayList<>();
        
        double currentPosition = 0;
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            
            if(currentPosition == 0) {
                normalized.add(order);
                currentPosition = order.getAmount().toDouble();
                continue;
            }
            
            if(currentPosition == order.getAmount().toDouble()) {
                normalized.add(order);
                currentPosition = 0;
                continue;
            }
            
            double newPosition = order.getAmount().toDouble() - currentPosition;
            if(currentPosition > 0) {
                Order stopOrder = Order.sellAt(order.getIndex(), order.getPrice(), Decimal.valueOf(currentPosition));
                Order newOrder = Order.sellAt(order.getIndex(), order.getPrice(), Decimal.valueOf(newPosition));
                
                normalized.add(stopOrder);
                normalized.add(newOrder);
            } else {
                Order stopOrder = Order.buyAt(order.getIndex(), order.getPrice(), Decimal.valueOf(currentPosition).abs());
                Order newOrder = Order.buyAt(order.getIndex(), order.getPrice(), Decimal.valueOf(newPosition).abs());
                
                normalized.add(stopOrder);
                normalized.add(newOrder);
            }
        }
        
        return normalized;
    }
}
