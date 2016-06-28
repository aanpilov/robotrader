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
public class StopOrder extends Order {
    //Стоп-цена
    private BigDecimal price;
    
    //Проскальзывание
    private BigDecimal slippage;

    public StopOrder(Long id, Paper paper, Long quantity, OrderAction action, BigDecimal price, BigDecimal slippage) {
        super(id, paper, quantity, action);
        this.price = price;
        this.slippage = slippage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal stopPrice) {
        this.price = stopPrice;
    }

    public BigDecimal getSlippage() {
        return slippage;
    }

    public void setSlippage(BigDecimal slippage) {
        this.slippage = slippage;
    }
}
