/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.order;

import com.robotrader.core.factor.Paper;

/**
 *
 * @author 1
 */
public abstract class Order {
    //Идентификатор заявки в адаптере
    protected Long id;
    
    //Торговый инструмент
    protected Paper paper;
    
    //Количество
    protected Long quantity;
    
    //Покупка/продажа
    protected OrderAction action;

    public Order(Long id, Paper paper, Long quantity, OrderAction action) {
        this.id = id;
        this.paper = paper;
        this.quantity = quantity;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OrderAction getAction() {
        return action;
    }

    public void setAction(OrderAction action) {
        this.action = action;
    }
}
