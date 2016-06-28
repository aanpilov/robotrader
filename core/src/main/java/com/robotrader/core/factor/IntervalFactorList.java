/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.*;

/**
 *
 * @author 1
 */
@XmlType(name = "INTERVAL_FACTOR_LIST")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "INTERVAL_FACTOR_LIST")
public class IntervalFactorList {

    @XmlElement(name = "INTERVAL_FACTOR")
    private Set<IntervalFactor> factorSet = new HashSet<IntervalFactor>();

    public void add(IntervalFactor factor) {
        factorSet.add(factor);
    }
    
    public void addAll(IntervalFactorList factorList) {
        this.factorSet.addAll(factorList.getFactors());
    }

    public IntervalFactor get(Paper paper) {
        for (IntervalFactor factor : factorSet) {
            if (factor.getPaper().equals(paper)) {
                return factor;
            }
        }
        return null;
    }

    public void setFactors(Collection<IntervalFactor> factorCollection) {
        factorSet.clear();
        factorSet.addAll(factorCollection);
    }
    
    public Collection<IntervalFactor> getFactors() {
        return factorSet;
    }

    public int size() {
        return factorSet.size();
    }
}
