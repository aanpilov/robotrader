/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.jaxb;

import javax.xml.transform.Source;

/**
 *
 * @author 1
 */
public interface JAXBUtil {
    public Object unmarshallObject(String source, Class targetClass) throws Exception;
    public Object unmarshallObject(Source source, Class targetClass) throws Exception;
    public String marshallObject(Object object) throws Exception;
    public Source marshallObject(Object object, String namespace) throws Exception;
}
