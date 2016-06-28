/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.adapter;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.interval.Interval;
import java.math.BigDecimal;

/**
 * @author 1
 * Хранилище факторов
 */
public interface FactorStorage {
    public void importFactorSet(FactorSet factorSet) throws Exception;
    public FactorSet getActiveFactors() throws Exception;
    public BigDecimal getAverageValue(String factorCode, Interval interval, Long intervalsCount) throws Exception;
}
