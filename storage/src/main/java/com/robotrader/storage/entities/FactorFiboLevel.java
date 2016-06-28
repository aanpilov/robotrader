/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.entities;

import java.math.BigDecimal;

/**
 *
 * @author 1
 */
public class FactorFiboLevel {
    private Long factorId;
    
    private BigDecimal lowLevel;
    
    private BigDecimal highLevel;

    public Long getFactorId() {
        return factorId;
    }

    public void setFactorId(Long factorId) {
        this.factorId = factorId;
    }

    public BigDecimal getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(BigDecimal lowLevel) {
        this.lowLevel = lowLevel;
    }

    public BigDecimal getHighLevel() {
        return highLevel;
    }

    public void setHighLevel(BigDecimal highLevel) {
        this.highLevel = highLevel;
    }

    @Override
    public String toString() {
        return "FactorFiboLevel{" + "factorId=" + factorId + ", lowLevel=" + lowLevel + ", highLevel=" + highLevel + '}';
    }
}
