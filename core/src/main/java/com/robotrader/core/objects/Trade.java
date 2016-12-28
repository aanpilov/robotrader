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
@XmlRootElement(name = "trade")
public class Trade {
    private Security security;
    
    /**
     * Дата сделки
     */
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime date;
    
    /**
     * Цена сделки
     */
    private double price;
    
    /**
     * Объем сделки
     */
    private int quantity;
    
    /**
     * Направление
     */
    private String buySell;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }
}
