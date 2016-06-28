/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.storage.adapter;

import com.robotrader.core.factor.FactorSet;
import com.robotrader.core.factor.FactorSetCollection;
import com.robotrader.core.factor.IntervalFactor;
import com.robotrader.core.factor.OnlineFactor;
import com.robotrader.core.interval.Interval;
import com.robotrader.core.util.jaxb.JAXBUtil;
import com.robotrader.storage.entities.FactorArchive;
import com.robotrader.storage.entities.FactorDao;
import com.robotrader.storage.entities.FactorHeader;
import com.robotrader.storage.entities.FactorHeaderDao;
import com.robotrader.storage.entities.FactorOnline;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author 1
 */
public class FactorStorageImpl implements FactorStorage {

    private FactorHeaderDao factorHeaderDao;
    private FactorDao factorDao;
    private JAXBUtil jaxbUtil;
    private JmsTemplate jmsTemplate;
    
    private Calendar lastArchiveDate = null;
    
    private static final String FACTOR_ARCHIVE_TARGET = "FACTOR.ARCHIVE";

    public void setFactorDao(FactorDao factorArchiveDao) {
        this.factorDao = factorArchiveDao;
    }

    public void setFactorHeaderDao(FactorHeaderDao factorHeaderDao) {
        this.factorHeaderDao = factorHeaderDao;
    }

    public void setJaxbUtil(JAXBUtil jaxbUtil) {
        this.jaxbUtil = jaxbUtil;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    public void importFactorSet(FactorSet factorSet) throws Exception {
        if(lastArchiveDate == null) {
            lastArchiveDate = new GregorianCalendar();
            lastArchiveDate.setTime(factorDao.getLastArchiveStartDate());
            lastArchiveDate.add(Calendar.MINUTE, - lastArchiveDate.get(Calendar.MINUTE) % 15);
            lastArchiveDate.set(Calendar.SECOND, 0);
            lastArchiveDate.set(Calendar.MILLISECOND, 0);
        }
        
        importIntervalFactors(factorSet);
        importOnlineFactors(factorSet);
        
        Calendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(factorSet.getDate());
        currentCalendar.add(Calendar.MINUTE, 1);
        currentCalendar.add(Calendar.MINUTE, - currentCalendar.get(Calendar.MINUTE) % 15);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        
        if(currentCalendar.getTime().after(lastArchiveDate.getTime())) {
            publishChangedArchive(lastArchiveDate.getTime());
            lastArchiveDate.setTime(currentCalendar.getTime());
        }
    }

    private void importIntervalFactors(FactorSet factorSet) throws Exception {
        for (IntervalFactor factor : factorSet.getIntervalFactors().getFactors()) {
            importIntervalFactor(factor);
        }
    }

    private void importOnlineFactors(FactorSet factorSet) throws Exception {
        for (OnlineFactor onlineFactor : factorSet.getOnlineFactors().getFactors()) {
            importOnlineFactor(onlineFactor);
        }
    }
    
    public FactorSet getActiveFactors() throws Exception {
        FactorSet result = new FactorSet();
        
        Set<FactorArchive> activeFactors = factorDao.getActiveFactors();
        for(FactorArchive factorArchive : activeFactors) {
            FactorHeader factorHeader = factorHeaderDao.findById(factorArchive.getFactorId());
            
            IntervalFactor intervalFactor = new IntervalFactor(
                    factorHeader.getCode(), 
                    factorArchive.getStartDate(), 
                    Interval.FIFTEEN_MINUTES.getCode(), 
                    factorArchive.getOpenPrice(), 
                    factorArchive.getClosePrice(), 
                    factorArchive.getMinPrice(), 
                    factorArchive.getMaxPrice());
            
            result.add(intervalFactor);
        }
        
        return result;
    }

    private void importIntervalFactor(IntervalFactor factor) throws Exception {
        FactorHeader factorHeader = factorHeaderDao.findByCode(factor.getHeader());

        FactorArchive factorArchive = new FactorArchive();
        factorArchive.setFactorId(factorHeader.getId());
        factorArchive.setStartDate(factor.getDate());
        factorArchive.setOpenPrice(factor.getOpenValue());
        factorArchive.setClosePrice(factor.getCloseValue());
        factorArchive.setMinPrice(factor.getMinValue());
        factorArchive.setMaxPrice(factor.getMaxValue());
        factorArchive.setVolume(new Long("0"));

        factorDao.importFactor(factorArchive);
    }

    private void importOnlineFactor(OnlineFactor factor) throws Exception {
        FactorHeader factorHeader = factorHeaderDao.findByCode(factor.getHeader());
        
        FactorOnline factorOnline = new FactorOnline();
        factorOnline.setFactorId(factorHeader.getId());
        factorOnline.setDate(factor.getDate());
        factorOnline.setValue(factor.getValue());
        factorOnline.setActive(factor.isActive());
        
        factorDao.importFactor(factorOnline);
    }

    @Override
    public BigDecimal getAverageValue(String factorCode, Interval interval, Long intervalsCount)  throws Exception{
        FactorHeader factorHeader = factorHeaderDao.findByCode(factorCode);
        return factorDao.getAverageValue(factorHeader.getId(), interval, intervalsCount);
    }

    /**
     * Публикация событий изменения архивных данных
     * @param lastArchiveStartDate - дата, от которой ведем отсчет
     */
    private void publishChangedArchive(Date lastArchiveStartDate) throws Exception {
        Set<FactorArchive> archiveFactors = factorDao.getArchiveFactors(lastArchiveStartDate);
        
        FactorSetCollection factorSetCollection = new FactorSetCollection();
        
        for(FactorArchive factorArchive : archiveFactors) {
            FactorHeader factorHeader = factorHeaderDao.findById(factorArchive.getFactorId());
            
            IntervalFactor intervalFactor = new IntervalFactor(
                    factorHeader.getCode(), 
                    factorArchive.getStartDate(), 
                    Interval.FIFTEEN_MINUTES.getCode(), 
                    factorArchive.getOpenPrice(), 
                    factorArchive.getClosePrice(), 
                    factorArchive.getMinPrice(), 
                    factorArchive.getMaxPrice());
            
            factorSetCollection.add(intervalFactor);
        }
        
        //Публикация архивных данных
        for(FactorSet factorSet : factorSetCollection.getCollection()) {
            String factorSetStr = jaxbUtil.marshallObject(factorSet);
            jmsTemplate.convertAndSend(FACTOR_ARCHIVE_TARGET, factorSetStr);
        }
    }
}
