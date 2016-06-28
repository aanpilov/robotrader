/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.order;

import com.robotrader.core.factor.Paper;
import java.math.BigDecimal;

/**
 *
 * @author 1
 */
public class LimitOrder extends Order {
    //Цена лимитированной заявки
    private BigDecimal price;

    public LimitOrder(Long id, Paper paper, Long quantity, OrderAction action, BigDecimal price) {
        super(id, paper, quantity, action);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
