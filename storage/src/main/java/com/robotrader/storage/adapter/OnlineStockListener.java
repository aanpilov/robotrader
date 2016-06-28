/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.adapter;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.util.jaxb.JAXBUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author 1
 */
public class OnlineStockListener {
    private Logger log = Logger.getLogger(getClass());
    
    private JAXBUtil jaxbUtil;
    private FactorStorage factorStorage;
    private OnlineFactorChangeHandler onlineFactorChangeListener;

    public void setJaxbUtil(JAXBUtil jaxbUtil) {
        this.jaxbUtil = jaxbUtil;
    }

    public void setFactorStorage(FactorStorage factorStorage) {
        this.factorStorage = factorStorage;
    }

    public void setOnlineFactorChangeListener(OnlineFactorChangeHandler onlineFactorChangeListener) {
        this.onlineFactorChangeListener = onlineFactorChangeListener;
    }
            
    public void onMessage(String messageText) {        
        try{
            FactorSet factorSet = (FactorSet) jaxbUtil.unmarshallObject(messageText, FactorSet.class);
            
            factorStorage.importFactorSet(factorSet);  
            
            onlineFactorChangeListener.onChange();
        } catch(Exception e) {
            log.error("Exception: " + e);
        }
    }
}
