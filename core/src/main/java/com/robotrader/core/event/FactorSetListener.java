/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.event;

import java.util.EventListener;

/**
 *
 * @author 1
 */
public interface FactorSetListener extends EventListener {
    public void onOnlineFactorSet(FactorSetEvent factorSetEvent);
    public void onArchiveFactorSet(FactorSetEvent factorSetEvent);
}
