/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.event;

import com.robotrader.core.factor.FactorSet;

/**
 *
 * @author 1
 */
public interface FactorSetEventSource {
    public void fireOnlineFactorSet(FactorSet factorSet);
    public void fireArchiveFactorSet(FactorSet factorSet);
}
