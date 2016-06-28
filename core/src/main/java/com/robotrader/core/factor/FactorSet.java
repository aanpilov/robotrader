/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import com.robotrader.core.util.jaxb.adapter.DateTimeAdapter;
import java.util.Date;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author 1
 */
@XmlType(name="FACTOR_SET")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="FACTOR_SET")
public class FactorSet {
     /**
     * Дата набора
     */
    @XmlAttribute(name="DATE")
    @XmlJavaTypeAdapter(value=DateTimeAdapter.class)
    private Date date;
    
    @XmlElement(name="ONLINE")
    private OnlineFactorList onlineFactors = new OnlineFactorList();
    
    @XmlElement(name="INTERVAL")
    private IntervalFactorList intervalFactors = new IntervalFactorList();
    
    @XmlElement(name="LEVEL")
    private LevelFactorList levelFactors = new LevelFactorList();
    
    public IntervalFactorList getIntervalFactors() {
        return intervalFactors;
    }

    public OnlineFactorList getOnlineFactors() {
        return onlineFactors;
    }
    
    public void add(LevelFactor levelFactor) {
        levelFactors.add(levelFactor);
    }

    public void add(OnlineFactor factor) {
        onlineFactors.add(factor);

        if (date == null) {
            date = factor.getDate();
        }
    }
    
    public void add(IntervalFactor factor) {
        intervalFactors.add(factor);
        
        if(date == null) {
            date = factor.getDate();
        }
    }
    
    public void add(FactorSet factorSet) {
        onlineFactors.addAll(factorSet.getOnlineFactors());
        intervalFactors.addAll(factorSet.getIntervalFactors());
        levelFactors.addAll(factorSet.getLevelFactors());
    }

    public LevelFactorList getLevelFactors() {
        return levelFactors;
    }
    
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "FactorSet{" + "date=" + date + ", onlineFactors=" + onlineFactors.size() + ", intervalFactors=" + intervalFactors.size() + '}';
    }
}
