/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author 1
 */
@XmlType(name = "ONLINE_FACTOR_LIST")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ONLINE_FACTOR_LIST")
public class OnlineFactorList {
    @XmlElement(name = "ONLINE_FACTOR")
    private Set<OnlineFactor> factorSet = new HashSet<OnlineFactor>();

    public void add(OnlineFactor factor) {
        factorSet.add(factor);
    }
    
    public void addAll(OnlineFactorList factorList) {
        this.factorSet.addAll(factorList.getFactors());
    }
    
    public OnlineFactor get(Paper paper) {
        for(OnlineFactor factor : factorSet) {
            if(factor.getPaper().equals(paper)) {
                return factor;
            }
        }        
        return null;
    }

    public void setFactors(Collection<OnlineFactor> factorCollection) {
        factorSet.clear();
        factorSet.addAll(factorCollection);        
    }
    
    public Collection<OnlineFactor> getFactors() {
        return factorSet;
    }
    
    public int size() {
        return factorSet.size();
    }
}
