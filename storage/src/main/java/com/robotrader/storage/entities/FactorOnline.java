/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 1
 */
public class FactorOnline {
    private Long factorId;
    
    private Date date;
    
    private BigDecimal value;
    
    private Boolean active;

    public Long getFactorId() {
        return factorId;
    }

    public void setFactorId(Long factorId) {
        this.factorId = factorId;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean isActive) {
        this.active = isActive;
    }

    @Override
    public String toString() {
        return "FactorOnline{" + "factorId=" + factorId + ", date=" + date + ", value=" + value + ", isActive=" + active + '}';
    }
}
