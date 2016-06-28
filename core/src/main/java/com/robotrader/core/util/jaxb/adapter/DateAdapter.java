/**
 * @version 1.0.0
 * @date 21.12.2010
 *
 * @code 
 * @name
 * 
 * @author user3889
 * @since 21.12.2010
 */

package com.robotrader.core.util.jaxb.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date>{
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    @Override
    public Date unmarshal(String v) throws Exception {
        return sdf.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        if(v == null) {
            return null;
        } else {
            return sdf.format(v);
        }
    }

}