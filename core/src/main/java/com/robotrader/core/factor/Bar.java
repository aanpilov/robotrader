/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import com.robotrader.core.interval.Interval;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author 1
 */
public class Bar extends IntervalFactor {

    public Bar(Paper paper, Date openDate, Interval interval, BigDecimal openValue, BigDecimal closeValue, BigDecimal minValue, BigDecimal maxValue, BigDecimal volume) {
        super(paper, openDate, interval, openValue, closeValue, minValue, maxValue);
        this.volume = volume;
    }
    
    /**
     * Объем
     */
    @XmlAttribute(name="VOLUME")
    private BigDecimal volume;

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Bar{" + "paper=" + getPaper() + ", date=" + getDate() + ", interval=" + getInterval() + ", openValue=" + getOpenValue() + ", closeValue=" + getCloseValue() + ", minValue=" + getMinValue() + ", maxValue=" + getMaxValue() + ", volume=" + volume + '}';
    }
}
