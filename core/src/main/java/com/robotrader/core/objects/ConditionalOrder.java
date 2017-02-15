/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author aanpilov
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conditional_order")
public class ConditionalOrder {
    public enum OrderStatus{
        ACTIVE,
        MATCHED,        
        CANCELLED
    };
    
    public enum OrderType {
        BUY {
            @Override
            public OrderType complementType() {
                return SELL;
            }
        },
        SELL {
            @Override
            public OrderType complementType() {
                return BUY;
            }
        };
        
        public abstract OrderType complementType();
    }
    
    public enum Condition {
        LAST_UP,
        LAST_DOWN
    }
    
    private long orderId;
    
    private long marketOrderId;
    
    private Security security;
    
    private OrderType type;
    
    private OrderStatus status;
    
    private Double price;
    
    private long quantity;
    
    private Condition condition;
    
    private Double conditionValue;
    
    public static ConditionalOrder buyLastUp(long orderId, long marketOrderId, Security security, long quantity, Double price, Double lastUp) {
        ConditionalOrder order = new ConditionalOrder();
        
        order.setOrderId(orderId);
        order.setMarketOrderId(marketOrderId);
        order.setSecurity(security);
        order.setType(OrderType.BUY);
        order.setStatus(OrderStatus.ACTIVE);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setCondition(Condition.LAST_UP);
        order.setConditionValue(lastUp);
        
        return order;
    }
    
    public static ConditionalOrder sellLastDown(long orderId, long marketOrderId, Security security, long quantity, Double price, Double lastDown) {
        ConditionalOrder order = new ConditionalOrder();
        
        order.setOrderId(orderId);
        order.setMarketOrderId(marketOrderId);
        order.setSecurity(security);
        order.setType(OrderType.SELL);
        order.setStatus(OrderStatus.ACTIVE);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setCondition(Condition.LAST_DOWN);
        order.setConditionValue(lastDown);
        
        return order;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getMarketOrderId() {
        return marketOrderId;
    }

    public void setMarketOrderId(long marketOrderId) {
        this.marketOrderId = marketOrderId;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Double conditionValue) {
        this.conditionValue = conditionValue;
    }
    
    public boolean isBuy() {
        return getType() == OrderType.BUY;
    }
    
    public boolean isSell() {
        return getType() == OrderType.SELL;
    }

    @Override
    public String toString() {
        return "ConditionalOrder{" + "orderId=" + orderId + ", status=" + status + ", type=" + type + ", price=" + price + ", quantity=" + quantity + ", condition=" + condition + ", conditionValue=" + conditionValue + '}';
    }
}
