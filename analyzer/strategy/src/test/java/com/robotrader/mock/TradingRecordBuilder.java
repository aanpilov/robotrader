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
        
        Decimal currentPosition = Decimal.ZERO;
        List<Order> increaseOrders = new ArrayList();
        List<Order> reduceOrders = new ArrayList();
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            
            Decimal amount = (order.isBuy())?order.getAmount():order.getAmount().multipliedBy(Decimal.valueOf(-1));
            
            if(currentPosition.isZero()) {
                increaseOrders.add(order);
                currentPosition = currentPosition.plus(amount);
                continue;
            }
            
            if(isOneSign(currentPosition, amount)) {
                //One direction
                currentPosition = currentPosition.plus(amount);
                increaseOrders.add(order);
                continue;
            }
            
            
            //Diff direction
            Decimal newPosition = currentPosition.plus(amount);
            if(isOneSign(currentPosition, newPosition)) {
                //Reduction
                reduceOrders.add(order);
                currentPosition = newPosition;
                continue;
            }
            
            if(newPosition.isZero()) {
                reduceOrders.add(order);
                currentPosition = newPosition;
                
                Order increaseOrder = createAvgOrder(increaseOrders);
                increaseOrders.clear();
                Order reduceOrder = createAvgOrder(reduceOrders);
                reduceOrders.clear();
                
                normalized.add(increaseOrder);
                normalized.add(reduceOrder);
                continue;
            }
            
            //Change direction
            Order newOrder = null;
            if(currentPosition.isPositive()) {
                Order stopOrder = Order.sellAt(order.getIndex(), order.getPrice(), currentPosition.abs());
                reduceOrders.add(stopOrder);
                
                newOrder = Order.sellAt(order.getIndex(), order.getPrice(), newPosition);
            } else {
                Order stopOrder = Order.buyAt(order.getIndex(), order.getPrice(), currentPosition.abs());
                reduceOrders.add(stopOrder);
                
                newOrder = Order.buyAt(order.getIndex(), order.getPrice(), newPosition);
            }
            
            Order increaseOrder = createAvgOrder(increaseOrders);
            increaseOrders.clear();
            Order reduceOrder = createAvgOrder(reduceOrders);
            reduceOrders.clear();

            increaseOrders.add(newOrder);
            currentPosition = newPosition;
            
            normalized.add(increaseOrder);
            normalized.add(reduceOrder);
        }
        
        return normalized;
    }

    private static boolean isOneSign(Decimal currentPosition, Decimal amount) {
        return currentPosition.multipliedBy(amount).isPositive();
    }

    private static Order createAvgOrder(List<Order> increaseOrders) {
        Decimal totalPrice = Decimal.ZERO;
        Decimal totalAmount = Decimal.ZERO;
        int lastIndex = 0;
        
        for (Order order : increaseOrders) {
            Decimal amount = order.isBuy()?order.getAmount():order.getAmount().multipliedBy(Decimal.valueOf(-1));
            totalAmount = totalAmount.plus(amount);
            totalPrice = totalPrice.plus(amount.multipliedBy(order.getPrice()));
            lastIndex = order.getIndex();
        }
        
        Decimal price = totalPrice.dividedBy(totalAmount).abs();
        if(totalAmount.isPositive()) {
            return Order.buyAt(lastIndex, price, totalAmount.abs());
        } else {
            return Order.sellAt(lastIndex, price, totalAmount.abs());
        }
    }
}
