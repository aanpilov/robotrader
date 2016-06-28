/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.process;

import com.robotrader.core.factor.FactorSet;

/**
 *
 * @author 1
 */
public interface FactorSetProcessor {
    public FactorSet process(FactorSet factorSet) throws Exception;
}
