/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.core.util.jaxb.adapter;

import com.robotrader.core.factor.OnlineFactor;
import com.robotrader.core.factor.OnlineFactorList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author 1
 */
public class OnlineFactorMapAdapter extends XmlAdapter<OnlineFactorList, HashMap<String, OnlineFactor>> {

    @Override
    public HashMap<String, OnlineFactor> unmarshal(OnlineFactorList list) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OnlineFactorList marshal(HashMap<String, OnlineFactor> map) throws Exception {
        OnlineFactorList factorList = new OnlineFactorList();
        
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            OnlineFactor onlineFactor = map.get(key);
            factorList.add(onlineFactor);
        }
        
        return factorList;
    }
    
}
