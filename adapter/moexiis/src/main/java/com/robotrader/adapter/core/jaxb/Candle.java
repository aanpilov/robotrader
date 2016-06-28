/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core.jaxb;

import com.robotrader.core.util.jaxb.adapter.DateTimeAdapter;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author aav
 */
@XmlType(name = "tCandle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Candle {
    @XmlAttribute
    private BigDecimal open;
    @XmlAttribute
    private BigDecimal close;
    @XmlAttribute
    private BigDecimal high;
    @XmlAttribute
    private BigDecimal low;
    
    @XmlAttribute
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date begin;
    
    @XmlAttribute
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date end;

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    
    
}
