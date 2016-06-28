/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.jaxb.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author 1
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean>{

    @Override
    public Boolean unmarshal(String v) throws Exception {
        if(v == null) {
            return null;
        }
        
        if(v.equals("1")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public String marshal(Boolean v) throws Exception {
        if(v == null) {
            return null;
        }
        
        if(v.equals(Boolean.TRUE)) {
            return "1";
        } else {
            return "0";
        }
    }
    
}
