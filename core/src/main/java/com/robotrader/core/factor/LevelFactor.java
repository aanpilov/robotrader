/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author 1
 */
@XmlType(name="LEVEL_FACTOR")
@XmlAccessorType(XmlAccessType.FIELD)
public class LevelFactor implements Comparable<LevelFactor> {
    /**
     * Код уровня
     */
    @XmlAttribute(name = "CODE")
    private String code;
    
    /**
     * Код базового фактора, к которому относится уровень
     */
    @XmlAttribute(name = "BASE_CODE")
    private String baseCode;
    
    /**
     * Значение
     */
    @XmlAttribute(name="VALUE")
    private BigDecimal value;

    public LevelFactor() {
    }

    public LevelFactor(String code, String baseCode, BigDecimal value) {
        this.code = code;
        this.baseCode = baseCode;
        this.value = value;
    }

    public String getCode() {
        return code;
    }
   
    public String getBaseCode() {
        return baseCode;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || ! (obj instanceof LevelFactor)) {
            return false;
        } 
        
        LevelFactor other = (LevelFactor) obj;
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.baseCode != null ? this.baseCode.hashCode() : 0);
        hash = 83 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(LevelFactor o) {
        if(getBaseCode().compareTo(o.getBaseCode()) == 0) {
            return getValue().compareTo(o.getValue());
        } else {
            return getBaseCode().compareTo(o.getBaseCode());
        }
    }
}
