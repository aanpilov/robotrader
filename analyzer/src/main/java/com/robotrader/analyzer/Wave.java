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
public class Wave {
    private boolean up;
    
    private Date startDate;
    private BigDecimal startValue;
    
    private Date endDate;
    private BigDecimal endValue;
    
    private BigDecimal mod;

    public Wave(Date startDate, BigDecimal startValue, Date endDate, BigDecimal endValue) {
        this.startDate = startDate;
        this.startValue = startValue;
        this.endDate = endDate;
        this.endValue = endValue;
        calculateParams();
    }

    public Wave(Pivot start, Pivot end) {
        this(start.getDate(), start.getValue(), end.getDate(), end.getValue());
    }

    public boolean isUp() {
        return up;
    }

    public Date getStartDate() {
        return startDate;
    }

    public BigDecimal getStartValue() {
        return startValue;
    }

    public Date getEndDate() {
        return endDate;
    }

    public BigDecimal getEndValue() {
        return endValue;
    }

    public BigDecimal getMod() {
        return mod;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndValue(BigDecimal endValue) {
        this.endValue = endValue;
        calculateParams();
    }

    @Override
    public String toString() {
        return "Wave{" + "up=" + up + ", startDate=" + startDate + ", startValue=" + startValue + ", endDate=" + endDate + ", endValue=" + endValue + ", mod=" + mod + '}';
    }

    private void calculateParams() {
        mod = endValue.subtract(startValue).abs();
        
        if(endValue.compareTo(startValue) == 1) {
            up = true;
        } else {
            up = false;
        }
    }
    
    
}
