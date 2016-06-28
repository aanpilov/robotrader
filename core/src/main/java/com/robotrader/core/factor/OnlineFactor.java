/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import com.robotrader.core.util.jaxb.adapter.BooleanAdapter;
import com.robotrader.core.util.jaxb.adapter.DateTimeAdapter;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author 1
 */
@XmlType(name="ONLINE_FACTOR")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ONLINE_FACTOR")
public class OnlineFactor implements Factor {
    /**
     * Торговый инструмент
     */
    @XmlAttribute(name="PAPER")
    private Paper paper;
    
    /**
     * Дата факта
     */
    @XmlAttribute(name="DATE")
    @XmlJavaTypeAdapter(value=DateTimeAdapter.class)
    private Date date;
    
    /**
     * Признак активности
     */
    @XmlAttribute(name="ACTIVE")
    @XmlJavaTypeAdapter(value=BooleanAdapter.class)
    private Boolean active;
    
    /**
     * Значение
     */
    @XmlAttribute(name="VALUE")
    private BigDecimal value;

    public OnlineFactor() {
    }

    public OnlineFactor(Paper paper, Date date, boolean active, BigDecimal value) {
        this.paper = paper;
        this.date = date;
        this.active = active;        
        this.value = value;
    }

    public boolean isActive() {
        return active;
    }

    public Paper getPaper() {
        return paper;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public boolean isInterval() {
        return false;
    }
}
