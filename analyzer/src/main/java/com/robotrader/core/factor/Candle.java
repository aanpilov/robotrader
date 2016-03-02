/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.factor;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 1
 */
public class Candle {
    /**
     * Торговый инструмент
     */
    private Paper paper;
    
    /**
     * Дата начала интервала
     */
    private Date date;
    
    /**
     * Интервал
     */
    private Interval interval;
    
    /**
     * Первое значение в интервале
     */
    private BigDecimal openValue;
    
    /**
     * Последнее значение в интервале
     */
    private BigDecimal closeValue;
    
    /**
     * Минимальное значение в интервале
     */
    private BigDecimal minValue;
    
    /**
     * Максимальное значение в интервале
     */
    private BigDecimal maxValue;
    
    /**
     * Объем
     */
    private BigDecimal volume;

    public Candle(Paper paper, Date date, Interval interval, BigDecimal openValue, BigDecimal closeValue, BigDecimal minValue, BigDecimal maxValue, BigDecimal volume) {
        this.paper = paper;
        this.date = date;
        this.interval = interval;
        this.openValue = openValue;
        this.closeValue = closeValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.volume = volume;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public BigDecimal getOpenValue() {
        return openValue;
    }

    public void setOpenValue(BigDecimal openValue) {
        this.openValue = openValue;
    }

    public BigDecimal getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(BigDecimal closeValue) {
        this.closeValue = closeValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Candle{" + "paper=" + paper + ", date=" + date + ", interval=" + interval + ", openValue=" + openValue + ", closeValue=" + closeValue + ", minValue=" + minValue + ", maxValue=" + maxValue + ", volume=" + volume + '}';
    }
}
