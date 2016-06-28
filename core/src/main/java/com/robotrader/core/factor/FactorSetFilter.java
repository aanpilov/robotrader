/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author 1
 */
public class FactorSetFilter {
    private Set<Paper> onlineFactorHeaders = new HashSet<Paper>();
    private Set<Paper> intervalFactorHeaders = new HashSet<Paper>();
    private Set<Paper> levelFactorHeaders = new HashSet<Paper>();

    public void setFilter(Set<Paper> online, Set<Paper> interval, Set<Paper> level) {
        this.onlineFactorHeaders = online;
        this.intervalFactorHeaders = interval;
        this.levelFactorHeaders = level;
    }

    public FactorSet doFilter(FactorSet factorSet) {
        for (Iterator<Paper> iter = onlineFactorHeaders.iterator(); iter.hasNext();) {
            Paper paper = iter.next();
            
            Factor factor = factorSet.getOnlineFactors().get(paper);
            if(factor == null) {
                return null;
            }
        }
        
        return factorSet;
    }
}
