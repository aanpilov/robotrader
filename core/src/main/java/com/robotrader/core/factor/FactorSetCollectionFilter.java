/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.Iterator;

/**
 *
 * @author 1
 */
public class FactorSetCollectionFilter {
    private FactorSetFilter filter;

    public void setFilter(FactorSetFilter filter) {
        this.filter = filter;
    }
    
    public FactorSetCollection doFilter(FactorSetCollection collection) {
        FactorSetCollection targetCollection = new FactorSetCollection();
        
        if(filter != null) {
            for (Iterator iter = collection.getCollection().iterator(); iter.hasNext();) {
                FactorSet factorSet = (FactorSet) iter.next();
                if(filter.doFilter(factorSet) != null) {
                    targetCollection.add(factorSet);
                }
            }
        }
        
        return targetCollection;
    }
}
