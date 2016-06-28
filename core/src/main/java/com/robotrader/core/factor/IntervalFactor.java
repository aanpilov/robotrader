/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import com.robotrader.core.interval.Interval;
import com.robotrader.core.util.jaxb.adapter.DateTimeAdapter;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author 1
 */
@XmlType(name="INTERVAL_FACTOR")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="INTERVAL_FACTOR")
public class IntervalFactor implements Factor {
    /**
     * Торговый инструмент
     */
    @XmlAttribute(name="PAPER")
    private Paper paper;
    
    /**
     * Дата начала интервала
     */
    @XmlAttribute(name="DATE")
    @XmlJavaTypeAdapter(value=DateTimeAdapter.class)
    private Date date;
    
    /**
     * Интервал
     */
    @XmlAttribute(name="INTERVAL")
    private Interval interval;
    
    /**
     * Первое значение в интервале
     */
    @XmlAttribute(name="OPEN")
    private BigDecimal openValue;
    
    /**
     * Последнее значение в интервале
     */
    @XmlAttribute(name="CLOSE")
    private BigDecimal closeValue;
    
    /**
     * Минимальное значение в интервале
     */
    @XmlAttribute(name="MIN")
    private BigDecimal minValue;
    
    /**
     * Максимальное значение в интервале
     */
    @XmlAttribute(name="MAX")
    private BigDecimal maxValue;

    public IntervalFactor() {
    }

    public IntervalFactor(Paper paper, Date openDate, Interval interval, BigDecimal openValue, BigDecimal closeValue, BigDecimal minValue, BigDecimal maxValue) {
        this.paper = paper;
        this.date = openDate;
        this.interval = interval;
        this.openValue = openValue;
        this.closeValue = closeValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public BigDecimal getCloseValue() {
        return closeValue;
    }

    public Paper getPaper() {
        return paper;
    }

    public Interval getInterval() {
        return interval;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getOpenValue() {
        return openValue;
    }

    public boolean isInterval() {
        return true;
    }
}
