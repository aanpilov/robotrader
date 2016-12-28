/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author aav
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "client_trade")
public class ClientTrade extends Trade {
    /**
     * Номер заявки на бирже
     */
    private int orderNo;
    
    /**
     * примечание
     */
    private String reference;

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
