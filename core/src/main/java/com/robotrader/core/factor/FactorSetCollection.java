/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;
import javax.xml.bind.annotation.*;

/**
 *
 * @author 1
 */
@XmlType(name="FACTOR_SET_COLLECTION")
@XmlAccessorType(XmlAccessType.FIELD)
public class FactorSetCollection {
    @XmlElementWrapper(name="COLLECTION")
    @XmlElement(name="FACTOR_SET")
    TreeMap<Date, FactorSet> collection = new TreeMap<Date, FactorSet>();
    
    public void add(FactorSet factorSet) {
        collection.put(factorSet.getDate(), factorSet);
    }
    
    public void add(OnlineFactor factor) {
        FactorSet factorSet = getFactorSet(factor.getDate());
        factorSet.add(factor);
    }
    
    public void add(IntervalFactor factor) {
        FactorSet factorSet = getFactorSet(factor.getDate());
        factorSet.add(factor);
    }
    
    private FactorSet getFactorSet(Date date) {
        if(collection.get(date) != null) {
            return collection.get(date);
        } else {
            FactorSet factorSet = new FactorSet();
            collection.put(date, factorSet);
            return factorSet;
        }
    }
    
    public Collection<FactorSet> getCollection() {
        return collection.values();
    }

    @Override
    public String toString() {
        return "FactorSetCollection{" + "from: " + collection.firstKey() + " to: " + collection.lastKey() + " size: " + collection.size() + '}';
    }
}
