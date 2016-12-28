/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.service;

import javax.xml.bind.JAXBException;

/**
 *
 * @author aanpilov
 */
public interface JaxbContext {
    public String marshall(Object obj) throws JAXBException;
    public Object unmarshall(String str) throws JAXBException;
}
