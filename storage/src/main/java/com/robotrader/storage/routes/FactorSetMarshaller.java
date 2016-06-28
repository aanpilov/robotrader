/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.routes;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.util.jaxb.JAXBUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author 1
 */
public class FactorSetMarshaller {
    @Autowired
    private JAXBUtil jaxbUtil;
    
    public String marshall(FactorSet factorSet) throws Exception {
        return jaxbUtil.marshallObject(factorSet);
    }
    
    public FactorSet unmarshall(String source) throws Exception {
        return (FactorSet) jaxbUtil.unmarshallObject(source, FactorSet.class);
    }
}
