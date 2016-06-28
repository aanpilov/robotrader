package com.robotrader.core.util.jaxb.adapter;

import java.math.BigDecimal;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author anpilov
 */
public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal>{

    @Override
    public BigDecimal unmarshal(String v) throws Exception {
        if(v == null || v.equals("")){
            return null;
        } else {
            return new BigDecimal(v);
        }
    }

    @Override
    public String marshal(BigDecimal v) throws Exception {
        if(v == null){
            return "";
        } else {
            return v.toString();
        }
    }
    
}