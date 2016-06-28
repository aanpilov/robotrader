/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.jaxb.adapter;

import com.robotrader.core.factor.IntervalFactor;
import com.robotrader.core.factor.IntervalFactorList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author 1
 */
public class IntervalFactorMapAdapter extends XmlAdapter<IntervalFactorList, HashMap<String, IntervalFactor>>{

    @Override
    public HashMap<String, IntervalFactor> unmarshal(IntervalFactorList v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IntervalFactorList marshal(HashMap<String, IntervalFactor> map) throws Exception {
        IntervalFactorList factorList = new IntervalFactorList();
        
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            IntervalFactor onlineFactor = map.get(key);
            factorList.add(onlineFactor);
        }
        
        return factorList;
    }
    
}
