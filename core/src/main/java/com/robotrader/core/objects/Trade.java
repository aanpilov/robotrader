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
import javax.xml.bind.annotation.XmlAttribute;
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
    /**
     * Код торгового раздела
     */
    @XmlAttribute
    private String board;
    
    /**
     * Код инструмента
     */
    @XmlAttribute
    private String secCode;
    
    /**
     * Дата сделки
     */
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @XmlAttribute
    private LocalDateTime date;
    
    /**
     * Цена сделки
     */
    @XmlAttribute
    private double price;
    
    /**
     * Объем сделки
     */
    @XmlAttribute
    private int quantity;
    
    /**
     * Направление
     */
    @XmlAttribute
    private String buySell;

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSecCode() {
        return secCode;
    }

    public void setSecCode(String secCode) {
        this.secCode = secCode;
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
