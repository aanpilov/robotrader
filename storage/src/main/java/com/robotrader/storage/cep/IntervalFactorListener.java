/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.cep;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.IntervalFactor;
import com.robotrader.core.interval.Interval;
import com.robotrader.storage.adapter.FactorStorage;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author 1
 */
public class IntervalFactorListener implements UpdateListener {
    private Logger log = Logger.getLogger(getClass());
    private FactorStorage factorStorage;

    public IntervalFactorListener(FactorStorage factorStorage) {
        this.factorStorage = factorStorage;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        FactorSet factorSet = new FactorSet();
        log.debug("new Event occured...");
        
        for(EventBean event : newEvents) {
            String header = (String) event.get("header");
            Date date = (Date) event.get("date");
            BigDecimal openValue = (BigDecimal) event.get("openValue");
            BigDecimal closeValue = (BigDecimal) event.get("closeValue");
            BigDecimal minValue = (BigDecimal) event.get("minValue");
            BigDecimal maxValue = (BigDecimal) event.get("maxValue");
            
            Calendar factorCalendar = new GregorianCalendar();
            factorCalendar.setTime(date);
            factorCalendar.set(Calendar.SECOND, 0);
            factorCalendar.set(Calendar.MILLISECOND, 0);
            
            IntervalFactor intervalFactor = new IntervalFactor(header, factorCalendar.getTime(), Interval.ONE_MINUTE.getCode(), openValue, closeValue, minValue, maxValue);
            factorSet.add(intervalFactor);            
        }
        try {
            log.debug("start save FactorSet: " + factorSet);
            factorStorage.importFactorSet(factorSet);
            log.debug("finish save FactorSet...");
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
