/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.order;

/**
 *
 * @author 1
 */
public enum OrderAction {
    BUY("B"),
    SELL("S");
    
    private final String code;

    private OrderAction(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }    
}
