/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core.jaxb;

import com.robotrader.adapter.core.jaxb.Candle;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author aav
 */
@XmlType(name = "tMoexData")
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    @XmlAttribute(name = "id")
    private String id;
    
    @XmlElementWrapper(name = "rows")
    @XmlElement(name="row")
    private List<Candle> candles;

    public Data() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    public void setCandles(List<Candle> candles) {
        this.candles = candles;
    }
}
