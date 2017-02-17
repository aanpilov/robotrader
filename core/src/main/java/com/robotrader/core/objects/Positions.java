/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.objects;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author aav
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "positions")
public class Positions {
    @XmlElement(name = "security_position")
    private List<SecurityPosition> securityPositions = new ArrayList<>();

    public List<SecurityPosition> getSecurityPositions() {
        return securityPositions;
    }

    public void setSecurityPositions(List<SecurityPosition> securityPositions) {
        this.securityPositions = securityPositions;
    }
}
