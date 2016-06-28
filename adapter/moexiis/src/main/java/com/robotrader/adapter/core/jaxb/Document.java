/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core.jaxb;

import com.robotrader.adapter.core.jaxb.Data;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author aav
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "document")
//@XmlType(name = "document", propOrder = {"field"})
public class Document {
    @XmlElement(name = "data")
    private Data data;

    public Document() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    
}
