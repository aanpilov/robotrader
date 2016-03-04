/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.analyzer;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author aanpilov
 */
public class Pivot {
    private Date date;
    private BigDecimal value;
    private boolean up;

    public Pivot(Date date, BigDecimal value, boolean up) {
        this.date = date;
        this.value = value;
        this.up = up;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    @Override
    public String toString() {
//        String direction = up?" UP ": " DOWN ";
//        return "Pivot " + direction + " AT " + date + " FROM " + value;
        return "Pivot{" + "date=" + date + ", value=" + value + ", up=" + up + '}';
    }
    
    
}
