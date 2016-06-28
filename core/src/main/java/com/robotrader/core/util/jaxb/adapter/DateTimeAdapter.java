package com.robotrader.core.util.jaxb.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author anpilov
 */
public class DateTimeAdapter extends XmlAdapter<String, Date>{
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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