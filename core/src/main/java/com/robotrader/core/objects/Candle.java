/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.objects;

import com.robotrader.adapter.core.objects.adapters.LocalDateTimeAdapter;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author aav
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "candle")
public class Candle {
    private Security security;
    
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime date;
    
    private double open;
    
    private double high;
    
    private double low;
    
    private double close;
    
    private int volume;
    
    private int oi;

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }
    
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getOi() {
        return oi;
    }

    public void setOi(int oi) {
        this.oi = oi;
    }
    
    @Override
    public String toString() {
        return "Candle{" + "date=" + date + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", volume=" + volume + ", oi=" + oi + '}';
    }
}
