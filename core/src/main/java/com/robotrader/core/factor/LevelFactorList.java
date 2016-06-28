/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author 1
 */
@XmlType(name="LEVEL_FACTOR_LIST")
@XmlAccessorType(XmlAccessType.FIELD)
public class LevelFactorList {
    @XmlElement(name="LEVEL_FACTOR")
    private Set<LevelFactor> factorList = new HashSet<LevelFactor>();
    
    public void add(LevelFactor factor) {
        factorList.add(factor);
    }
    
    public void addAll(LevelFactorList extFactorList) {
        this.factorList.addAll(extFactorList.getFactors());
    }

    public List<LevelFactor> getFactorList(String baseCode) {
        List<LevelFactor> result = new ArrayList<LevelFactor>();
        
        for(LevelFactor levelFactor : factorList) {
            if(levelFactor.getBaseCode().equals(baseCode)) {
                result.add(levelFactor);
            }
        }
        
        return result;
    }
    
    public List<LevelFactor> getLevels(String levelCode) {
        List<LevelFactor> result = new ArrayList<LevelFactor>();

        for (LevelFactor levelFactor : factorList) {
            if (levelFactor.getCode().equals(levelCode)) {
                result.add(levelFactor);
            }
        }

        return result;
    }
    
    public List<LevelFactor> getLevels(String levelCode, String baseCode) {
        List<LevelFactor> result = new ArrayList<LevelFactor>();

        for (LevelFactor levelFactor : factorList) {
            if (levelFactor.getCode().equals(levelCode) && levelFactor.getBaseCode().equals(baseCode)) {
                result.add(levelFactor);
            }
        }

        return result;
    }

    public void setFactors(Collection<LevelFactor> factorList) {
        this.factorList.clear();
        this.factorList.addAll(factorList);
    }
    
    public Collection<LevelFactor> getFactors() {
        return factorList;
    }
}
